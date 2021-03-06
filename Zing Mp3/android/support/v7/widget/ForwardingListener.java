package android.support.v7.widget;

import android.os.Build.VERSION;
import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.mp3download.zingmp3.C1569R;

@RestrictTo({Scope.GROUP_ID})
public abstract class ForwardingListener implements OnTouchListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation;
    private Runnable mTriggerLongPress;

    /* renamed from: android.support.v7.widget.ForwardingListener.1 */
    class C02731 implements OnAttachStateChangeListener {
        C02731() {
        }

        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            ForwardingListener.this.onDetachedFromWindow();
        }
    }

    /* renamed from: android.support.v7.widget.ForwardingListener.2 */
    class C02742 implements OnGlobalLayoutListener {
        boolean mIsAttached;

        C02742() {
            this.mIsAttached = ForwardingListener.this.mSrc.isAttachedToWindow();
        }

        public void onGlobalLayout() {
            boolean wasAttached = this.mIsAttached;
            this.mIsAttached = ForwardingListener.this.mSrc.isAttachedToWindow();
            if (wasAttached && !this.mIsAttached) {
                ForwardingListener.this.onDetachedFromWindow();
            }
        }
    }

    private class DisallowIntercept implements Runnable {
        DisallowIntercept() {
        }

        public void run() {
            ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress implements Runnable {
        TriggerLongPress() {
        }

        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }

    public abstract ShowableListMenu getPopup();

    public ForwardingListener(View src) {
        this.mTmpLocation = new int[2];
        this.mSrc = src;
        src.setLongClickable(true);
        if (VERSION.SDK_INT >= 12) {
            addDetachListenerApi12(src);
        } else {
            addDetachListenerBase(src);
        }
        this.mScaledTouchSlop = (float) ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void addDetachListenerApi12(View src) {
        src.addOnAttachStateChangeListener(new C02731());
    }

    private void addDetachListenerBase(View src) {
        src.getViewTreeObserver().addOnGlobalLayoutListener(new C02742());
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean forwarding;
        boolean wasForwarding = this.mForwarding;
        if (!wasForwarding) {
            if (onTouchObserved(event) && onForwardingStarted()) {
                forwarding = true;
            } else {
                forwarding = false;
            }
            if (forwarding) {
                long now = SystemClock.uptimeMillis();
                MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(e);
                e.recycle();
            }
        } else if (onTouchForwarded(event) || !onForwardingStopped()) {
            forwarding = true;
        } else {
            forwarding = false;
        }
        this.mForwarding = forwarding;
        if (forwarding || wasForwarding) {
            return true;
        }
        return false;
    }

    private void onDetachedFromWindow() {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }

    protected boolean onForwardingStarted() {
        ShowableListMenu popup = getPopup();
        if (!(popup == null || popup.isShowing())) {
            popup.show();
        }
        return true;
    }

    protected boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
        return true;
    }

    private boolean onTouchObserved(MotionEvent srcEvent) {
        View src = this.mSrc;
        if (!src.isEnabled()) {
            return false;
        }
        switch (MotionEventCompat.getActionMasked(srcEvent)) {
            case C1569R.styleable.com_facebook_login_view_com_facebook_confirm_logout /*0*/:
                this.mActivePointerId = srcEvent.getPointerId(0);
                if (this.mDisallowIntercept == null) {
                    this.mDisallowIntercept = new DisallowIntercept();
                }
                src.postDelayed(this.mDisallowIntercept, (long) this.mTapTimeout);
                if (this.mTriggerLongPress == null) {
                    this.mTriggerLongPress = new TriggerLongPress();
                }
                src.postDelayed(this.mTriggerLongPress, (long) this.mLongPressTimeout);
                return false;
            case C1569R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped /*1*/:
            case C1569R.styleable.com_facebook_login_view_com_facebook_tooltip_mode /*3*/:
                clearCallbacks();
                return false;
            case C1569R.styleable.com_facebook_login_view_com_facebook_logout_text /*2*/:
                int activePointerIndex = srcEvent.findPointerIndex(this.mActivePointerId);
                if (activePointerIndex < 0 || pointInView(src, srcEvent.getX(activePointerIndex), srcEvent.getY(activePointerIndex), this.mScaledTouchSlop)) {
                    return false;
                }
                clearCallbacks();
                src.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            default:
                return false;
        }
    }

    private void clearCallbacks() {
        if (this.mTriggerLongPress != null) {
            this.mSrc.removeCallbacks(this.mTriggerLongPress);
        }
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }

    void onLongPress() {
        clearCallbacks();
        View src = this.mSrc;
        if (src.isEnabled() && !src.isLongClickable() && onForwardingStarted()) {
            src.getParent().requestDisallowInterceptTouchEvent(true);
            long now = SystemClock.uptimeMillis();
            MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
            src.onTouchEvent(e);
            e.recycle();
            this.mForwarding = true;
        }
    }

    private boolean onTouchForwarded(MotionEvent srcEvent) {
        boolean z = true;
        View src = this.mSrc;
        ShowableListMenu popup = getPopup();
        if (popup == null || !popup.isShowing()) {
            return false;
        }
        DropDownListView dst = (DropDownListView) popup.getListView();
        if (dst == null || !dst.isShown()) {
            return false;
        }
        MotionEvent dstEvent = MotionEvent.obtainNoHistory(srcEvent);
        toGlobalMotionEvent(src, dstEvent);
        toLocalMotionEvent(dst, dstEvent);
        boolean handled = dst.onForwardedEvent(dstEvent, this.mActivePointerId);
        dstEvent.recycle();
        int action = MotionEventCompat.getActionMasked(srcEvent);
        boolean keepForwarding;
        if (action == 1 || action == 3) {
            keepForwarding = false;
        } else {
            keepForwarding = true;
        }
        if (!(handled && keepForwarding)) {
            z = false;
        }
        return z;
    }

    private static boolean pointInView(View view, float localX, float localY, float slop) {
        return localX >= (-slop) && localY >= (-slop) && localX < ((float) (view.getRight() - view.getLeft())) + slop && localY < ((float) (view.getBottom() - view.getTop())) + slop;
    }

    private boolean toLocalMotionEvent(View view, MotionEvent event) {
        int[] loc = this.mTmpLocation;
        view.getLocationOnScreen(loc);
        event.offsetLocation((float) (-loc[0]), (float) (-loc[1]));
        return true;
    }

    private boolean toGlobalMotionEvent(View view, MotionEvent event) {
        int[] loc = this.mTmpLocation;
        view.getLocationOnScreen(loc);
        event.offsetLocation((float) loc[0], (float) loc[1]);
        return true;
    }
}
