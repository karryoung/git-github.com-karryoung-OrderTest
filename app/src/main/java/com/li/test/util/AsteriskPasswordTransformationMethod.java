package com.li.test.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * 用于将输入的密码隐藏
 */
public class AsteriskPasswordTransformationMethod extends
        PasswordTransformationMethod {
	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		return new PasswordCharSequence(source);
	}

	private class PasswordCharSequence implements CharSequence {
		private CharSequence mSource;

		public PasswordCharSequence(CharSequence source) {
			mSource = source; // Store char sequence
		}

		public char charAt(int index) {
			return '*'; // This is the important part
		}

		public int length() {
			return mSource.length(); // Return default
		}

		public CharSequence subSequence(int start, int end) {
			return mSource.subSequence(start, end); // Return default
		}
	}
}