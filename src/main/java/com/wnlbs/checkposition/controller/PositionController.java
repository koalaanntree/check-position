package com.wnlbs.checkposition.controller;

import com.wnlbs.checkposition.dataobject.*;
import com.wnlbs.checkposition.enums.ProvincesEnum;
import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;
import com.wnlbs.checkposition.request.PositionRequest;
import com.wnlbs.checkposition.response.CheckResult;
import com.wnlbs.checkposition.service.PositionService;
import com.wnlbs.checkposition.utils.HttpUtils;
import com.wnlbs.checkposition.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.GeneralPath;
import java.io.FileReader;
import java.util.*;

/**
 * @Author: huangxin
 * @Date: Created in 上午9:33 2018/3/20
 * @Description: 定位服务
 */
@RestController
@Slf4j
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private PositionService positionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取边界线并入库
     */
    @PostMapping("/initdb")
    @ResponseBody
    public String getPolyLine(@RequestBody PositionRequest positionRequest) throws Exception {
        //拿到请求对象
        log.info("positionRequest:" + positionRequest.toString());
        //拿到请求路径
        String url = httpUtils.getRealUrl(positionRequest);
        //第一步，进入正题，拿到原文的响应对象，并且转化为项目中自己的bean
        GaoDeJSONFormatBean bean = httpUtils.queryPolyLine(url);
        //发送消息到消息队列，利用队列存储数据
        SavePositionMessage savePositionMessage = new SavePositionMessage(bean, 0);
        amqpTemplate.convertAndSend("check.position", "DBData", savePositionMessage);
        log.info("bean:" + bean.toString());
        return "OK";
    }

    /**
     * 在获取边界线后将数据载入缓存
     *
     * @return
     */
    @PostMapping("/initcache")
    public String cacheGEOModel() throws Exception {
        //拿到核心工具类
        MapUtils mapUtils = new MapUtils();
        ProvincesEnum[] provincesEnums = ProvincesEnum.values();
        for (ProvincesEnum provincesEnum : provincesEnums) {
            String adcode = provincesEnum.getFilter().substring(0, 2);
            String name = provincesEnum.getProvinceName();
            //拿到省级数据
            List<CountryProvinceCityDistrictStreet> provincesList = positionService.findByAdcodeStartingWithAndName(adcode, name);
            CountryProvinceCityDistrictStreet province = provincesList.get(0);
            //拿到模型的集合
            Vector<GeneralPath> provinceGeneralPaths = mapUtils.getGeneralPaths(province.getPolyline());
            //构造存入redis的参数
            PositionBaseData provincePositionBaseData = new PositionBaseData();
            provincePositionBaseData.setGeneralPaths(provinceGeneralPaths);
            //名称、级别、邮编
            provincePositionBaseData.setCachedKey(province.getName() + province.getLevel() + province.getAdcode());
            provincePositionBaseData.setModelLevel(province.getLevel());
            provincePositionBaseData.setModelName(province.getName());
            //拿到单个省的市级数据
            List<CountryProvinceCityDistrictStreet> citylist = positionService.findByParentidAndLevel(province.getId(), "city");
            //处理单个城市的信息
            for (CountryProvinceCityDistrictStreet city : citylist) {
                //构建单个城市的模型
                //拿到城市模型的集合
                Vector<GeneralPath> cityGeneralPaths = mapUtils.getGeneralPaths(city.getPolyline());
                //构造存入redis的参数
                PositionBaseData cityPositionBaseData = new PositionBaseData();
                cityPositionBaseData.setGeneralPaths(cityGeneralPaths);
                //名称、级别、邮编
                cityPositionBaseData.setCachedKey(city.getName() + city.getLevel() + city.getAdcode());
                cityPositionBaseData.setModelLevel(city.getLevel());
                cityPositionBaseData.setModelName(city.getName());
                //拿到单个市的区级数据
                List<CountryProvinceCityDistrictStreet> districtList = positionService.findByParentidAndLevel(city.getId(), "district");
                //处理单个区的信息
                for (CountryProvinceCityDistrictStreet district : districtList) {
                    //构建单个区县的模型
                    //拿到模型的集合
                    Vector<GeneralPath> districtGeneralPaths = mapUtils.getGeneralPaths(district.getPolyline());
                    //构造存入redis的参数
                    PositionBaseData districtPositionBaseData = new PositionBaseData();
                    districtPositionBaseData.setGeneralPaths(districtGeneralPaths);
                    //名称、级别、邮编
                    districtPositionBaseData.setCachedKey(district.getName() + district.getLevel() + district.getAdcode());
                    districtPositionBaseData.setModelLevel(district.getLevel());
                    districtPositionBaseData.setModelName(district.getName());
                    //存入区县信息 格式:eg: district:湖北省:武汉市:昌平区
                    redisTemplate.opsForValue().set(
                            districtPositionBaseData.getModelLevel()
                                    + ":" + provincePositionBaseData.getModelName()
                                    + ":" + cityPositionBaseData.getModelName()
                                    + ":" + districtPositionBaseData.getModelName()
                            , districtPositionBaseData.getGeneralPaths()
                    );
                }
                //存入城市信息 格式:eg: city:湖北省:武汉市
                redisTemplate.opsForValue().set(cityPositionBaseData.getModelLevel()
                                + ":" + provincePositionBaseData.getModelName()
                                + ":" + cityPositionBaseData.getModelName()
                        , cityPositionBaseData.getGeneralPaths());
            }
            //存入省份信息 格式:eg: province:湖北省
            redisTemplate.opsForValue().set
                    (provincePositionBaseData.getModelLevel()
                                    + ":" + provincePositionBaseData.getModelName()
                            , provincePositionBaseData.getGeneralPaths());
        }


        return "OK";
    }

    /**
     * 判断是否存在在版图里
     */
    @PostMapping("/checkexist")
    public CheckResult check(@RequestBody CenterPonint centerPonint) {
        //初始化通用工具
        MapUtils mapUtils = new MapUtils();
        //初始化返回结果
        CheckResult checkResult = new CheckResult();

        //轮询省
        ProvincesEnum[] provincesEnums = ProvincesEnum.values();
        for (ProvincesEnum provincesEnum : provincesEnums) {
            String adcode = provincesEnum.getFilter().substring(0, 2);
            String name = provincesEnum.getProvinceName();
            //拿到省级数据
            List<CountryProvinceCityDistrictStreet> provincesList = positionService.findByAdcodeStartingWithAndName(adcode, name);
            CountryProvinceCityDistrictStreet province = provincesList.get(0);
            //备入缓存 province:湖北省
            String redisProvinceKey = "province:" + province.getName();
            Vector<GeneralPath> generalProvincePaths = (Vector<GeneralPath>) redisTemplate.opsForValue().get(redisProvinceKey);
            Set<Boolean> judgeProvinceSet = new HashSet<>();
            for (GeneralPath generalPath : generalProvincePaths) {
                boolean b = mapUtils.checkPointIn(centerPonint, generalPath);
                judgeProvinceSet.add(b);
            }
            //拼装返回结果省级别
            if (judgeProvinceSet.contains(true)) {
                log.info("centerPonint : " + centerPonint.toString() + " located in : " + redisProvinceKey);
                checkResult.setProvince(StringUtils.substringAfter(redisProvinceKey, "province:"));
            }
            //拿到当前省份所有市信息
            List<CountryProvinceCityDistrictStreet> cities = positionService.findByParentidAndLevel(province.getId(), "city");
            //轮询市
            for (CountryProvinceCityDistrictStreet city : cities) {
                //备入缓存 city:湖北省:武汉市
                String redisCityKey = "city" + ":" + provincesEnum.getProvinceName() + ":" + city.getName();
                Vector<GeneralPath> generalCityPaths = (Vector<GeneralPath>) redisTemplate.opsForValue().get(redisCityKey);
                Set<Boolean> judgeCitySet = new HashSet<>();
                for (GeneralPath generalPath : generalCityPaths) {
                    boolean b = mapUtils.checkPointIn(centerPonint, generalPath);
                    judgeCitySet.add(b);
                }
                //拼装返回结果市级别
                if (judgeCitySet.contains(true)) {
                    log.info("centerPonint : " + centerPonint.toString() + " located in : " + redisCityKey);
                    checkResult.setCity(StringUtils.substringAfter(redisCityKey, "city" + ":" + provincesEnum.getProvinceName() + ":"));
                }
                //拿到当前市所有区县信息
                List<CountryProvinceCityDistrictStreet> districts = positionService.findByParentidAndLevel(city.getId(), "district");
                //轮询区县
                for (CountryProvinceCityDistrictStreet district : districts) {
                    //备入缓存 district:湖北省:武汉市:昌平区
                    String redisDistrictKey = "district"
                            + ":" + provincesEnum.getProvinceName()
                            + ":" + city.getName()
                            + ":" + district.getName();
                    Vector<GeneralPath> generalDistrictPaths = (Vector<GeneralPath>) redisTemplate.opsForValue().get(redisDistrictKey);
                    Set<Boolean> judgeDistrictSet = new HashSet<>();
                    for (GeneralPath generalPath : generalDistrictPaths) {
                        boolean b = mapUtils.checkPointIn(centerPonint, generalPath);
                        judgeDistrictSet.add(b);
                    }
                    //拼装返回结果区县级别
                    if (judgeDistrictSet.contains(true)) {
                        log.info("centerPonint : " + centerPonint.toString() + " located in : " + redisDistrictKey);
                        checkResult.setDistrict(StringUtils.substringAfter(redisDistrictKey
                                , "district"
                                        + ":" + provincesEnum.getProvinceName()
                                        + ":" + city.getName()
                                        + ":"));
                        return checkResult;
                    }
                }
            }
        }
        return checkResult;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }


}
