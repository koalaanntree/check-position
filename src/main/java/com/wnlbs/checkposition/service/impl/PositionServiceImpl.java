package com.wnlbs.checkposition.service.impl;

import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.repository.CountryProvinceCityDistrictStreetRepository;
import com.wnlbs.checkposition.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: huangxin
 * @Date: Created in 下午2:17 2018/3/20
 * @Description:
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private CountryProvinceCityDistrictStreetRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveToDB(CountryProvinceCityDistrictStreet countryProvinceCityDistrictStreet) {
        CountryProvinceCityDistrictStreet save = repository.save(countryProvinceCityDistrictStreet);
        if (null!=save.getId()) {
            //保存成功
            return true;
        }
        //保存失败
        return false;
    }
}
