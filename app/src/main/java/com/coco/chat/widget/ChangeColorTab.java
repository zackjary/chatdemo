package com.coco.chat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.coco.chat.R;

/**
 * Created by coco.zhou on 2016/4/1.
 */
public class ChangeColorTab extends RelativeLayout{
    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private float mAlpha = 0.0f;
    private Bitmap mBitmapNormal;
    private Bitmap mBitmapSelected;
    private Rect mSrcRect, mDstRect;

    public ChangeColorTab(Context context) {
        this(context, null);
    }

    public ChangeColorTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorTab);
        int indexCount = ta.getIndexCount();
        BitmapDrawable drawable = null;
        for (int i = 0; i < indexCount; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorTab_icon_normal:
                    drawable = (BitmapDrawable) ta.getDrawable(attr);
                    mBitmapNormal = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorTab_icon_selected:
                    drawable = (BitmapDrawable) ta.getDrawable(attr);
                    mBitmapSelected = drawable.getBitmap();
                    break;
            }
        }
        ta.recycle();
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSrcRect = new Rect(0, 0, mBitmapNormal.getWidth(), mBitmapNormal.getHeight());
        float scale = Math.max(1.0f * mBitmapNormal.getHeight() / getMeasuredHeight(), 1.0f * mBitmapNormal.getWidth()
                / getMeasuredWidth());
        int w = (int) (mBitmapNormal.getWidth() / scale);
        int h = (int) (mBitmapNormal.getHeight() / scale);
        int left = (getMeasuredWidth() - w) / 2;
        int top = (getMeasuredHeight() - h) / 2;
        mDstRect = new Rect(left, top, left + w, top + h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawBitmap(mBitmapNormal, 0, 0, null);
        canvas.drawBitmap(mBitmapNormal, mSrcRect, mDstRect, null);

        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), (int) (mAlpha * 255), LAYER_FLAGS);

        // canvas.drawBitmap(mBitmapSelected, 0, 0, null);
        canvas.drawBitmap(mBitmapSelected, mSrcRect, mDstRect, null);
        canvas.restore();
    }

    public void setAlpha(float alpha) {
        if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("alpha must be between [0 - 1]");
        }
        mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    // Activity回收处理
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            // 恢复View的自身状态
            super.onRestoreInstanceState(((Bundle) state).getParcelable(INSTANCE_STATUS));
            // 恢复我们定义的状态
            mAlpha = ((Bundle) state).getFloat(STATUS_ALPHA);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // 保存View的自身状态
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        // 保存我们定义的状态
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }
}
