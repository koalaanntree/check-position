package com.wnlbs.checkposition.repository;

import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:10 2018/3/20
 * @Description:
 */
public interface CountryProvinceCityDistrictStreetRepository extends JpaRepository<CountryProvinceCityDistrictStreet, Integer> {
    List<CountryProvinceCityDistrictStreet> findByAdcodeStartingWithAndName(String adcode,String name);

    List<CountryProvinceCityDistrictStreet> findByParentidAndLevel(Integer parentid,String level);

}
