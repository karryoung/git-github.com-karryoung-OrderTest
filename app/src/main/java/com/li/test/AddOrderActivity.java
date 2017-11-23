package com.li.test;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.test.bean.OrderInfo;
import com.li.test.bean.OrderStoreBean;
import com.li.test.bean.OrderStyleBean;
import com.li.test.util.AbsTextUtil;
import com.li.test.util.HttpOrderStoreData;
import com.li.test.util.HttpOrderStyleData;
import com.li.test.util.HttpUrl;
import com.li.test.util.LogUtil;
import com.li.test.util.PickerUtil;
import com.li.test.util.ToastManager;
import com.li.test.util.Util;
import com.li.test.view.MyScrollView;
import com.li.test.view.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 新增订单页面
 */

public class AddOrderActivity extends BaseActivity implements HttpOrderStyleData.StyleDataCallBack, HttpOrderStoreData.StoreDataCallBack {
    private MyScrollView myscrollview;//整体ScrollView的布局
    private EditText add_order_name_et, add_order_phone_et, add_order_area_et, add_adress_edit, add_demand_edit;//姓名, 电话,产证面积,地址,特殊要求
    private LinearLayout referralLayout;//推荐人整体的布局
    private EditText referralNameEt, referralPhoneEt;//推荐人姓名的输入框, 推荐人电话的输入框
    private TextView type_choose_tv;//最终选择的风格
    private HttpOrderStyleData orderStyleData;//风格的请求类
    private HttpOrderStoreData orderStoreData;//门店的请求类
    private TextView store_choose_tv;//最终选择的门店
    private TextView house_type_choose_tv;//最终选择的房型
    private TextView zone_choose_tv;//最终选择的区域
    private String Room = "0";//最终选择的卧室数
    private String Hall = "0";//最终选择的厅数
    private String Kitchen = "0";//最终选择的厨房数
    private String Toilet = "0";//最终选择的卫生间数
    private String Balcony = "0";//最终选择的阳台数
    private int AreaId = -1;//最终选择的区域ID
    private int orderStyleId;// 风格ID
    private int storeId;// 门店ID
    private int orderId = -1;// 订单ID

    private SwitchCompat referral_switch;// 是否有推荐人按钮
    private TextView head_title;// 页面标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order_activity);
        changeHeader();
        init();
    }

    //初始化信息
    private void init() {
        try {
            orderStoreData = new HttpOrderStoreData(AddOrderActivity.this);
            orderStyleData = new HttpOrderStyleData(AddOrderActivity.this);
            myscrollview = (MyScrollView) findViewById(R.id.add_order_scrollview);
            myscrollview.setIsCanZoom(false);
            //姓名
            View add_order_name_layout = findViewById(R.id.add_order_name_layout);
            TextView add_order_name_tv = (TextView) add_order_name_layout.findViewById(R.id.layout_title);
            add_order_name_tv.setText(Util.changStr(getResources().getString(R.string.add_order_name_tv)
                    , Util.Dp2Px(this, 12), Color.argb(255, 71, 71, 71), 2));
            add_order_name_et = (EditText) add_order_name_layout.findViewById(R.id.layout_value);
            add_order_name_et.setHint(getResources().getString(R.string.add_order_name_hint));
            add_order_name_et.setInputType(InputType.TYPE_CLASS_TEXT);
            //电话
            View add_order_phone_layout = findViewById(R.id.add_order_phone_layout);
            TextView add_order_phone_tv = (TextView) add_order_phone_layout.findViewById(R.id.layout_title);
            add_order_phone_tv.setText(Util.changStr(getResources().getString(R.string.add_order_phone_tv)
                    , Util.Dp2Px(this, 12), Color.argb(255, 71, 71, 71), 2));
            add_order_phone_et = (EditText) add_order_phone_layout.findViewById(R.id.layout_value);
            add_order_phone_et.setHint(getResources().getString(R.string.add_order_phone_hint));
            add_order_phone_et.setInputType(InputType.TYPE_CLASS_NUMBER);
            //产证面积
            TextView house_area_tv = (TextView) findViewById(R.id.house_area_tv);
            house_area_tv.setText(Util.changStr(getResources().getString(R.string.house_area_tv)
                    , Util.Dp2Px(this, 12), Color.argb(255, 71, 71, 71), 4));
            add_order_area_et = (EditText) findViewById(R.id.add_order_area_et);
            Util.setEditTextChangeListen(add_order_area_et);
            //门店
            View add_order_store = findViewById(R.id.add_order_store);
            TextView store_tv = (TextView) add_order_store.findViewById(R.id.tv_main_title);
            store_tv.setText(getResources().getString(R.string.store_tv));
            store_choose_tv = (TextView) add_order_store.findViewById(R.id.tv_default_title);
            store_choose_tv.setText(getResources().getString(R.string.strings_please_choose));
            store_choose_tv.setTextColor(ContextCompat.getColor(this, R.color.material_view_color));
            RippleView order_store_rippleView = (RippleView) add_order_store.findViewById(R.id.update_number_layout);
            order_store_rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderStoreData.getStoreData();
                }
            });
            //房型
            View add_order_house_type = findViewById(R.id.add_order_house_type);
            TextView house_type_tv = (TextView) add_order_house_type.findViewById(R.id.tv_main_title);
            house_type_tv.setText(getResources().getString(R.string.design_agreement_strings_house_type));
            house_type_choose_tv = (TextView) add_order_house_type.findViewById(R.id.tv_default_title);
            house_type_choose_tv.setText(getResources().getString(R.string.strings_please_choose));
            house_type_choose_tv.setTextColor(ContextCompat.getColor(this, R.color.material_view_color));
            RippleView house_type_rippleView = (RippleView) add_order_house_type.findViewById(R.id.update_number_layout);
            house_type_rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] array = {"1","2","3","4","5","6","7","8","9","10"};
                    PickerUtil pickerUtil = new PickerUtil(AddOrderActivity.this);
                    pickerUtil.showFiveChooseDialog(AddOrderActivity.this, getResources().getString(R.string.choose_house_type), array);
                    pickerUtil.setCallBack(new PickerUtil.HouseTypeCallBack() {
                        @Override
                        public void houseTySuccess(String result, String room, String hall, String kitchen, String toilet, String balcony) {
                            if (!Util.isTextNull(result)) {
                                house_type_choose_tv.setText(result);
                                house_type_choose_tv.setTextColor(ContextCompat.getColor(AddOrderActivity.this, R.color.combo_select_text_color));
                                Room = room;
                                Hall = hall;
                                Kitchen = kitchen;
                                Toilet = toilet;
                                Balcony = balcony;
                            }
                        }
                    });
                }
            });
            //风格
            View add_order_type = findViewById(R.id.add_order_type);
            TextView type_tv = (TextView) add_order_type.findViewById(R.id.tv_main_title);
            type_tv.setText(getResources().getString(R.string.evaluate_strings_style_title));
            type_choose_tv = (TextView) add_order_type.findViewById(R.id.tv_default_title);
            type_choose_tv.setText(getResources().getString(R.string.strings_please_choose));
            type_choose_tv.setTextColor(ContextCompat.getColor(this, R.color.material_view_color));
            RippleView order_type_rippleView = (RippleView) add_order_type.findViewById(R.id.update_number_layout);
            order_type_rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderStyleData.getStyleData();
                }
            });
            //区域
            View add_order_zone = findViewById(R.id.add_order_zone);
            TextView zone_tv = (TextView) add_order_zone.findViewById(R.id.tv_main_title);
            zone_tv.setText(getResources().getString(R.string.design_agreement_strings_locate_area));
            zone_choose_tv = (TextView) add_order_zone.findViewById(R.id.tv_default_title);
            zone_choose_tv.setText(getResources().getString(R.string.strings_please_choose));
            zone_choose_tv.setTextColor(ContextCompat.getColor(this, R.color.material_view_color));
            RippleView order_zone_rippleView = (RippleView) add_order_zone.findViewById(R.id.update_number_layout);
            order_zone_rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickerUtil pickerUtil2 = new PickerUtil(AddOrderActivity.this);
                    pickerUtil2.showTwoChooseDialog(getResources().getString(R.string.chouse_area), AbsTextUtil.AreaStr1, AbsTextUtil.AreaStr2);
                    pickerUtil2.setCallBack(new PickerUtil.AreaCallBack() {
                        @Override
                        public void areaSuccess(String result, int areaId) {
                            if (!Util.isTextNull(result)) {
                                zone_choose_tv.setText(result);
                                zone_choose_tv.setTextColor(ContextCompat.getColor(AddOrderActivity.this, R.color.combo_select_text_color));
                                AreaId = areaId;
                            }
                        }
                    });
                }
            });

            //地址
            add_adress_edit = (EditText) findViewById(R.id.add_adress_edit);
            referral_switch = (SwitchCompat) findViewById(R.id.referral_switch);
            //推荐人整体的布局
            referralLayout = (LinearLayout) findViewById(R.id.referral_layout);
            //推荐人姓名的整体布局
            View referral_name_layout = findViewById(R.id.referral_name_layout);
            //推荐人姓名的TextView
            TextView referral_name_tv = (TextView) referral_name_layout.findViewById(R.id.layout_title);
            referral_name_tv.setText(getResources().getString(R.string.referral_name_tv));;
            referralNameEt = (EditText) referral_name_layout.findViewById(R.id.layout_value);
            referralNameEt.setHint(getResources().getString(R.string.input_search_name_tv));
            referralNameEt.setInputType(InputType.TYPE_CLASS_TEXT);
            //推荐人电话的整体布局
            View referral_phone_layout = findViewById(R.id.referral_phone_layout);
            //推荐人电话的TextView
            TextView referral_phone_tv = (TextView) referral_phone_layout.findViewById(R.id.layout_title);
            referral_phone_tv.setText(Util.changStr(getResources().getString(R.string.referral_phone_tv), Util.Dp2Px(this, 12), Color.argb(255, 71, 71, 71), 5));
            referralPhoneEt = (EditText) referral_phone_layout.findViewById(R.id.layout_value);
            referralPhoneEt.setHint(getResources().getString(R.string.input_phone_hint));
            referralPhoneEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            //特殊要求
            add_demand_edit = (EditText) findViewById(R.id.add_demand_edit);

            referralLayout.setVisibility(View.GONE);
            referral_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (referral_switch.isChecked()) {
                        referralLayout.setVisibility(View.VISIBLE);
                    } else {
                        referralLayout.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            LogUtil.e(getClass(), "init()", e);
        }

    }

    //更改头部信息
    private void changeHeader() {
        try {
            View headView = findViewById(R.id.add_order_head);
            ImageView head_back = (ImageView) headView.findViewById(R.id.head_back);
            head_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.closeSoftKey(AddOrderActivity.this);
                    AddOrderActivity.this.finish();
                    Util.ActivityExit(AddOrderActivity.this);
                }
            });
            TextView tv_head_right = (TextView) headView.findViewById(R.id.tv_head_right);
            tv_head_right.setVisibility(View.VISIBLE);
            tv_head_right.setText(getResources().getString(R.string.order_save_tv));
            tv_head_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!judgeConform()) {
                        return;
                    }
                    PickerUtil pickerUtil = new PickerUtil(AddOrderActivity.this);
                    pickerUtil.showTipDialog(AddOrderActivity.this, getResources().getString(R.string.ensure_add_order));
                    pickerUtil.setCallBack(new PickerUtil.pressBtCallBack() {

                        @Override
                        public void pressSuccess(boolean isPressYes) {
                            if (!isPressYes) {
                                return;
                            }
                            //点击保存
                            saveOrderData();
                        }
                    });
                }
            });
            head_title = (TextView) headView.findViewById(R.id.tv_title);
            head_title.setText(this.getResources().getString(R.string.order_title_tv));
        } catch (Exception e) {
            LogUtil.e(getClass(), "changeHeader()", e);
        }
    }

    //点击保存按钮之后运行的程序
    private void saveOrderData(){
        try {
//            String url = "";
//            if (orderId != -1) {// 代表订单ID不为空，说明是编辑订单
//                url = HttpUrl.appendUrl(HttpUrl.HTTP_POST_ORDER_EDI);
//            } else {
//                url = HttpUrl.appendUrl(HttpUrl.HTTP_POST_ADD_ORDER);
//            }
//            LogUtil.v(getClass(), "saveOrderData---" + "url:" + url);
//            if (loadingDialog != null) {
//                loadingDialog.show();
//            }
//            ContentValues listParmas = new ContentValues();
//            listParmas.put("OperateId", Util.getUserIdFromSharedPreferce(this) + "");//操作人ID
//            listParmas.put("AreaId", AreaId+"");//区域ID
//            listParmas.put("StoreId", storeId + "");//门店ID
//            listParmas.put("UserName", add_order_name_et.getText().toString());//用户名称
//            listParmas.put("UserMobile", add_order_phone_et.getText().toString());//用户手机号
//            listParmas.put("HouseSize", Util.isTextNull(add_order_area_et.getText().toString()) ? "0" :
//                    add_order_area_et.getText().toString());//产证面积
//            listParmas.put("Room", Room);//室
//            listParmas.put("Hall", Hall);//厅
//            listParmas.put("Kitchen", Kitchen);//厨
//            listParmas.put("Toilet", Toilet);//卫
//            listParmas.put("Balcony", Balcony);//阳台
//            listParmas.put("StyleId", orderStyleId + "");//风格Id
//            listParmas.put("Address", add_adress_edit.getText().toString());//地址
//            listParmas.put("ReferalName", (referralLayout.getVisibility() == View.VISIBLE)? referralNameEt.getText().toString():"");//推荐人名称
//            listParmas.put("ReferalMobile", (referralLayout.getVisibility() == View.VISIBLE)? referralPhoneEt.getText().toString():"");//推荐人手机号
//            listParmas.put("Remark", add_demand_edit.getText().toString());//备注
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.Id = 1;
            orderInfo.UserName = add_order_name_et.getText().toString();
            orderInfo.UserMobile = add_order_phone_et.getText().toString();
            orderInfo.OrderNo = "123456";
            orderInfo.Status = 1;
            orderInfo.StatusName = "未下定";
            orderInfo.CreateDate = Util.getTime();
            Intent intent = new Intent(AddOrderActivity.this, MainActivity.class);
            intent.putExtra("orderInfo", orderInfo);
            setResult(Util.ADD_ORDER_RESULTCODE, intent);
            AddOrderActivity.this.finish();
            Util.ActivityExit(AddOrderActivity.this);
            ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.add_order_success));
        } catch (Exception e) {
            LogUtil.e(getClass(), "saveOrderData()", e);
        }
    }

    //判断输入的内容是否符合要求
    private boolean judgeConform() {
        try {
            //姓名判断
            if (Util.isTextNull(add_order_name_et.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_name_tv));
                return false;
            }
            //电话判断
            if (!Util.isMobileNO2(add_order_phone_et.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_correct_phone_tv));
                return false;
            }
            //产证面积判断
            if (Util.isTextNull(add_order_area_et.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_area_tv));
                return false;
            }
            //房型判断
            if (!judgeChoose(house_type_choose_tv.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_choose_house_type_tv));
                return false;
            }
            //风格判断
            if (!judgeChoose(type_choose_tv.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_choose_type_tv));
                return false;
            }
            //区域判断
            if (!judgeChoose(zone_choose_tv.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_choose_zone_tv));
                return false;
            }
            //地址判断
            if (Util.isTextNull(add_adress_edit.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_adress_tv));
                return false;
            }
            //推荐人信息判断
            if (referralLayout.getVisibility() == View.VISIBLE) {
                if (Util.isTextNull(referralNameEt.getText().toString())) {
                    ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_referralname_tv));
                    return false;
                }
                if (!Util.isMobileNO2(referralPhoneEt.getText().toString())) {
                    ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_input_referralphone_tv));
                    return false;
                }
            }
            //门店判断
            if (!judgeChoose(store_choose_tv.getText().toString())) {
                ToastManager.showToast(AddOrderActivity.this, getResources().getString(R.string.please_choose_store_tv));
                return false;
            }
            //网络判断
            if (!Util.judgeInternet(AddOrderActivity.this)) {
                return false;
            }
        } catch (Resources.NotFoundException e) {
            LogUtil.e(getClass(), "judgeConform()", e);
        }
        return true;
    }

    //判断门店, 房型, 风格是否选择了
    private boolean judgeChoose(String et){
        try {
            if (et.equals(getResources().getString(R.string.strings_please_choose))) {
                return false;
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "judgeChoose()", e);
        }
        return true;
    }

    @Override
    public String getActivitySimpleName() {
        return AddOrderActivity.class.getName();
    }

    @Override
    public void styleDataCallBack(final ArrayList<OrderStyleBean> styleList) {
        //风格请求返回的数据
        if (styleList != null && styleList.size() > 0) {
            String[] array = new String[styleList.size()];
            for (int i = 0; i < styleList.size(); i++) {
                array[i] = styleList.get(i).StyleName;
            }
            PickerUtil pickerUtil = new PickerUtil(AddOrderActivity.this);
            pickerUtil.showSingleChooseDialog(getResources().getString(R.string.style_str), array);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    if (!Util.isTextNull(result)) {
                        type_choose_tv.setText(result);
                        type_choose_tv.setTextColor(ContextCompat.getColor(AddOrderActivity.this, R.color.combo_select_text_color));
                        if (styleList != null && styleList.size() > 0) {
                            for (int i = 0; i < styleList.size(); i++) {
                                if (styleList.get(i).StyleName.equals(result)) {
                                    orderStyleId = styleList.get(i).Id;
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void storeDataCallBack(final ArrayList<OrderStoreBean> storeList) {
        //门店请求返回的数据
        if (storeList != null && storeList.size() > 0) {
            String[] array = new String[storeList.size()];
            for (int i = 0; i < storeList.size(); i++) {
                array[i] = storeList.get(i).StoreName;
            }
            PickerUtil pickerUtil = new PickerUtil(AddOrderActivity.this);
            pickerUtil.showSingleChooseDialog(getResources().getString(R.string.store_tv), array);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    if (!Util.isTextNull(result)) {
                        store_choose_tv.setText(result);
                        store_choose_tv.setTextColor(ContextCompat.getColor(AddOrderActivity.this, R.color.combo_select_text_color));
                        if (storeList != null && storeList.size() > 0) {
                            for (int i = 0; i < storeList.size(); i++) {
                                if (storeList.get(i).StoreName.equals(result)) {
                                    storeId = storeList.get(i).Id;
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
