package com.wnlbs.checkposition.utils;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.wnlbs.checkposition.dataobject.CenterPonint;
import com.wnlbs.checkposition.dataobject.EdgePoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: huangxin
 * @Date: Created in 下午3:28 2018/3/21
 * @Description:
 */
@Slf4j
public class MapUtils {

    /**
     * 若外层有'|'字符 province
     */
    public static List<List<EdgePoint>> getEdgeListList(String points) {
        String[] toDeals = StringUtils.split(points, "|");
        List<List<EdgePoint>> result = new ArrayList<>();
        for (String toDeal : toDeals) {
            List<EdgePoint> edgePointList = getEdgePointList(toDeal);
            result.add(edgePointList);
        }
        return result;
    }

    /**
     * 手工转化边界坐标集合 内层
     *
     * @return
     */
    public static List<EdgePoint> getEdgePointList(String points) {
        String[] lonLats = StringUtils.split(points, ";");
        List<EdgePoint> edgePoints = new ArrayList<>();
        for (String lonLat : lonLats) {
            String[] pair = StringUtils.split(lonLat, ",");
            EdgePoint edgePoint = new EdgePoint();
            edgePoint.setLongitude(Double.parseDouble(pair[0]));
            edgePoint.setLatitude(Double.parseDouble(pair[1]));
            edgePoints.add(edgePoint);
        }
        return edgePoints;
    }

    /**
     * 绘制边界图形
     *
     * @param edgePointList 边界坐标点集合
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public GeneralPath genGraphEdge(List<EdgePoint> edgePointList) {
        // 初始化图形边界坐标点集合
        List<Point2D.Double> graphEdgePointList = new ArrayList();

        // 遍历边界坐标点（需要按照顺序来遍历）
        for (EdgePoint edgePoint : edgePointList) {
            double polygonPoint_x = edgePoint.getLongitude();
            double polygonPoint_y = edgePoint.getLatitude();
            // 将边界坐标转化为x,y轴图形坐标点
            Point2D.Double graphEdgePoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
            // 添加graphEdgePoint至有序集合
            graphEdgePointList.add(graphEdgePoint);
        }
        GeneralPath generalPath = new GeneralPath();
        // 拿到第一个边界图形坐标点
        Point2D.Double first = graphEdgePointList.get(0);
        // 路径定位到起始点(闭合点)
        generalPath.moveTo(first.x, first.y);
        // 移除已经使用的起始点位置(闭合点)
        graphEdgePointList.remove(0);
        // 从起始点开始绘图
        for (Point2D.Double d : graphEdgePointList) {
            generalPath.lineTo(d.x, d.y);
        }
        // 画图结束移动至起始点(闭合点)
        generalPath.lineTo(first.x, first.y);

        // 完成绘图
        generalPath.closePath();

        // 返回边界图形
        return generalPath;
    }

    /**
     * 判断点是否在边界图形内
     *
     * @param centerPonint 中心点坐标
     * @param graph        闭合图形
     * @return
     */
    public boolean checkPointIn(CenterPonint centerPonint, GeneralPath graph) {
        double p_x = Double.parseDouble(centerPonint.getLongitude());
        double p_y = Double.parseDouble(centerPonint.getLatitude());
        // 将中心点转为2D图形点
        Point2D.Double point = new Point2D.Double(p_x, p_y);
        // 判断是否包含
        return graph.contains(point);
    }


    /**
     * 构建地理模型方法
     */
    public Vector<GeneralPath> getGeneralPaths(String polylinePath) throws Exception {
        //拿到点的坐标集合
        String points = "";
        try (FileReader fileReader = new FileReader(polylinePath)) {
            StringBuilder sb = new StringBuilder();
            int read = 0;
            while ((read = fileReader.read()) != -1) {
                sb.append((char) read);
            }
            points = sb.toString();
        }
        log.info("坐标集合：" + points.substring(0, 100));
        Vector<GeneralPath> generalPaths = new Vector<>();
        List<List<EdgePoint>> edgePoints = MapUtils.getEdgeListList(points);
        // 绘制边界图形
        for (List<EdgePoint> edgePointList : edgePoints) {
            GeneralPath generalPath = genGraphEdge(edgePointList);
            generalPaths.addElement(generalPath);
            GeneralPath gp9 = genGraphEdge(edgePointList);
            generalPaths.addElement(gp9);
        }
        return generalPaths;
    }

}
