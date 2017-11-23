package com.li.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.test.adapter.ReserveAdapter;
import com.li.test.bean.OrderInfo;
import com.li.test.util.HttpUrl;
import com.li.test.util.LogUtil;
import com.li.test.util.ToastManager;
import com.li.test.util.Util;
import com.li.test.view.xlistview.XListView;

import java.util.ArrayList;

/**
 * 订单列表页面
 */
public class MainActivity extends BaseActivity implements XListView.IXListViewListener{
    private int pageSize = 10;//每页条数
    private int pageIndex = 1;//当前页码
    private XListView reserve_listview;
    private ReserveAdapter reserveAdapter;
    private ArrayList<OrderInfo> list_data = new ArrayList<>();
    private ArrayList<OrderInfo> list_data_temp = new ArrayList<>();// 我的订单数据(暂时存储)
    private ImageButton addGoodsIg;
    private MyBroadcastReceiver receiver;// 刷新我的订单列表的广播
    private boolean isRefreshOrderList = false;// 是否是刷新数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeHeader();
        init();
        getData();
        registerRefreshBroadcast();
    }

    // 注册刷新我的订单的广播
    private void registerRefreshBroadcast(){
        try {
            receiver = new MyBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Util.REFRESH_MY_ORDER_LIST_ACTION);
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            LogUtil.e(getClass(), "registerRefreshBroadcast", e);
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Util.REFRESH_MY_ORDER_LIST_ACTION)) {
                pageIndex = 1;
                getData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);// 取消注册刷新我的订单列表广播
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.ADD_ORDER_REQUESTCODE && resultCode == Util.ADD_ORDER_RESULTCODE) {
            //从新增订单页面保存成功返回
            if (data != null) {
                OrderInfo orderInfo = (OrderInfo) data.getSerializableExtra("orderInfo");
                list_data_temp.add(orderInfo);
                list_data.add(0, orderInfo);
                if (list_data_temp != null && list_data_temp.size() < 10) {// 说明一页获取的数据不足10条
                    reserve_listview.setPullLoadEnable(false);// 不允许上拉加载数据,此时会自动隐藏底部view
                } else {
                    reserve_listview.setPullLoadEnable(true);
                }
                reserveAdapter.notifyDataSetChanged();
            }
        }
    }

    //网络请求获取数据
    private void getData() {
        try {
            //网络判断
            if (!Util.judgeInternet(this)) {
                return;
            }
            //请求数据
            String url = HttpUrl.appendUrl("dkl");
            //获取数据成功之后解析数据
            jsonParse("skjhd");
        } catch (Exception e) {
            LogUtil.e(getClass(), "getData()", e);
        }
    }

    //json解析
    private void jsonParse(String result) {
        try {
            if (!Util.isTextNull(result)) {
//                JSONObject jsonObject = new JSONObject(result);
                //这里是定死的数据,实际情况要以请求解析的数据
                Message msg = handler.obtainMessage();
                if (true) {
                    if (list_data_temp != null && list_data_temp.size() > 0) {
                        list_data_temp.clear();
                    }
                    for (int i = 0; i < 10; i++) {
                        OrderInfo ordersBean = new OrderInfo();
                        ordersBean.Id = 1;
                        ordersBean.UserName = "dkjs";
                        ordersBean.UserMobile = "15323562356";
                        ordersBean.OrderNo = "dj";
                        ordersBean.Status = 2;
                        ordersBean.StatusName = "ds";
                        ordersBean.CreateDate = "ds";
                        list_data_temp.add(ordersBean);
                    }
                    msg.what = Util.HTTP_MYORDER_SUCCESS;
                    handler.sendMessage(msg);
                }else {
                    msg.what = Util.HTTP_MYORDER_ERROR;
//                    msg.obj = jsonObject.optString("Msg");
                    handler.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "jsonParse()", e);
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Util.HTTP_MYORDER_SUCCESS://我的订单获取成功
                    if (list_data != null && list_data.size() > 0 && pageIndex == 1) {
                        list_data.clear();
                    }
                    list_data.addAll(list_data_temp);
                    if (list_data_temp != null && list_data_temp.size() < 10) {// 说明一页获取的数据不足10条
                        reserve_listview.setPullLoadEnable(false);// 不允许上拉加载数据,此时会自动隐藏底部view
                    } else {
                        reserve_listview.setPullLoadEnable(true);
                    }
                    reserveAdapter.notifyDataSetChanged();
                    break;
                case Util.HTTP_MYORDER_ERROR:
                    ToastManager.showToast(MainActivity.this, (String) msg.obj);
                    break;
            }
        }
    };

    private void init(){
        try {
            reserve_listview = (XListView) findViewById(R.id.reserve_listview);
            reserve_listview.setPullLoadEnable(true);// 上拉加载
            reserve_listview.setPullRefreshEnable(true);// 上拉刷新
            reserve_listview.setXListViewListener(this);// 添加监听
            reserveAdapter = new ReserveAdapter(this, list_data);
            reserve_listview.setAdapter(reserveAdapter);
            reserve_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OrderInfo orderInfo =(OrderInfo) adapterView.getAdapter().getItem(i);
                    Intent intent = new Intent(MainActivity.this, MyRevationDetailActivity.class);
                    intent.putExtra("orderInfo", orderInfo);
                    startActivity(intent);
                    Util.ActivitySkip(MainActivity.this);
                }
            });
            addGoodsIg = (ImageButton) findViewById(R.id.add_goods_ig);
            addGoodsIg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddOrderActivity.class);
                    startActivityForResult(intent, Util.ADD_ORDER_REQUESTCODE);
                    Util.ActivitySkip(MainActivity.this);
                }
            });
        } catch (Exception e) {
            LogUtil.e(getClass(), "initListView", e);
        }
    }

    // 更改头部
    private void changeHeader() {
        try {
            View headView = findViewById(R.id.include_head);
            ImageView head_back = (ImageView) headView.findViewById(R.id.head_back);
            head_back.setVisibility(View.GONE);
            TextView tv_head_right = (TextView) headView.findViewById(R.id.tv_head_right);
            tv_head_right.setVisibility(View.GONE);
            TextView head_title = (TextView) headView.findViewById(R.id.tv_title);
            head_title.setText(this.getResources().getString(R.string.myorder_tv));
        } catch (Exception e) {
            LogUtil.e(getClass(), "changeHeader()", e);
        }
    }

    @Override
    public String getActivitySimpleName() {
        return MainActivity.class.getName();
    }

    // 上拉刷新
    @Override
    public void onRefresh() {
        pageIndex = 1;
        isRefreshOrderList = true;
        getData();
        onDataLoaded();
    }

    // 下拉加载
    @Override
    public void onLoadMore() {
        pageIndex ++;
        getData();
    }

    // 加载数据完成后执行
    private void onDataLoaded() {
        if (isRefreshOrderList) {
            ToastManager.showToast(this, getString(R.string.refresh_success));
            isRefreshOrderList = false;
        }
        reserve_listview.stopRefresh();
        reserve_listview.stopLoadMore();
    }
}
