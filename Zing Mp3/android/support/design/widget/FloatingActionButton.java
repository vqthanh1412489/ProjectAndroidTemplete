package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.C0007R;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import com.google.android.exoplayer.C0989C;
import com.google.android.exoplayer.text.Cue;
import com.mp3download.zingmp3.C1569R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@DefaultBehavior(Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton {
    private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
    private static final String LOG_TAG = "FloatingActionButton";
    public static final int SIZE_AUTO = -1;
    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 0;
    private ColorStateList mBackgroundTint;
    private Mode mBackgroundTintMode;
    private int mBorderWidth;
    boolean mCompatPadding;
    private AppCompatImageHelper mImageHelper;
    int mImagePadding;
    private FloatingActionButtonImpl mImpl;
    private int mMaxImageSize;
    private int mRippleColor;
    final Rect mShadowPadding;
    private int mSize;
    private final Rect mTouchArea;

    /* renamed from: android.support.design.widget.FloatingActionButton.1 */
    class C00251 implements InternalVisibilityChangedListener {
        final /* synthetic */ OnVisibilityChangedListener val$listener;

        C00251(OnVisibilityChangedListener onVisibilityChangedListener) {
            this.val$listener = onVisibilityChangedListener;
        }

        public void onShown() {
            this.val$listener.onShown(FloatingActionButton.this);
        }

        public void onHidden() {
            this.val$listener.onHidden(FloatingActionButton.this);
        }
    }

    public static class Behavior extends android.support.design.widget.CoordinatorLayout.Behavior<FloatingActionButton> {
        private static final boolean AUTO_HIDE_DEFAULT = true;
        private boolean mAutoHideEnabled;
        private OnVisibilityChangedListener mInternalAutoHideListener;
        private Rect mTmpRect;

        public Behavior() {
            this.mAutoHideEnabled = AUTO_HIDE_DEFAULT;
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, C0007R.styleable.FloatingActionButton_Behavior_Layout);
            this.mAutoHideEnabled = a.getBoolean(C0007R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, AUTO_HIDE_DEFAULT);
            a.recycle();
        }

        public void setAutoHideEnabled(boolean autoHide) {
            this.mAutoHideEnabled = autoHide;
        }

        public boolean isAutoHideEnabled() {
            return this.mAutoHideEnabled;
        }

        public void onAttachedToLayoutParams(@NonNull LayoutParams lp) {
            if (lp.dodgeInsetEdges == 0) {
                lp.dodgeInsetEdges = 80;
            }
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child);
            } else if (isBottomSheet(dependency)) {
                updateFabVisibilityForBottomSheet(dependency, child);
            }
            return false;
        }

        private static boolean isBottomSheet(@NonNull View view) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof LayoutParams) {
                return ((LayoutParams) lp).getBehavior() instanceof BottomSheetBehavior;
            }
            return false;
        }

        @VisibleForTesting
        void setInternalAutoHideListener(OnVisibilityChangedListener listener) {
            this.mInternalAutoHideListener = listener;
        }

        private boolean shouldUpdateVisibility(View dependency, FloatingActionButton child) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (this.mAutoHideEnabled && lp.getAnchorId() == dependency.getId() && child.getUserSetVisibility() == 0) {
                return AUTO_HIDE_DEFAULT;
            }
            return false;
        }

        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent, AppBarLayout appBarLayout, FloatingActionButton child) {
            if (!shouldUpdateVisibility(appBarLayout, child)) {
                return false;
            }
            if (this.mTmpRect == null) {
                this.mTmpRect = new Rect();
            }
            Rect rect = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                child.hide(this.mInternalAutoHideListener, false);
            } else {
                child.show(this.mInternalAutoHideListener, false);
            }
            return AUTO_HIDE_DEFAULT;
        }

        private boolean updateFabVisibilityForBottomSheet(View bottomSheet, FloatingActionButton child) {
            if (!shouldUpdateVisibility(bottomSheet, child)) {
                return false;
            }
            if (bottomSheet.getTop() < (child.getHeight() / 2) + ((LayoutParams) child.getLayoutParams()).topMargin) {
                child.hide(this.mInternalAutoHideListener, false);
            } else {
                child.show(this.mInternalAutoHideListener, false);
            }
            return AUTO_HIDE_DEFAULT;
        }

        public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
            List<View> dependencies = parent.getDependencies(child);
            int count = dependencies.size();
            for (int i = 0; i < count; i += FloatingActionButton.SIZE_MINI) {
                View dependency = (View) dependencies.get(i);
                if (!(dependency instanceof AppBarLayout)) {
                    if (isBottomSheet(dependency) && updateFabVisibilityForBottomSheet(dependency, child)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child)) {
                    break;
                }
            }
            parent.onLayoutChild(child, layoutDirection);
            offsetIfNeeded(parent, child);
            return AUTO_HIDE_DEFAULT;
        }

        public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull Rect rect) {
            Rect shadowPadding = child.mShadowPadding;
            rect.set(child.getLeft() + shadowPadding.left, child.getTop() + shadowPadding.top, child.getRight() - shadowPadding.right, child.getBottom() - shadowPadding.bottom);
            return AUTO_HIDE_DEFAULT;
        }

        private void offsetIfNeeded(CoordinatorLayout parent, FloatingActionButton fab) {
            Rect padding = fab.mShadowPadding;
            if (padding != null && padding.centerX() > 0 && padding.centerY() > 0) {
                LayoutParams lp = (LayoutParams) fab.getLayoutParams();
                int offsetTB = 0;
                int offsetLR = 0;
                if (fab.getRight() >= parent.getWidth() - lp.rightMargin) {
                    offsetLR = padding.right;
                } else if (fab.getLeft() <= lp.leftMargin) {
                    offsetLR = -padding.left;
                }
                if (fab.getBottom() >= parent.getHeight() - lp.bottomMargin) {
                    offsetTB = padding.bottom;
                } else if (fab.getTop() <= lp.topMargin) {
                    offsetTB = -padding.top;
                }
                if (offsetTB != 0) {
                    ViewCompat.offsetTopAndBottom(fab, offsetTB);
                }
                if (offsetLR != 0) {
                    ViewCompat.offsetLeftAndRight(fab, offsetLR);
                }
            }
        }
    }

    public static abstract class OnVisibilityChangedListener {
        public void onShown(FloatingActionButton fab) {
        }

        public void onHidden(FloatingActionButton fab) {
        }
    }

    private class ShadowDelegateImpl implements ShadowViewDelegate {
        ShadowDelegateImpl() {
        }

        public float getRadius() {
            return ((float) FloatingActionButton.this.getSizeDimension()) / 2.0f;
        }

        public void setShadowPadding(int left, int top, int right, int bottom) {
            FloatingActionButton.this.mShadowPadding.set(left, top, right, bottom);
            FloatingActionButton.this.setPadding(FloatingActionButton.this.mImagePadding + left, FloatingActionButton.this.mImagePadding + top, FloatingActionButton.this.mImagePadding + right, FloatingActionButton.this.mImagePadding + bottom);
        }

        public void setBackgroundDrawable(Drawable background) {
            super.setBackgroundDrawable(background);
        }

        public boolean isCompatPaddingEnabled() {
            return FloatingActionButton.this.mCompatPadding;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Size {
    }

    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mShadowPadding = new Rect();
        this.mTouchArea = new Rect();
        ThemeUtils.checkAppCompatTheme(context);
        TypedArray a = context.obtainStyledAttributes(attrs, C0007R.styleable.FloatingActionButton, defStyleAttr, C0007R.style.Widget_Design_FloatingActionButton);
        this.mBackgroundTint = a.getColorStateList(C0007R.styleable.FloatingActionButton_backgroundTint);
        this.mBackgroundTintMode = ViewUtils.parseTintMode(a.getInt(C0007R.styleable.FloatingActionButton_backgroundTintMode, SIZE_AUTO), null);
        this.mRippleColor = a.getColor(C0007R.styleable.FloatingActionButton_rippleColor, 0);
        this.mSize = a.getInt(C0007R.styleable.FloatingActionButton_fabSize, SIZE_AUTO);
        this.mBorderWidth = a.getDimensionPixelSize(C0007R.styleable.FloatingActionButton_borderWidth, 0);
        float elevation = a.getDimension(C0007R.styleable.FloatingActionButton_elevation, 0.0f);
        float pressedTranslationZ = a.getDimension(C0007R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        this.mCompatPadding = a.getBoolean(C0007R.styleable.FloatingActionButton_useCompatPadding, false);
        a.recycle();
        this.mImageHelper = new AppCompatImageHelper(this);
        this.mImageHelper.loadFromAttributes(attrs, defStyleAttr);
        this.mMaxImageSize = (int) getResources().getDimension(C0007R.dimen.design_fab_image_size);
        getImpl().setBackgroundDrawable(this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        getImpl().setElevation(elevation);
        getImpl().setPressedTranslationZ(pressedTranslationZ);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int preferredSize = getSizeDimension();
        this.mImagePadding = (preferredSize - this.mMaxImageSize) / 2;
        getImpl().updatePadding();
        int d = Math.min(resolveAdjustedSize(preferredSize, widthMeasureSpec), resolveAdjustedSize(preferredSize, heightMeasureSpec));
        setMeasuredDimension((this.mShadowPadding.left + d) + this.mShadowPadding.right, (this.mShadowPadding.top + d) + this.mShadowPadding.bottom);
    }

    public void setRippleColor(@ColorInt int color) {
        if (this.mRippleColor != color) {
            this.mRippleColor = color;
            getImpl().setRippleColor(color);
        }
    }

    @Nullable
    public ColorStateList getBackgroundTintList() {
        return this.mBackgroundTint;
    }

    public void setBackgroundTintList(@Nullable ColorStateList tint) {
        if (this.mBackgroundTint != tint) {
            this.mBackgroundTint = tint;
            getImpl().setBackgroundTintList(tint);
        }
    }

    @Nullable
    public Mode getBackgroundTintMode() {
        return this.mBackgroundTintMode;
    }

    public void setBackgroundTintMode(@Nullable Mode tintMode) {
        if (this.mBackgroundTintMode != tintMode) {
            this.mBackgroundTintMode = tintMode;
            getImpl().setBackgroundTintMode(tintMode);
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setBackgroundResource(int resid) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setBackgroundColor(int color) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setImageResource(@DrawableRes int resId) {
        this.mImageHelper.setImageResource(resId);
    }

    public void show() {
        show(null);
    }

    public void show(@Nullable OnVisibilityChangedListener listener) {
        show(listener, true);
    }

    void show(OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().show(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    public void hide() {
        hide(null);
    }

    public void hide(@Nullable OnVisibilityChangedListener listener) {
        hide(listener, true);
    }

    void hide(@Nullable OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().hide(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    public void setUseCompatPadding(boolean useCompatPadding) {
        if (this.mCompatPadding != useCompatPadding) {
            this.mCompatPadding = useCompatPadding;
            getImpl().onCompatShadowChanged();
        }
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    public void setSize(int size) {
        if (size != this.mSize) {
            this.mSize = size;
            requestLayout();
        }
    }

    public int getSize() {
        return this.mSize;
    }

    @Nullable
    private InternalVisibilityChangedListener wrapOnVisibilityChangedListener(@Nullable OnVisibilityChangedListener listener) {
        if (listener == null) {
            return null;
        }
        return new C00251(listener);
    }

    int getSizeDimension() {
        return getSizeDimension(this.mSize);
    }

    private int getSizeDimension(int size) {
        Resources res = getResources();
        switch (size) {
            case SIZE_AUTO /*-1*/:
                if (Math.max(ConfigurationHelper.getScreenWidthDp(res), ConfigurationHelper.getScreenHeightDp(res)) < AUTO_MINI_LARGEST_SCREEN_WIDTH) {
                    return getSizeDimension(SIZE_MINI);
                }
                return getSizeDimension(0);
            case SIZE_MINI /*1*/:
                return res.getDimensionPixelSize(C0007R.dimen.design_fab_size_mini);
            default:
                return res.getDimensionPixelSize(C0007R.dimen.design_fab_size_normal);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().onDetachedFromWindow();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().onDrawableStateChanged(getDrawableState());
    }

    @TargetApi(11)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().jumpDrawableToCurrentState();
    }

    public boolean getContentRect(@NonNull Rect rect) {
        if (!ViewCompat.isLaidOut(this)) {
            return false;
        }
        rect.set(0, 0, getWidth(), getHeight());
        rect.left += this.mShadowPadding.left;
        rect.top += this.mShadowPadding.top;
        rect.right -= this.mShadowPadding.right;
        rect.bottom -= this.mShadowPadding.bottom;
        return true;
    }

    @NonNull
    public Drawable getContentBackground() {
        return getImpl().getContentBackground();
    }

    private static int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Cue.TYPE_UNSET /*-2147483648*/:
                return Math.min(desiredSize, specSize);
            case C1569R.styleable.com_facebook_login_view_com_facebook_confirm_logout /*0*/:
                return desiredSize;
            case C0989C.ENCODING_PCM_32BIT /*1073741824*/:
                return specSize;
            default:
                return result;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!getContentRect(this.mTouchArea) || this.mTouchArea.contains((int) ev.getX(), (int) ev.getY())) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public float getCompatElevation() {
        return getImpl().getElevation();
    }

    public void setCompatElevation(float elevation) {
        getImpl().setElevation(elevation);
    }

    private FloatingActionButtonImpl getImpl() {
        if (this.mImpl == null) {
            this.mImpl = createImpl();
        }
        return this.mImpl;
    }

    private FloatingActionButtonImpl createImpl() {
        int sdk = VERSION.SDK_INT;
        if (sdk >= 21) {
            return new FloatingActionButtonLollipop(this, new ShadowDelegateImpl(), ViewUtils.DEFAULT_ANIMATOR_CREATOR);
        }
        if (sdk >= 14) {
            return new FloatingActionButtonIcs(this, new ShadowDelegateImpl(), ViewUtils.DEFAULT_ANIMATOR_CREATOR);
        }
        return new FloatingActionButtonGingerbread(this, new ShadowDelegateImpl(), ViewUtils.DEFAULT_ANIMATOR_CREATOR);
    }
}