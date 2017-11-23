package com.li.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.test.bean.OrderInfo;
import com.li.test.util.LogUtil;
import com.li.test.util.Util;
import com.li.test.view.MyScrollView;


/**
 * 订单详情页面
 */

public class MyRevationDetailActivity extends BaseActivity implements View.OnClickListener {
    //姓名,电话,面积,房型,风格,卫生间数量,区域,地址,特殊要求
    private TextView detailName, detailPhone, detailArea, house_type_tv, detailstyle, detailZone, detailAdress, detailSpecial;
    private MyScrollView scrollView;
    private TextView order_number_tv;//订单编号
    private ImageView call_phone_img;//打电话的按钮
    private OrderInfo orderInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_revation_detail_activity);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        changeHeader();
        init();
        getDetailData();

    }

    //标题部分初始化
    private void changeHeader() {
        try {
            //标题部分初始化
            View view = findViewById(R.id.redetail_title_layout);
            ImageView titleBack = (ImageView) view.findViewById(R.id.head_back);
            titleBack.setOnClickListener(this);
            TextView titleCenter = (TextView) view.findViewById(R.id.tv_title);
            titleCenter.setText(getResources().getString(R.string.revation_order_tv));
            TextView titleRight = (TextView) view.findViewById(R.id.tv_head_right);
            titleRight.setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtil.e(getClass(), "changeHeader()", e);
        }
    }

    private void getDetailData() {
        try {
            //网络判断
            if (!Util.judgeInternet(MyRevationDetailActivity.this)) {
                return;
            }
//            String url = HttpUrl.appendUrl(HttpUrl.HTTP_GET_ORDER_DETAIL);
//            LogUtil.v(getClass(), "getDetailData---" + "url:" + url);
            if (orderInfo != null) {
                initData();
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "getDetailData()", e);
        }
    }

    //在请求成功之后,初始化控件数据
    private void initData() {
        try {
            detailName.setText(orderInfo.UserName);//用户名称
            detailPhone.setText(orderInfo.UserMobile);//用户手机号
            detailArea.setText("100平米");//面积
            house_type_tv.setText(5 + getResources().getString(R.string.house_type1_room) +
                    2 + getResources().getString(R.string.house_type2_hall) +
                    2 + getResources().getString(R.string.house_type3_kitchen) +
                    1 + getResources().getString(R.string.house_type4_toilet) +
                    1 + getResources().getString(R.string.house_type5_balcony));//房型
            detailstyle.setText("欧美风格");//风格
            detailZone.setText("上海市静安区");//区域
            detailAdress.setText("上海市静安区");//地址
            detailSpecial.setText("无");//特殊要求
            order_number_tv.setText("123466");
        } catch (Exception e) {
            LogUtil.e(getClass(), "initData()", e);
        }
    }

    private void init() {
        //内容初始化
        scrollView = (MyScrollView) findViewById(R.id.detail_scrollview);
        scrollView.setIsCanZoom(false);//禁止下拉滑动
        detailName = (TextView) findViewById(R.id.detail_name_tv);
        detailPhone = (TextView) findViewById(R.id.detail_phone_tv);
        detailArea = (TextView) findViewById(R.id.detail_area_tv);
        house_type_tv = (TextView) findViewById(R.id.house_type_tv);
        detailstyle = (TextView) findViewById(R.id.detail_style_tv);
        detailZone = (TextView) findViewById(R.id.detail_zone_tv);
        detailAdress = (TextView) findViewById(R.id.detail_adress_tv);
        detailSpecial = (TextView) findViewById(R.id.detail_special_tv);
        order_number_tv = (TextView) findViewById(R.id.order_number_tv);
        call_phone_img = (ImageView) findViewById(R.id.call_phone_img);
        call_phone_img.setOnClickListener(this);
    }

    @Override
    public String getActivitySimpleName() {
        return MyRevationDetailActivity.class.getName();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                MyRevationDetailActivity.this.finish();
                Util.ActivityExit(MyRevationDetailActivity.this);
                break;
            case R.id.call_phone_img://打电话的按钮
                Util.callPhone(this, detailPhone.getText().toString());
                break;
        }
    }

}
