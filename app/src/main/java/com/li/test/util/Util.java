package com.li.test.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.li.test.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final int HTTP_LOGIN_SUCCESS = 0X01;// 登录的请求成功
    public static final int REVATION_REQUESTCODE = 0x02;
    public static final int HTTP_SUCCESS_CODE = 1;// 获取数据成功
    public static final int HTTP_MYORDER_SUCCESS = 0x03;
    public static final int HTTP_MYORDER_ERROR = 0x04;
    public static final int ADD_ORDER_RESULTCODE = 0x05;//从新增订单页面保存成功之后的返回码
    public static final int ADD_ORDER_REQUESTCODE = 0x06;//跳转到新增订单页面的请求码
    public static final String REFRESH_MY_ORDER_LIST_ACTION = "refresh";//发送广播刷新订单列表页面
    // 判断字符串是否为数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).find();
    }

    //判断字符是否为汉字
    public static boolean isCCharacter(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        return pattern.matcher(str).find();
    }

    //判断字符是否为英文
    public static boolean isEnglish(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        return pattern.matcher(str).find();
    }

    // 判断手机号码(开始为1，长度为11)
    public static boolean isMobileNO(String mobiles) {
        boolean flag = true;
        try {
            if (mobiles.length() != 11 || !mobiles.startsWith("1")) {
                flag = false;
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "isMobileNO", e);
        }
        return flag;
    }

    // 判断手机号码(数字，大于8位)
    public static boolean isMobileNO2(String mobiles) {
        boolean flag = true;
        try {
            if (mobiles.length() < 8) {
                flag = false;
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "isMobileNO2", e);
        }
        return flag;
    }

    // 如果返回false
    public static boolean judgeInternet(Context context) {// 是否连接网络
        try {

            if (!isNetworkConnected(context)) {
                ToastManager.showToast(context, context.getString(R.string.nerwork_error_retry));
                return false;
            }

        } catch (Exception e) {
            LogUtil.e(Util.class, "judgeInternet(Context context)", e);
        }
        return true;
    }

    // 判断当前网络是否连接
    public static boolean isNetworkConnected(Context context) {
        try {

            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager
                        .getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }

        } catch (Exception e) {
            LogUtil.e(Util.class, "isNetworkConnected(Context context)", e);
        }
        return false;
    }


    /**
     * 根据不同手机分辨率获取相应的比例值
     */
    public static float getScale(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }

    public static int getWorkGroupIdFromSharedPreferce(Context context) {
        SharedPreferences sharedPreferce = context.getSharedPreferences(
                "userinfo", Context.MODE_APPEND);
        return sharedPreferce.getInt("workGroupId", -1);
    }

    //应用退出动画
    public static void ApplicationExit(Context context) {
        try {
            ((Activity) context).overridePendingTransition(
                    0, R.anim.application_out_to_bottom);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ApplicationExit", e);
        }
    }

    public static void ActivitySkip(Context context) {
        try {
            ((Activity) context).overridePendingTransition(
                    R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivitySkip", e);
        }

    }

    public static void ActivityExit(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.activity_in_from_left,
                    R.anim.activity_out_to_right);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivityExit", e);
        }
    }

    /**
     * 淡入淡出动画
     */
    public static void ActivityAlpha(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.activity_enter_alpha,
                    R.anim.activity_exit_alpha);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivityAlpha", e);
        }
    }

    public static void ActivityUp(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.activity_bottom_to_up, 0);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivityExit", e);
        }
    }

    // 获取系统版本号
    public static String getVersionName(Context context) {
        String version = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            LogUtil.e(Util.class, "getVersionName", e);
        }
        return version;
    }

    // 获取系统versionCode
    public static int getVersionCode(Context context) {
        int version = -1;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
            LogUtil.e(Util.class, "getVersionCode", e);
        }
        return version;
    }

    /*
     * 半角转换为全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /*
     * 去除特殊字符或将所有中文标号替换为英文标号
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * @param text 判断字符串是否为空，true为空，false不为空
     * @return
     */
    public static boolean isTextNull(String text) {
        boolean flag = true;
        try {
            if ((!TextUtils.isEmpty(text)) && (!text.equals("null"))) {
                flag = false;
                return flag;
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "Util--isTextNull(String text)", e);
        }
        return flag;
    }

    /**
     * 获取屏幕宽度
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        int width = 0;
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            width = wm.getDefaultDisplay().getWidth();
        } catch (Exception e) {
            LogUtil.e(Util.class, "Util--getScreenWidth(Context context)", e);
        }
        return width;
    }

    /**
     * 获取屏幕高度
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        int height = 0;
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            height = wm.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            LogUtil.e(Util.class, "Util--getScreenHeight(Context context)", e);
        }
        return height;
    }

    //获取控件高度
    public static int getViewHeight(View view) {
        try {
            int ww = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int hh = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(ww, hh);
            int height = view.getMeasuredHeight();
            return height;
        } catch (Exception e) {
            LogUtil.e(Util.class, "getViewHeight(View view)", e);
        }
        return 0;
    }

    /**
     * 不用科学计数法显示金额
     *
     * @param count
     * @param isDecimal 是否保留2位小数
     * @return
     */
    public static String getNonScientificCount(String count, boolean isDecimal) {
        try {
            if (!Util.isTextNull(count)) {
                DecimalFormat df = new DecimalFormat(
                        isDecimal ? "################0.00"
                                : "################0");
                double value = Double.parseDouble(count);
                count = df.format(value);
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "getNonScientificCount()", e);
        }
        return count;
    }

    /**
     * 不用科学计数法显示整数金额(四舍五入取整)
     *
     * @param count
     * @return
     */
    public static String getIntegerNonScientificCount(String count) {
        try {
            if (!Util.isTextNull(count)) {
                DecimalFormat df = new DecimalFormat("################0");
                double value = Double.parseDouble(count);
                // value = (value > 0) ? Math.floor(value) : Math.ceil(value);
                value = Math.round(value);
                count = df.format(value);
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "getIntegerNonScientificCount()", e);
        }
        return count;
    }

    /**
     * 不用科学计数法显示金额(保留两位小数，不准四舍五入)
     *
     * @param count
     * @return
     */
    public static String getNonScientificCountTwoDeci(String count) {
        try {
            if (!Util.isTextNull(count)) {
                // LogUtil.v(Util.class, "getNonScientificCountTwoDeci---" + "count1:" + count);
                DecimalFormat df = new DecimalFormat("################0.00");
                DecimalFormat df2 = new DecimalFormat("#.00");
                //double value = Double.parseDouble(count);

                BigDecimal bd = new BigDecimal(count);
                BigDecimal setScale = bd.setScale(2, bd.ROUND_DOWN);

                count = df.format(Double.parseDouble(setScale + ""));
                // LogUtil.v(Util.class, "getNonScientificCountTwoDeci---" + "count2:" + count);
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "getNonScientificCountTwoDeci()", e);
        }
        return count;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取当前系统的时间
     */
    public static String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 转义特殊字符
     *
     * @param s
     * @return
     */
    public static String replaceSpecialChar(String s) {
        try {
            if (!isTextNull(s)) {
                // s = s.replace("?", "%3F");
                // s = s.replace("&", "%26");
                // s = s.replace("|", "%124");
                // s = s.replace("=", "%3D");
                // s = s.replace("#", "%23");
                // s = s.replace("/", "%2F");
                // s = s.replace("+", "%2B");
                s = s.replace("%", "%25");
                s = s.replace(" ", "%20");
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "replaceSpecialChar", e);
        }
        return s;
    }

    /*
     * 判断是否为整数
     *
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /*
     * 去掉字符串中时、分、秒。得到年月日
     */
    public static String getTimeBir(Context context, String str) {
        try {
            SimpleDateFormat datestr = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date date = datestr.parse(str);
            SimpleDateFormat birstr = new SimpleDateFormat("yyyy/MM/dd");
            return birstr.format(date);
        } catch (Exception e) {
            LogUtil.e(context.getClass(), "getTimeBir(String str)", e);
        }
        return null;
    }

    // 关闭软键盘
    public static void closeSoftKey(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && ((Activity) context).getCurrentFocus() != null) {// 如果处于打开状态再关闭
                if (((Activity) context).getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "closeSoftKey", e);
        }
    }

    /**
     * 更改字体大小和颜色
     * size是像素大小
     * start字符串开始改变的位置
     */
    public static SpannableString changStr(String str, int size, int color, int start) {
        SpannableString spanString = new SpannableString(str);
        //改变其颜色
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //改变其大小
        AbsoluteSizeSpan abspan = new AbsoluteSizeSpan(size, false);
        spanString.setSpan(abspan, start, spanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public static double stringTransDouble(String str) {
        double d = 0;
        if (Util.isTextNull(str) || str.equals(".")) {
            return d;
        } else {
            d = Double.parseDouble(str);
        }
        return d;
    }

    //拨打电话
    public static void callPhone(Context context, String phone) {
        try {
            if (!Util.isTextNull(phone)) {
                Intent intentCallPhone = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intentCallPhone.setData(data);
                context.startActivity(intentCallPhone);
            } else {
                ToastManager.showToast(context, context.getResources().getString(R.string.phone_str_null));
            }
        } catch (Exception e) {
            LogUtil.e(Util.class, "callPhone()", e);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        try {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            String text = editText.getText().toString();
            if (!Util.isTextNull(text)) {
                editText.setSelection(text.length());
            }
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (Exception e) {
            LogUtil.e(Util.class, "showSoftInputFromWindow", e);
        }
    }

    //输入框监听，只能输入小数点后两位
    public static void setEditTextChangeListen(final EditText editText){
        try {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //限制输入小数点后两位
                    if (s.toString().contains(".")) {
                        //如果要限制输入的小数点后的位数，只需更改数字“2”
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    //如果输入的第一个字符是“.”那么改成“0.”
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }

                    //如果输入的第一个字符是“0”那么输入的第二个字符只能是“.”
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            LogUtil.e(Util.class, "setEditTextChangeListen()", e);
        }
    }

}
