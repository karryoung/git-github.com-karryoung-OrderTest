package com.li.test.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.li.test.R;
import com.li.test.view.timePicker.ArrayWheelAdapter;
import com.li.test.view.timePicker.OnWheelChangedListener;
import com.li.test.view.timePicker.WheelView;

import java.util.Calendar;

public class PickerUtil {

    private Context context;
    private LayoutInflater inflater;
    private loadDataCallBack callBack;
    private pressBtCallBack pressCallBack;
    private HouseTypeCallBack houseTypeCallBack;
    private AreaCallBack areaCallBack;
    private Dialog dialog = null;
    // 滚轮上的数据，字符串数组
    String[] yearArrayString = null;
    String[] dayArrayString = null;
    String[] monthArrayString = null;
    String[] hourArrayString = null;
    Calendar c = null;
    private WheelView choice_wv_layout1;// 选择控件1,年
    private WheelView choice_wv_layout2;// 选择控件2,月
    private WheelView choice_wv_layout3;// 选择控件3,日
    private WheelView choice_wv_layout4;// 选择控件4,时
    int year;
    int month;

    public PickerUtil(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**@title 展示单选框
     * @params 
     * @return type 
     **/
    public void showSingleChooseDialog(String textTile, final String[] arrayString) {
        try {
            View v = inflater.inflate(R.layout.activity_single_choice, null);
            final TextView tv_single_choice_title = (TextView) v.findViewById(R.id.single_choice_title);// 标题
            final WheelView wv_single_choice_wv_layout = (WheelView) v.findViewById(R.id.single_choice_wv_layout);// 选择控件
            final TextView wv_single_choice_bt = (TextView) v.findViewById(R.id.single_choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
            //wv.setLabel("年");// 设置滚轮的标签
            tv_single_choice_title.setText(textTile);

            wv_single_choice_wv_layout.setAdapter(new ArrayWheelAdapter<String>(arrayString));
            wv_single_choice_wv_layout.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    // tv.setText(arrayString[wv.getCurrentItem()]);
                    // callBack.loadDataSuccess(arrayString[wv_single_choice_wv_layout.getCurrentItem()]);
                }
            });
            wv_single_choice_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.loadDataSuccess(arrayString[wv_single_choice_wv_layout.getCurrentItem()]);
                    dismissChooseDialog();
                }
            });
            getChooseDialog(context, v);
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showSingleChooseDialog", e);
        }
    }

    /**@title 双选框
     * @params 
     * @return type 
     **/
    public void showTwoChooseDialog(String textTile, final String[] arrayString1, final String[][] arrayString2) {
        try {
            View v = inflater.inflate(R.layout.activity_double_choice, null);
            final TextView tv_title = (TextView) v.findViewById(R.id.choice_title);// 标题
            final WheelView choice_wv_layout1 = (WheelView) v.findViewById(R.id.choice_wv_layout1);// 选择控件1
            final WheelView choice_wv_layout2 = (WheelView) v.findViewById(R.id.choice_wv_layout2);// 选择控件2
            final TextView choice_bt = (TextView) v.findViewById(R.id.choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
            // wv.setLabel("年");// 设置滚轮的标签
            tv_title.setText(textTile);

            choice_wv_layout1.setAdapter(new ArrayWheelAdapter<String>(arrayString1));// 默认显示数组中的第一个集合
            choice_wv_layout1.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    choice_wv_layout2.setAdapter(new ArrayWheelAdapter<String>(arrayString2[choice_wv_layout1.getCurrentItem()]));// 设置第二个选项里的内容
                    if (choice_wv_layout2.getCurrentItem() > arrayString2[choice_wv_layout1.getCurrentItem()].length) {// 如果第二项选择框选中的position大于内容的长度，设置显示最后一个文字(切换第一项选择框会导致这个问题)
                        choice_wv_layout2.setCurrentItem(arrayString2[choice_wv_layout1.getCurrentItem()].length - 1);
                    }
                }
            });
            choice_wv_layout2.setAdapter(new ArrayWheelAdapter<String>(arrayString2[0]));
            choice_wv_layout2.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                }
            });
            choice_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    areaCallBack.areaSuccess(arrayString1[choice_wv_layout1.getCurrentItem()] + arrayString2[choice_wv_layout1.getCurrentItem()][choice_wv_layout2.getCurrentItem()],
                            AbsTextUtil.secondAreaId[choice_wv_layout1.getCurrentItem()][choice_wv_layout2.getCurrentItem()]);
                    dismissChooseDialog();
                }
            });
            getChooseDialog(context, v);
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showThreeChooseDialog", e);
        }
    }

    /**@title 显示三选或四选框
     * @params 
     * @return type 
     **/
    public void showThreeChooseDialog(String textTile, final boolean isShowHour) {
        try {
            // 获取当前系统时间
            c = Calendar.getInstance();
            View v = null;
            if (isShowHour) {
                v = inflater.inflate(R.layout.activity_four_choice, null);
                choice_wv_layout4 = (WheelView) v.findViewById(R.id.choice_wv_layout4);// 选择控件4
                choice_wv_layout4.setLabel("时");// 设置滚轮的标签
            } else {
                v = inflater.inflate(R.layout.activity_three_choice, null);
            }

            final TextView tv_title = (TextView) v.findViewById(R.id.choice_title);// 标题
            tv_title.setText(textTile);
            choice_wv_layout1 = (WheelView) v.findViewById(R.id.choice_wv_layout1);// 选择控件1
            choice_wv_layout2 = (WheelView) v.findViewById(R.id.choice_wv_layout2);// 选择控件2
            choice_wv_layout3 = (WheelView) v.findViewById(R.id.choice_wv_layout3);// 选择控件3
            final TextView choice_bt = (TextView) v.findViewById(R.id.choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
            choice_wv_layout1.setLabel("年");// 设置滚轮的标签
            choice_wv_layout2.setLabel("月");// 设置滚轮的标签
            choice_wv_layout3.setLabel("日");// 设置滚轮的标签

            // 得到相应的数组
            yearArrayString = getYEARArray(2010, 20);
            monthArrayString = getDayArray(12);
            dayArrayString = getDayArray(30);
            hourArrayString = getDayArray(24);

            choice_wv_layout1.setAdapter(new ArrayWheelAdapter<String>(yearArrayString));// 添加年数据
            choice_wv_layout2.setAdapter(new ArrayWheelAdapter<String>(monthArrayString));// 添加月数据
            choice_wv_layout3.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));// 添加日数据
            if (choice_wv_layout4 != null) {
                choice_wv_layout4.setAdapter(new ArrayWheelAdapter<String>(hourArrayString));// 添加时数据
            }
            choice_wv_layout1.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    // Auto-generated method stub
                    // 获取年和月
                    year = Integer.parseInt(yearArrayString[choice_wv_layout1.getCurrentItem()]);
                    month = Integer.parseInt(monthArrayString[choice_wv_layout2
                            .getCurrentItem()]);
                    // 根据年和月生成天数数组
                    dayArrayString = getDayArray(getDay(year, month));
                    // 给天数的滚轮设置数据
                    choice_wv_layout3.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
                    // 防止数组越界
                    if (choice_wv_layout3.getCurrentItem() >= dayArrayString.length) {
                        choice_wv_layout3.setCurrentItem(dayArrayString.length - 1);
                    }
                }
            });

            // 当月变化时显示的时间
            choice_wv_layout2.addChangingListener(new OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    // Auto-generated method stub
                    // 获取年和月
                    year = Integer.parseInt(yearArrayString[choice_wv_layout1.getCurrentItem()]);
                    month = Integer.parseInt(monthArrayString[choice_wv_layout2
                            .getCurrentItem()]);
                    // 根据年和月生成天数数组
                    dayArrayString = getDayArray(getDay(year, month));
                    // 给天数的滚轮设置数据
                    choice_wv_layout3.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
                    // 防止数组越界
                    if (choice_wv_layout3.getCurrentItem() >= dayArrayString.length) {
                        choice_wv_layout3.setCurrentItem(dayArrayString.length - 1);
                    }
                }
            });
            // 把当前系统时间显示为滚轮默认时间
            setDefTime();

            choice_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDate(isShowHour);
                    dismissChooseDialog();
                }
            });
            getChooseDialog(context, v);
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showThreeChooseDialog", e);
        }
    }

    /**@title 展示五选框
     * @params 
     * @return type 
     **/
    public void showFiveChooseDialog(final Context context, String textTile, final String[] arrayContent) {
        try {
            View v = inflater.inflate(R.layout.activity_five_choice, null);

            final TextView tv_title = (TextView) v.findViewById(R.id.choice_title);// 标题
            tv_title.setText(textTile);
            final WheelView choice_wv_layout1 = (WheelView) v.findViewById(R.id.choice_wv_layout1);// 选择控件1
            final WheelView choice_wv_layout2 = (WheelView) v.findViewById(R.id.choice_wv_layout2);// 选择控件2
            final WheelView choice_wv_layout3 = (WheelView) v.findViewById(R.id.choice_wv_layout3);// 选择控件3
            final WheelView choice_wv_layout4 = (WheelView) v.findViewById(R.id.choice_wv_layout4);// 选择控件4
            final WheelView choice_wv_layout5 = (WheelView) v.findViewById(R.id.choice_wv_layout5);// 选择控件5
            final TextView choice_bt = (TextView) v.findViewById(R.id.choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
            choice_wv_layout1.setLabel("房间");
            choice_wv_layout2.setLabel("客厅");
            choice_wv_layout3.setLabel("厨房");
            choice_wv_layout4.setLabel("卫生间");
            choice_wv_layout5.setLabel("阳台");

            choice_wv_layout1.setAdapter(new ArrayWheelAdapter<String>(arrayContent));
            choice_wv_layout2.setAdapter(new ArrayWheelAdapter<String>(arrayContent));
            choice_wv_layout3.setAdapter(new ArrayWheelAdapter<String>(arrayContent));
            choice_wv_layout4.setAdapter(new ArrayWheelAdapter<String>(arrayContent));
            choice_wv_layout5.setAdapter(new ArrayWheelAdapter<String>(arrayContent));

            choice_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    houseTypeCallBack.houseTySuccess(arrayContent[choice_wv_layout1.getCurrentItem()] + context.getString(R.string.house_type1_room) +
                            arrayContent[choice_wv_layout2.getCurrentItem()] + context.getString(R.string.house_type2_hall) +
                            arrayContent[choice_wv_layout3.getCurrentItem()] + context.getString(R.string.house_type3_kitchen) +
                            arrayContent[choice_wv_layout4.getCurrentItem()] + context.getString(R.string.house_type4_toilet) +
                            arrayContent[choice_wv_layout5.getCurrentItem()] + context.getString(R.string.house_type5_balcony),
                            arrayContent[choice_wv_layout1.getCurrentItem()],
                            arrayContent[choice_wv_layout2.getCurrentItem()],
                            arrayContent[choice_wv_layout3.getCurrentItem()],
                            arrayContent[choice_wv_layout4.getCurrentItem()],
                            arrayContent[choice_wv_layout5.getCurrentItem()]);
                    dismissChooseDialog();
                }
            });
            getChooseDialog(context, v);
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showFiveChooseDialog", e);
        }
    }

    /**@title 显示提示的dialog
     * @params 
     * @return type 
     **/
    public void showTipDialog(Context context, String textTile) {
        try {
            final AlertDialog dialog = new AlertDialog.Builder(
                    context, R.style.myTipDialogStyle).create();

            View v = inflater.inflate(R.layout.activity_tip_dialog, null);

            final TextView tip_title = (TextView) v.findViewById(R.id.tip_title);// 标题
            tip_title.setText(textTile);
            final TextView tip_yes = (TextView) v.findViewById(R.id.tip_yes);// 确定按钮
            final TextView tip_no = (TextView) v.findViewById(R.id.tip_no);// 返回按钮

            tip_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCallBack.pressSuccess(true);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            tip_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCallBack.pressSuccess(false);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.setView(v);
            dialog.show();;
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showTipDialog", e);
        }
    }
    /**@title 显示更多操作提示的dialog
     * @params
     * @return type
     **/
    public void showMoreFunctionDialog(Context context) {
        try {
            final AlertDialog dialog = new AlertDialog.Builder(
                    context, R.style.myTipDialogStyle).create();

            View v = inflater.inflate(R.layout.activity_tip_more_function_dialog, null);

            final TextView tip_del = (TextView) v.findViewById(R.id.tip_del);// 删除按钮
            final TextView tip_cancel = (TextView) v.findViewById(R.id.tip_cancel);// 返回按钮

            tip_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCallBack.pressSuccess(true);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            tip_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCallBack.pressSuccess(false);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.setView(v);
            dialog.show();;
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "showMoreFunctionDialog", e);
        }
    }

    // 显示时间
    void showDate(boolean isShowHour) {
        if (isShowHour) {
            createDateWithHour(yearArrayString[choice_wv_layout1.getCurrentItem()],
                    monthArrayString[choice_wv_layout2.getCurrentItem()],
                    dayArrayString[choice_wv_layout3.getCurrentItem()], hourArrayString[choice_wv_layout4.getCurrentItem()]);
        } else {
            createDate(yearArrayString[choice_wv_layout1.getCurrentItem()],
                    monthArrayString[choice_wv_layout2.getCurrentItem()],
                    dayArrayString[choice_wv_layout3.getCurrentItem()]);
        }
    }

    // 设定初始时间
    void setDefTime() {
        if (choice_wv_layout1 != null) {
            choice_wv_layout1.setCurrentItem(getNumData(c.get(Calendar.YEAR) + "",
                    yearArrayString));
        }
        if (choice_wv_layout2 != null) {
            choice_wv_layout2.setCurrentItem(getNumData(c.get(Calendar.MONTH) + 1 + "",
                    monthArrayString) + 0);
        }
        if (choice_wv_layout3 != null) {
            dayArrayString = getDayArray(getDay(year, month));
            choice_wv_layout3.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
            choice_wv_layout3.setCurrentItem(getNumData(c.get(Calendar.DAY_OF_MONTH) + "",
                    dayArrayString));
        }
        if (choice_wv_layout4 != null) {
            choice_wv_layout4.setCurrentItem(getNumData(c.get(Calendar.HOUR_OF_DAY) + "",
                    hourArrayString));
        }
    }

    // 生成时间
    void createDate(String year, String month, String day) {
        String dateStr = year + "年" + month + "月" + day + "日";
        callBack.loadDataSuccess(dateStr);
    }

    // 生成时间
    void createDateWithHour(String year, String month, String day, String hour) {
        String dateStr = year + "年" + month + "月" + day + "日" + hour + "时";
        callBack.loadDataSuccess(dateStr);
    }

    // 在数组Array[]中找出字符串s的位置
    int getNumData(String s, String[] Array) {
        int num = 0;
        for (int i = 0; i < Array.length; i++) {
            if (s.equals(Array[i])) {
                num = i;
                break;
            }
        }
        return num;
    }

    // 根据当前年份和月份判断这个月的天数
    public int getDay(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0) { // 闰年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 29;
            } else {
                day = 30;
            }
        } else { // 平年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 28;
            } else {
                day = 30;
            }
        }
        return day;
    }

    // 根据数字生成一个字符串数组
    public String[] getDayArray(int day) {
        String[] dayArr = new String[day];
        for (int i = 0; i < day; i++) {
            dayArr[i] = i + 1 + "";
        }
        return dayArr;
    }

    // 根据初始值start和step得到一个字符数组，自start起至start+step-1
    public String[] getYEARArray(int start, int step) {
        String[] dayArr = new String[step];
        for (int i = 0; i < step; i++) {
            dayArr[i] = start + i + "";
        }
        return dayArr;
    }

    /*
 * 弹出dialog
 */
    public void getChooseDialog(Context context, View v) {
        dialog = new Dialog(context, R.style.set_dialog_style);
        try {
            dialog.setCancelable(true);// 可以用返回键取消
            dialog.setContentView(v);
            dialog.setCanceledOnTouchOutside(true);// 点击其它区域取消dialog
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            //设置显示动画
            window.setWindowAnimations(R.style.set_dialog_style);
            wlp.gravity = Gravity.BOTTOM;// 设置在最下面
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
            Util.closeSoftKey(context);
            dialog.show();
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "nameOrHeadDialog(Context context, int who)", e);
        }
    }

    public void dismissChooseDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "dismissChooseDialog", e);
        }
    }

    public interface loadDataCallBack {

        public void loadDataSuccess(String result);
    }
    public void setCallBack(loadDataCallBack callBack) {
        this.callBack = callBack;
    }

    public interface pressBtCallBack {

        public void pressSuccess(boolean isPressYes);
    }
    public void setCallBack(pressBtCallBack callBack) {
        this.pressCallBack = callBack;
    }

    //新增订单中房型点击返回的接口
    public interface HouseTypeCallBack{

        void houseTySuccess(String result, String room, String hall, String kitchen, String toilet, String balcony);
    }

    public void setCallBack(HouseTypeCallBack callBack) {
        this.houseTypeCallBack = callBack;
    }

    //新增订单中区域点击返回的接口
    public interface AreaCallBack{

        void areaSuccess(String result, int areaId);
    }

    public void setCallBack(AreaCallBack areaCallBack) {
        this.areaCallBack = areaCallBack;
    }
}
