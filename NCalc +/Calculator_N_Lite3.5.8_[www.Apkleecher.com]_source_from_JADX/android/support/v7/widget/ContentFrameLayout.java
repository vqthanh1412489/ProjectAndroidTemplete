package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import com.google.common.primitives.Ints;

public class ContentFrameLayout extends FrameLayout {
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;

    public interface OnAttachListener {
        void onAttachedFromWindow();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public ContentFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDecorPadding = new Rect();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void dispatchFitSystemWindows(Rect insets) {
        fitSystemWindows(insets);
    }

    public void setAttachListener(OnAttachListener attachListener) {
        this.mAttachListener = attachListener;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setDecorPadding(int left, int top, int right, int bottom) {
        this.mDecorPadding.set(left, top, right, bottom);
        if (ViewCompat.isLaidOut(this)) {
            requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        boolean fixedWidth = false;
        if (widthMode == Integer.MIN_VALUE) {
            TypedValue tvw = isPortrait ? this.mFixedWidthMinor : this.mFixedWidthMajor;
            if (!(tvw == null || tvw.type == 0)) {
                int w = 0;
                i = tvw.type;
                if (r0 == 5) {
                    w = (int) tvw.getDimension(metrics);
                } else {
                    i = tvw.type;
                    if (r0 == 6) {
                        w = (int) tvw.getFraction((float) metrics.widthPixels, (float) metrics.widthPixels);
                    }
                }
                if (w > 0) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(w - (this.mDecorPadding.left + this.mDecorPadding.right), MeasureSpec.getSize(widthMeasureSpec)), Ints.MAX_POWER_OF_TWO);
                    fixedWidth = true;
                }
            }
        }
        if (heightMode == Integer.MIN_VALUE) {
            TypedValue tvh = isPortrait ? this.mFixedHeightMajor : this.mFixedHeightMinor;
            if (!(tvh == null || tvh.type == 0)) {
                int h = 0;
                i = tvh.type;
                if (r0 == 5) {
                    h = (int) tvh.getDimension(metrics);
                } else {
                    i = tvh.type;
                    if (r0 == 6) {
                        h = (int) tvh.getFraction((float) metrics.heightPixels, (float) metrics.heightPixels);
                    }
                }
                if (h > 0) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(h - (this.mDecorPadding.top + this.mDecorPadding.bottom), MeasureSpec.getSize(heightMeasureSpec)), Ints.MAX_POWER_OF_TWO);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        boolean measure = false;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, Ints.MAX_POWER_OF_TWO);
        if (!fixedWidth && widthMode == Integer.MIN_VALUE) {
            TypedValue tv = isPortrait ? this.mMinWidthMinor : this.mMinWidthMajor;
            if (!(tv == null || tv.type == 0)) {
                int min = 0;
                i = tv.type;
                if (r0 == 5) {
                    min = (int) tv.getDimension(metrics);
                } else {
                    i = tv.type;
                    if (r0 == 6) {
                        min = (int) tv.getFraction((float) metrics.widthPixels, (float) metrics.widthPixels);
                    }
                }
                if (min > 0) {
                    min -= this.mDecorPadding.left + this.mDecorPadding.right;
                }
                if (width < min) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(min, Ints.MAX_POWER_OF_TWO);
                    measure = true;
                }
            }
        }
        if (measure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }

    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }

    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mAttachListener != null) {
            this.mAttachListener.onAttachedFromWindow();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttachListener != null) {
            this.mAttachListener.onDetachedFromWindow();
        }
    }
}