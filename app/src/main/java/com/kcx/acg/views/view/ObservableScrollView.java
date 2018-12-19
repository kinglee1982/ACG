package com.kcx.acg.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * TODO<自定义监听滑动的ScrollView>
 *
 * @author: jb
 * @date: 2017/1/9 11:37
 */

public class ObservableScrollView extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public void setOnScrollListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {

            if (oldy < y) {// 手指向上滑动，屏幕内容下滑
                scrollViewListener.onScroll(oldy, y, false);

            } else if (oldy > y) {// 手指向下滑动，屏幕内容上滑
                scrollViewListener.onScroll(oldy, y, true);
            }

        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(e);
    }

    public interface ScrollViewListener {//dy Y轴滑动距离，isUp 是否返回顶部

        void onScroll(int oldy, int dy, boolean isUp);
    }
}