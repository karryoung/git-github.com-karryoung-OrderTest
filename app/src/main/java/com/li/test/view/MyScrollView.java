package com.li.test.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.li.test.util.LogUtil;

/**
 * 添加scrollview下拉时图片拉伸效果
 */
public class MyScrollView extends ScrollView implements View.OnTouchListener {

	// 记录首次按下位置
	private float mFirstPosition = 0;
	// 是否正在放大
	private Boolean mScaling = false;

	private View dropZoomView;
	private int dropZoomViewWidth;
	private int dropZoomViewHeight;
	private MyScrollViewListener scrollViewListener = null;

	private boolean isCanZoom = true;// 是否执行下拉图片拉伸效果

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public void setScrollViewListener(MyScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int scrollX, int scrollY, int oldx, int oldy) {
		try {
		
		super.onScrollChanged(scrollX, scrollY, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, scrollX, scrollY, oldx,
					oldy);
		}
		
		} catch (Exception e) {
			LogUtil.e(getClass(), "onScrollChanged(int scrollX, int scrollY, int oldx, int oldy)", e);
		}
	}

	private void init() {
		try {
		
		setOverScrollMode(OVER_SCROLL_NEVER);
		if (getChildAt(0) != null) {
			ViewGroup vg = (ViewGroup) getChildAt(0);
			if (vg.getChildAt(0) != null) {
				dropZoomView = vg.getChildAt(0);
				setOnTouchListener(this);
			}
		}
		
		} catch (Exception e) {
			LogUtil.e(getClass(), "init()", e);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
		
		if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
			dropZoomViewWidth = dropZoomView.getMeasuredWidth();
			dropZoomViewHeight = dropZoomView.getMeasuredHeight();
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 手指离开后恢复图片
			mScaling = false;
			replyImage();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mScaling) {
				if (getScrollY() == 0) {
					mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
				} else {
					break;
				}
			}
			int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
			if (distance < 0) { // 当前位置比记录位置要小，正常返回
				break;
			}

			// 处理放大
			mScaling = true;
			setZoom(1 + distance);
			return true; // 返回true表示已经完成触摸事件，不再处理
		}
		
		} catch (Exception e) {
			LogUtil.e(getClass(), "onTouch(View v, MotionEvent event)", e);
		}
		return false;
	}

	// 回弹动画 (使用了属性动画)
	public void replyImage() {
		try {
		
		final float distance = dropZoomView.getMeasuredWidth()
				- dropZoomViewWidth;

		// 设置动画
		ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(
				(long) (distance * 0.7));

		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				setZoom(distance - ((distance) * cVal));
			}
		});
		anim.start();
		
		} catch (Exception e) {
			LogUtil.e(getClass(), "replyImage()", e);
		}

	}

	// 缩放
	public void setZoom(float s) {
		try {

			if (!isCanZoom) {
				return;
			}

			if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {
				return;
			}
			ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
			lp.width = (int) (dropZoomViewWidth + s);
			lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));
			dropZoomView.setLayoutParams(lp);

		} catch (Exception e) {
			LogUtil.e(getClass(), "setZoom(float s)", e);
		}
	}

	/**
	 * 设置是否执行下拉时图片拉伸动画
	 */
	public void setIsCanZoom(boolean isCanZoom) {
		this.isCanZoom = isCanZoom;
	}
}