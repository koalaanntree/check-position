package com.wnlbs.checkposition.service.impl;

import com.wnlbs.checkposition.converter.ConverteGaoDeJSONDistrictToCountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;
import com.wnlbs.checkposition.repository.CountryProvinceCityDistrictStreetRepository;
import com.wnlbs.checkposition.request.PositionRequest;
import com.wnlbs.checkposition.service.PositionService;
import com.wnlbs.checkposition.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: huangxin
 * @Date: Created in 下午2:17 2018/3/20
 * @Description:
 */
@Service
@Slf4j
public class PositionServiceImpl implements PositionService {

    @Autowired
    private CountryProvinceCityDistrictStreetRepository repository;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 迭代方法
     *
     * @param savePositionMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveToDB(SavePositionMessage savePositionMessage) {
        GaoDeJSONFormatBean bean = savePositionMessage.getGaoDeJSONFormatBean();
        Integer parentId = savePositionMessage.getParentId();
        //遍历第一遍数据
        for (int i = 0; i < bean.getDistricts().length; i++) {
            boolean havePolyLine = false;
            if (StringUtils.isNotEmpty(bean.getDistricts()[i].getPolyline())) {
                havePolyLine = true;
            }
            //格式化数据
            CountryProvinceCityDistrictStreet cityDistrictStreet =
                    ConverteGaoDeJSONDistrictToCountryProvinceCityDistrictStreet.convert(bean.getDistricts()[i], havePolyLine, parentId);
            if (havePolyLine || cityDistrictStreet.getLevel().equals("street")) {
                repository.save(cityDistrictStreet);
            }
            //重置parentId
            parentId = cityDistrictStreet.getId();
            //如果级别为街道的时候终止迭代
            if (!cityDistrictStreet.getLevel().equals("street")) {
                //迭代
                if (0 != bean.getDistricts().length) {
                    //内部循环
                    for (int j = 0; j < bean.getDistricts()[i].getDistricts().length; j++) {
                        //重置参数
                        PositionRequest positionRequest = new PositionRequest();
                        positionRequest.setKeywords(bean.getDistricts()[i].getDistricts()[j].getName());
                        positionRequest.setSubdistrict(1);
                        positionRequest.setExtensions("all");
                        positionRequest.setFilter(cityDistrictStreet.getAdcode().substring(0, 2) + "0000");
                        //拿到请求路径
                        String url = httpUtils.getRealUrl(positionRequest);
                        try {
                            GaoDeJSONFormatBean iteratorBean = httpUtils.queryPolyLine(url);
                            //调用自身saveToDB()
                            SavePositionMessage iteratorSavePositionMessage = new SavePositionMessage(iteratorBean, parentId);
                            saveToDB(iteratorSavePositionMessage);
                        } catch (Exception e) {
                            log.info("error:" + positionRequest.toString());
                        }

                    }

                }
            }

        }


        return false;
    }
}
