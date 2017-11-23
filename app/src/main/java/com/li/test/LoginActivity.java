package com.li.test;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.test.util.AsteriskPasswordTransformationMethod;
import com.li.test.util.HttpUrl;
import com.li.test.util.LogUtil;
import com.li.test.util.ToastManager;
import com.li.test.util.Util;

import org.json.JSONObject;


/**
 * @title LoginActivity 登录页面
 **/
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;// 登录--用户名输入框
    private EditText et_pw;// 登录--密码输入框
    private ImageView img_login_clear;// 登录--清空按钮
    private ImageView imy_login_eye;// 登录--密码可见不可见按钮
    private TextView tv_line_name;// 登录--用户名下方的线
    private TextView tv_line_pw;// 登录--密码下方的线
    private boolean isPwVisible = false;// 密码是否可见，默认不可见
    boolean isPostData = false;//如果数据已经上传成功,则不允许再次上传,避免出现多次上传同一个数据的现象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void btnLogin(View v) {
        try {
            if (isTextRight()) {
                if (!isPostData) {//避免多次提交数据
                    HttpLogin(et_name.getText().toString(), et_pw.getText().toString());
                } else {
                    ToastManager.showToast(LoginActivity.this, getResources().getString(R.string.data_uoload_success_no_again));
                }
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "btnLogin", e);
        }
    }

    private void HttpLogin(String name, String pw) {
        try {
            //网络判断
            if (!Util.judgeInternet(LoginActivity.this)) {
                return;
            }
            isPostData = true;
            String url = HttpUrl.appendUrl(HttpUrl.HTTP_LOGIN);
            LogUtil.v(getClass(), "HttpLogin---" + "url:" + url);
            ContentValues values = new ContentValues();
            values.put("UserName", name);
            values.put("PassWord", pw);
            //开始进行网络请求数据，这里模拟的直接是数据提交成功
            Message msg = handler.obtainMessage();
            msg.what = Util.HTTP_LOGIN_SUCCESS;
            handler.sendMessage(msg);
        } catch (Exception e) {
            LogUtil.e(getClass(), "HttpLogin", e);
        }
    }

    private void activityToMain() {
        try {
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(in);
            LoginActivity.this.finish();
            Util.ActivitySkip(LoginActivity.this);
        } catch (Exception e) {
            LogUtil.e(getClass(), "activityToMain", e);
        }
    }

    // hanlder处理处理数据.
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case Util.HTTP_LOGIN_SUCCESS:
                        ToastManager.showToast(LoginActivity.this, getString(R.string.login_strings_login_success));
                        activityToMain();
                        break;
//                    case Util.HTTP_LOGIN_ERROR:
//                        isPostData = false;
//                        String message = (String) msg.obj;
//                        ToastManager.showToast(LoginActivity.this, message);
//                        break;
                }
            } catch (Exception e) {
                LogUtil.e(getClass(), "handleMessage()", e);
            }

        }

        ;
    };

    /**
     * @return type
     * @title 判断输入文字是否正确
     * @params
     **/
    private boolean isTextRight() {
        boolean flag = true;
        try {
            if (et_name != null) {
                if (et_name.getText() == null
                        || et_name.getText().toString() == null
                        || "".equals(et_name.getText().toString())) {
                    ToastManager.showToast(LoginActivity.this, getString(R.string.login_strings_please_input_name));
                    flag = false;
                    return flag;
                }
            }
            if (et_pw != null) {
                if (et_pw.getText() == null
                        || et_pw.getText().toString() == null
                        || "".equals(et_pw.getText().toString())) {
                    ToastManager.showToast(LoginActivity.this, getString(R.string.login_strings_please_input_pw));
                    flag = false;
                    return flag;
                }
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "isTextRight", e);
        }
        return flag;
    }

    private void initView() {
        try {
            et_name = (EditText) findViewById(R.id.login_name);
            et_pw = (EditText) findViewById(R.id.login_pw);
            img_login_clear = (ImageView) findViewById(R.id.img_clean_login);
            imy_login_eye = (ImageView) findViewById(R.id.img_pw_visible);
            tv_line_name = (TextView) findViewById(R.id.login_name_line);
            tv_line_pw = (TextView) findViewById(R.id.login_pw_line);
            tv_line_name.setActivated(true);
            tv_line_pw.setActivated(false);
            // 监听焦点
            et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tv_line_name.setActivated(true);
                    } else {
                        tv_line_name.setActivated(false);
                    }
                }
            });
            // 监听焦点
            et_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tv_line_pw.setActivated(true);
                    } else {
                        tv_line_pw.setActivated(false);
                    }
                }
            });

            // 输入监听
            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        img_login_clear.setVisibility(View.VISIBLE);
                    } else {
                        img_login_clear.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            // 输入监听
            et_pw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // LogUtil.v(getClass(), "onTextChanged---" + "char:" + s + "\n" + "count:"+count);
                    if (s.length() > 0) {
                        imy_login_eye.setVisibility(View.VISIBLE);
                    } else {
                        imy_login_eye.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            img_login_clear.setOnClickListener(this);
            imy_login_eye.setOnClickListener(this);
        } catch (Exception e) {
            LogUtil.e(getClass(), "initView", e);
        }
    }

    @Override
    public String getActivitySimpleName() {
        return LoginActivity.class.getName();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_clean_login:// 清空按钮
                if (et_name != null && !Util.isTextNull(et_name.getText().toString())) {
                    et_name.setText("");
                }
                break;
            case R.id.img_pw_visible:// 密码可见不可见
                if (!isPwVisible) {
                    et_pw.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    imy_login_eye.setImageResource(R.drawable.icon_login_pw_visible);
                    isPwVisible = true;
                } else {
                    imy_login_eye.setImageResource(R.drawable.icon_login_pw_invisible);
                    et_pw.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    isPwVisible = false;
                }
                et_pw.setSelection(et_pw.getText().toString().length());
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN) {
            Util.ApplicationExit(LoginActivity.this);
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
