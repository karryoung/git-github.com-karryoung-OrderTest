package com.li.test;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.li.test.util.LogUtil;
import com.li.test.util.Util;

/**
 * 基础Activity
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity {
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				this.finish();
				Util.ActivityExit(BaseActivity.this);
			} else if (keyCode == KeyEvent.KEYCODE_HOME) {
				moveTaskToBack(true);
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.e(getClass(), "onKeyDown(int keyCode, KeyEvent event)", e);
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public abstract String getActivitySimpleName();
}
