package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import org.apache.commons.math4.random.ValueServer;
import org.matheclipse.core.interfaces.IExpr;

public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>> {
    private ArrayList<AngularVelocitySample> _velocitySamples;
    private float mDecelerationAngularVelocity;
    private long mDecelerationLastTime;
    private float mStartAngle;
    private MPPointF mTouchStartPoint;

    private class AngularVelocitySample {
        public float angle;
        public long time;

        public AngularVelocitySample(long time, float angle) {
            this.time = time;
            this.angle = angle;
        }
    }

    public PieRadarChartTouchListener(PieRadarChartBase<?> chart) {
        super(chart);
        this.mTouchStartPoint = MPPointF.getInstance(0.0f, 0.0f);
        this.mStartAngle = 0.0f;
        this._velocitySamples = new ArrayList();
        this.mDecelerationLastTime = 0;
        this.mDecelerationAngularVelocity = 0.0f;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        if (!this.mGestureDetector.onTouchEvent(event) && ((PieRadarChartBase) this.mChart).isRotationEnabled()) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case ValueServer.DIGEST_MODE /*0*/:
                    startAction(event);
                    stopDeceleration();
                    resetVelocity();
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }
                    setGestureStartAngle(x, y);
                    this.mTouchStartPoint.x = x;
                    this.mTouchStartPoint.y = y;
                    break;
                case ValueServer.REPLAY_MODE /*1*/:
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        stopDeceleration();
                        sampleVelocity(x, y);
                        this.mDecelerationAngularVelocity = calculateVelocity();
                        if (this.mDecelerationAngularVelocity != 0.0f) {
                            this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                            Utils.postInvalidateOnAnimation(this.mChart);
                        }
                    }
                    ((PieRadarChartBase) this.mChart).enableScroll();
                    this.mTouchMode = 0;
                    endAction(event);
                    break;
                case IExpr.DOUBLEID /*2*/:
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }
                    if (this.mTouchMode == 0 && ChartTouchListener.distance(x, this.mTouchStartPoint.x, y, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0f)) {
                        this.mLastGesture = ChartGesture.ROTATE;
                        this.mTouchMode = 6;
                        ((PieRadarChartBase) this.mChart).disableScroll();
                    } else if (this.mTouchMode == 6) {
                        updateGestureRotation(x, y);
                        ((PieRadarChartBase) this.mChart).invalidate();
                    }
                    endAction(event);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public void onLongPress(MotionEvent me) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        OnChartGestureListener l = ((PieRadarChartBase) this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartLongPressed(me);
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        OnChartGestureListener l = ((PieRadarChartBase) this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartSingleTapped(e);
        }
        if (!((PieRadarChartBase) this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        performHighlight(((PieRadarChartBase) this.mChart).getHighlightByTouchPoint(e.getX(), e.getY()), e);
        return true;
    }

    private void resetVelocity() {
        this._velocitySamples.clear();
    }

    private void sampleVelocity(float touchLocationX, float touchLocationY) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        this._velocitySamples.add(new AngularVelocitySample(currentTime, ((PieRadarChartBase) this.mChart).getAngleForPoint(touchLocationX, touchLocationY)));
        int i = 0;
        int count = this._velocitySamples.size();
        while (count - 2 > 0 && currentTime - ((AngularVelocitySample) this._velocitySamples.get(i)).time > 1000) {
            this._velocitySamples.remove(0);
            count--;
            i = (i - 1) + 1;
        }
    }

    private float calculateVelocity() {
        if (this._velocitySamples.isEmpty()) {
            return 0.0f;
        }
        AngularVelocitySample firstSample = (AngularVelocitySample) this._velocitySamples.get(0);
        AngularVelocitySample lastSample = (AngularVelocitySample) this._velocitySamples.get(this._velocitySamples.size() - 1);
        AngularVelocitySample beforeLastSample = firstSample;
        for (int i = this._velocitySamples.size() - 1; i >= 0; i--) {
            beforeLastSample = (AngularVelocitySample) this._velocitySamples.get(i);
            if (beforeLastSample.angle != lastSample.angle) {
                break;
            }
        }
        float timeDelta = ((float) (lastSample.time - firstSample.time)) / 1000.0f;
        if (timeDelta == 0.0f) {
            timeDelta = 0.1f;
        }
        boolean clockwise = lastSample.angle >= beforeLastSample.angle;
        if (((double) Math.abs(lastSample.angle - beforeLastSample.angle)) > 270.0d) {
            clockwise = !clockwise;
        }
        if (((double) (lastSample.angle - firstSample.angle)) > 180.0d) {
            firstSample.angle = (float) (((double) firstSample.angle) + 360.0d);
        } else if (((double) (firstSample.angle - lastSample.angle)) > 180.0d) {
            lastSample.angle = (float) (((double) lastSample.angle) + 360.0d);
        }
        float velocity = Math.abs((lastSample.angle - firstSample.angle) / timeDelta);
        if (clockwise) {
            return velocity;
        }
        return -velocity;
    }

    public void setGestureStartAngle(float x, float y) {
        this.mStartAngle = ((PieRadarChartBase) this.mChart).getAngleForPoint(x, y) - ((PieRadarChartBase) this.mChart).getRawRotationAngle();
    }

    public void updateGestureRotation(float x, float y) {
        ((PieRadarChartBase) this.mChart).setRotationAngle(((PieRadarChartBase) this.mChart).getAngleForPoint(x, y) - this.mStartAngle);
    }

    public void stopDeceleration() {
        this.mDecelerationAngularVelocity = 0.0f;
    }

    public void computeScroll() {
        if (this.mDecelerationAngularVelocity != 0.0f) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDecelerationAngularVelocity = ((PieRadarChartBase) this.mChart).getDragDecelerationFrictionCoef() * this.mDecelerationAngularVelocity;
            ((PieRadarChartBase) this.mChart).setRotationAngle(((PieRadarChartBase) this.mChart).getRotationAngle() + (this.mDecelerationAngularVelocity * (((float) (currentTime - this.mDecelerationLastTime)) / 1000.0f)));
            this.mDecelerationLastTime = currentTime;
            if (((double) Math.abs(this.mDecelerationAngularVelocity)) >= 0.001d) {
                Utils.postInvalidateOnAnimation(this.mChart);
            } else {
                stopDeceleration();
            }
        }
    }
}
