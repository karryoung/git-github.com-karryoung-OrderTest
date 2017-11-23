package com.li.test.util;


/**
 * 不可修改的文字
 */

public class AbsTextUtil {
    public static final String shanghaiStr = "上海市";
    public static final String jiangshuStr = "江苏省";
    public static final String zhejiangStr = "浙江省";
    public static final String huangpuStr = "黄浦区";
    public static final String xuhuiStr = "徐汇区";
    public static final String changningStr = "长宁区";
    public static final String jinganStr = "静安区";
    public static final String putuoStr = "普陀区";
    public static final String hongkouStr = "虹口区";
    public static final String yangpuStr = "杨浦区";
    public static final String minghangStr = "闵行区";
    public static final String baoshanStr = "宝山区";
    public static final String jiadingStr = "嘉定区";
    public static final String pudongStr = "浦东新区";
    public static final String songjiangStr = "松江区";
    public static final String qingpuStr = "青浦区";
    public static final String chongmingStr = "崇明县";
    public static final String jinshanStr = "金山区";
    public static final String fengxianStr = "奉贤区";
    public static final String nanjingStr = "南京市";
    public static final String wuxiStr = "无锡市";
    public static final String suzhouStr = "苏州市";
    public static final String qidongStr = "启东";
    public static final String hangzhouStr = "杭州市";
    public static final String ningboStr = "宁波市";
    public static final String jiaxingStr = "嘉兴市";

    //省级数组
    public static final String[] AreaStr1 = {shanghaiStr, jiangshuStr, zhejiangStr};
    //市级数组
    public static final String[][] AreaStr2 = {{huangpuStr, xuhuiStr, changningStr,
            jinganStr, putuoStr, hongkouStr, yangpuStr, minghangStr, baoshanStr, jiadingStr, pudongStr, songjiangStr, qingpuStr, chongmingStr, jinshanStr, fengxianStr},
            {nanjingStr, wuxiStr, suzhouStr, qidongStr},
            {hangzhouStr, ningboStr, jiaxingStr}};
    //市级对应的区域ID
    public static final int[][] secondAreaId = {{65, 65, 5, 6, 6, 4, 5, 8, 8, 89, 9, 2, 1, 43, 435, 45}
    , {2, 23, 34, 43}, {23, 32, 80}};

    public static String[] array_pay_type_title = {"sdfs", "dsdf", "ds", "gdfs"};
    private static int[] array_pay_type_id = {1, 2, 3, 4};

    public static String[] array_front_money = {"80", "90"};// 定金金额
}
