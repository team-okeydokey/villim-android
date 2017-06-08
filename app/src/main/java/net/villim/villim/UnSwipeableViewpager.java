package net.villim.villim;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by seongmin on 6/8/17.
 */

public class UnSwipeableViewpager extends ViewPager {

    private boolean swipePageChangeEnabled;

    public UnSwipeableViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipePageChangeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipePageChangeEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return swipePageChangeEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.swipePageChangeEnabled = enabled;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
