package com.robusoft.lgd.wtfe.presentation;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.robusoft.lgd.wtfe.R;

import static com.robusoft.lgd.wtfe.utils.SystemUtils.getScreenHeight;
import static com.robusoft.lgd.wtfe.utils.SystemUtils.getStatusBarHeight;
import static com.robusoft.lgd.wtfe.utils.SystemUtils.hideSoftInputFromWindow;
import static com.robusoft.lgd.wtfe.utils.SystemUtils.showSoftInputFromWindow;


/**
 * User: lgd(1779964617@qq.com)
 * Date: 2017/4/1
 * Function: 仿微信聊天界面的自定义键盘，不适用于全屏模式
 * 提供面板高度(系统键盘和输入框的高度之和)，显示或隐藏自定义键盘，显示或隐藏系统键盘
 */
public abstract class KeyboardActivity extends FragmentActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    private static final String TAG = "KeyboardActivity";
    private static final int TOUCH_GAP_MILLISECOND = 200;

    private View mRootView;
    private View mInputPanel;
    private EditText mEditText;
    private View mCustomKeyboard;
    private ViewGroup mContentView;

    private Rect mViewRect = new Rect();

    private boolean isSystemKeyboardShowing;
    private boolean isCustomKeyboardShowing;

    private long mLastTouchMs;

    private int mSystemKeyboardHeight = 0;
    private int mScreenWithoutStatusBarHeight = 0;
    //TODO: 暂时没有处理好自定义键盘，待处理
    private boolean customKeyboardEnable = false;
    private boolean isAutoHideKeyboard = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_keyboard);
        mRootView = findViewById(R.id.keyboard_rootView);
        findViewById(R.id.keyboard_button).setOnClickListener(this);
        findViewById(R.id.keyboard_button2).setOnClickListener(this);
        mInputPanel = findViewById(R.id.keyboard_inputContainer);
        mEditText = (EditText) findViewById(R.id.keyboard_inputEditText);
        mContentView = (ViewGroup) findViewById(R.id.keyboard_contentContainer);
        mCustomKeyboard = findViewById(R.id.keyboard_customKeyboard);

        mScreenWithoutStatusBarHeight = getScreenHeight() - getStatusBarHeight();

        mRootView.addOnLayoutChangeListener(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mContentView);
    }

    /**
     * 系统键盘的高度是通过监听布局变化获取的，这就导致如果没有调起过键盘，系统键盘的高度就会为0。
     * 可以将上次获取的高度存到SP(SharedPreferences)文件中，如果调用改方法获取的值为0就从SP文件中加载缓存过的值。
     * 如果不为0则更新SP文件的值。
     *
     * @return 键盘和输入框总高度
     */
    public int getKeyboardWithInputPanelHeight() {
        int inputPanelHeight = mInputPanel.getHeight();
        if (inputPanelHeight == 0) {
            inputPanelHeight = getResources().getDimensionPixelSize(R.dimen.keyboard_input_panel_min_height);
        }
        if (mSystemKeyboardHeight == 0) {
            return 0;
        }
        return mSystemKeyboardHeight + inputPanelHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getInputPanelRect().contains((int) ev.getX(), (int) ev.getY())) {
            mLastTouchMs = Long.MAX_VALUE;
        } else {
            mLastTouchMs = System.currentTimeMillis();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        // 判断是否为点击系统键盘上的隐藏键盘的按钮
        final long touchDelta = System.currentTimeMillis() - mLastTouchMs;
        if (isKeyboardShown() && touchDelta > TOUCH_GAP_MILLISECOND) {
            hidKeyboard();
        }
        mLastTouchMs = System.currentTimeMillis();

        // 获取系统键盘的高度
        if (oldBottom != bottom && oldBottom == mScreenWithoutStatusBarHeight) {
            mSystemKeyboardHeight = oldBottom - bottom;
        }
    }

    public void setAutoHideKeyboard(boolean autoHide) {
        isAutoHideKeyboard = autoHide;
    }

    private void showCustomKeyboard() {
        if (!customKeyboardEnable) {
            return;
        }
        mCustomKeyboard.getLayoutParams().height = mSystemKeyboardHeight;
        mRootView.getLayoutParams().height = mScreenWithoutStatusBarHeight;

        mCustomKeyboard.setVisibility(View.VISIBLE);
        isCustomKeyboardShowing = true;

        hideSoftInputFromWindow(this);
        isSystemKeyboardShowing = false;
    }

    private void showSystemKeyboard() {
        mCustomKeyboard.setVisibility(View.INVISIBLE);
        isCustomKeyboardShowing = false;

        showSoftInputFromWindow();
        isSystemKeyboardShowing = true;
    }

    private void hidKeyboard() {
        mRootView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        mCustomKeyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCustomKeyboard.setVisibility(View.GONE);
            }
        }, 100);
        isCustomKeyboardShowing = false;

        hideSoftInputFromWindow(this);
        isSystemKeyboardShowing = false;
    }

    private boolean isKeyboardShown() {
        return isCustomKeyboardShowing || isSystemKeyboardShowing;
    }

    private int getInputPanelTop() {
        mInputPanel.getGlobalVisibleRect(mViewRect);
        return mViewRect.top;
    }

    private Rect getInputPanelRect() {
        mEditText.getGlobalVisibleRect(mViewRect);
        return mViewRect;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_button:
                if (mCustomKeyboard.getVisibility() != View.VISIBLE) {
                    showCustomKeyboard();
                } else {
                    showSystemKeyboard();
                }
                break;
            case R.id.keyboard_button2:
                hidKeyboard();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isKeyboardShown() && keyCode == KeyEvent.KEYCODE_BACK) {
            hidKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
