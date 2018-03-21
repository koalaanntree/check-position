package com.wnlbs.checkposition.converter;

import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.dataobject.GaoDeJSONDistrict;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:15 2018/3/20
 * @Description:
 */
public class ConverteGaoDeJSONDistrictToCountryProvinceCityDistrictStreet {

    /**
     * 转化数据为国家、省份、城市、区县、街道
     *
     * @param gaoDeJSONDistrict 基本数据
     * @param havePolyLine      是否有边界坐标
     * @param parentId          上级id 如果没有填-1
     * @return
     */
    public static CountryProvinceCityDistrictStreet convert(GaoDeJSONDistrict gaoDeJSONDistrict, boolean havePolyLine, Integer parentId) {
        //实例化对象
        CountryProvinceCityDistrictStreet cityDistrictStreet = new CountryProvinceCityDistrictStreet();
        //是否有parentId
        if (parentId != -1) {
            cityDistrictStreet.setParentid(parentId);
        }
        //拼装基本数据
        cityDistrictStreet.setCitycode(gaoDeJSONDistrict.getCitycode());
        cityDistrictStreet.setAdcode(gaoDeJSONDistrict.getAdcode());
        cityDistrictStreet.setName(gaoDeJSONDistrict.getName());
        cityDistrictStreet.setCenter(gaoDeJSONDistrict.getCenter());
        cityDistrictStreet.setLevel(gaoDeJSONDistrict.getLevel());
        //级别为街道即返回
        if (cityDistrictStreet.getLevel().equals("street")) {
            cityDistrictStreet.setPolyline("street have no polyline");
            return cityDistrictStreet;
        }
        //如果有边界线
        if (havePolyLine) {
            //存放根目录 确保有
            String rootPath = "/Users/huangxin/Desktop/demoprojects/check-position/src/main/resources/doc/polyline";
            //待生成文件夹 国家、省份、城市、区县、街道
            String dirName = gaoDeJSONDistrict.getLevel();
            //待生成文件名 eg:110000北京市.txt
            String fileName = gaoDeJSONDistrict.getAdcode() + gaoDeJSONDistrict.getName() + ".txt";
            //最终存放路径
            String savePath = rootPath + "/" + dirName + "/" + fileName;
            //存入数据
            cityDistrictStreet.setPolyline(savePath);
            //操作文件
            File file = new File(savePath);
            //判断路径是否存在
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //待写入数据
            String toWrite = gaoDeJSONDistrict.getPolyline();
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(toWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cityDistrictStreet;
    }

}
