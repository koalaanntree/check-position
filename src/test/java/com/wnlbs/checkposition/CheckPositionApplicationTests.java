package com.wnlbs.checkposition;

import com.wnlbs.checkposition.dataobject.CenterPonint;
import com.wnlbs.checkposition.dataobject.EdgePoint;
import com.wnlbs.checkposition.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CheckPositionApplicationTests {

	@Test
	public void checkisexist() throws Exception{

		//IO测试
		StringBuilder stringBuilder = new StringBuilder();
		String str;
		String file = "/Users/huangxin/Desktop/demoprojects/check-position/src/main/resources/doc/polyline/province/420000湖北省.txt";
		try (BufferedReader bre = new BufferedReader(new FileReader(file))){
			while ((str = bre.readLine()) != null) {
				stringBuilder.append(str);
			}
		}
		log.info(stringBuilder.toString());

		//判断测试
		MapUtils mapTools = new MapUtils();
		//拿到点坐标集合|
		String points = stringBuilder.toString();
		//拆分|
		List<List<EdgePoint>> edgePoints = MapUtils.getEdgeListList(points);
		//初始化模型集合
		List<GeneralPath> generalPaths = new ArrayList<>();
		//初始化检查结果
		Set<String> checkResultSet= new HashSet<>();
		//遍历边界坐标点集合的集合，拿到单个坐标点集合
		for(List<EdgePoint> edgePointList:edgePoints) {
			//生成
			GeneralPath generalPath = mapTools.genGraphEdge(edgePointList);
			generalPaths.add(generalPath);
			CenterPonint centerPonint = new CenterPonint("114.341745","30.546557");
			boolean isIn = mapTools.checkPointIn(centerPonint, generalPath);
//			System.out.println(isIn == true ? "在围栏里" : "不在围栏里");
			String result;
			if (isIn == true) {
				result = "yes";
			} else {
				result = "no";
			}
			checkResultSet.add(result);
		}
		System.out.println(checkResultSet.contains("yes")?"在围栏里" : "不在围栏里");

	}

}
