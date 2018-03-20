CREATE TABLE `country_province_city_district_street` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT "主键",
  `parentid` int(32) DEFAULT NULL COMMENT "上级行政区id",
  `citycode` varchar(32) DEFAULT NULL COMMENT "所属城市编码",
  `adcode` varchar(32) DEFAULT NULL COMMENT "所属位置邮编",
  `name` varchar(128) DEFAULT NULL COMMENT "名称全称",
  `polyline` varchar(128) DEFAULT NULL COMMENT "边界线存储地址",
  `center` varchar(32) DEFAULT NULL COMMENT "中心点坐标",
  `level` varchar(16) DEFAULT NULL COMMENT "行政级别",
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;