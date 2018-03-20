package com.wnlbs.checkposition.repository;

import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:10 2018/3/20
 * @Description:
 */
public interface CountryProvinceCityDistrictStreetRepository extends JpaRepository<CountryProvinceCityDistrictStreet,Integer> {
}
