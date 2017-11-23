package com.li.test.util;

import android.content.Context;

import com.li.test.bean.OrderStoreBean;

import java.util.ArrayList;

/**
 * 获取门店的请求公共类
 */

public class HttpOrderStoreData {
    private Context context;
    private ArrayList<OrderStoreBean> storeList;//门店的数据源
    private StoreDataCallBack storeCallBack;

    public HttpOrderStoreData(Context context) {
        this.context = context;
        storeList = new ArrayList<>();
        storeCallBack = (StoreDataCallBack) context;
    }

    public interface StoreDataCallBack {
        void storeDataCallBack(ArrayList<OrderStoreBean> storeList);
    }

    public void getStoreData(){
        try {
            //网络判断
            if (!Util.judgeInternet(context)) {
                return;
            }
//            String url = HttpUrl.appendUrl(HttpUrl.HTTP_GET_ORDER_STORE);
//            LogUtil.v(context.getClass(), "getData---" + "url:" + url);
            for (int i = 0; i < 3; i++) {
                OrderStoreBean storeBean = new OrderStoreBean();
                storeBean.Id = 1;
                storeBean.StoreName = "静安寺门店";
                storeBean.Address = "静安寺";
                storeBean.Phone = "15323562356";
                storeBean.Remark = "上海市";
                storeList.add(storeBean);
            }
            storeCallBack.storeDataCallBack(storeList);
        } catch (Exception e) {
            LogUtil.e(context.getClass(), "getStoreData()", e);
        }
    }
}
