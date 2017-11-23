package com.li.test.util;

import android.content.Context;

import com.li.test.bean.OrderStyleBean;

import java.util.ArrayList;

/**
 * 新增订单页面风格的数据请求及其解析
 */

public class HttpOrderStyleData {

    private Context context;
    private ArrayList<OrderStyleBean> styleList;//风格的数据源
    private StyleDataCallBack dataCallBack;

    public HttpOrderStyleData(Context context) {
        this.context = context;
        dataCallBack = (StyleDataCallBack) context;
        styleList = new ArrayList<>();
    }

    public void getStyleData(){
        try {
            //网络判断
            if (!Util.judgeInternet(context)) {
                return;
            }
//            String url = HttpUrl.appendUrl(HttpUrl.HTTP_GET_ORDER_STYLE);
//            LogUtil.v(context.getClass(), "getData---" + "url:" + url);
            for (int i = 0; i < 3; i++) {
                OrderStyleBean styleBean = new OrderStyleBean();
                styleBean.Id = 1;
                styleBean.StyleName ="欧美风格";
                styleList.add(styleBean);
            }
            dataCallBack.styleDataCallBack(styleList);
        } catch (Exception e) {
            LogUtil.e(context.getClass(), "getSytleData()", e);
        }
    }

    public interface StyleDataCallBack{
        void styleDataCallBack(ArrayList<OrderStyleBean> styleList);
    }
}
