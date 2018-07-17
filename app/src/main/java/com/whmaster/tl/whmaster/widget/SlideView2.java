package com.whmaster.tl.whmaster.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.whmaster.tl.whmaster.R;

/**
 * Created by admin on 2017/12/27.
 */

public class SlideView2 extends View{
    private Bitmap mLockBitmap,mSuccessBitmap;
    private int mLockDrawableId,mSuccessDrawableId;
    private Paint mPaint;
    private int mLockRadius;
    private String mTipText,mSuccessText;
    private int mTipsTextSize;
    private int mTipsTextColor;
    private Rect mTipsTextRect = new Rect();
    private Context mContext;
    private float mLocationX;
    private boolean mIsDragable = false,isSuccess = false;
    private onSuccessInterface mOnSuccessListener;

    public SlideView2(Context context) {
        this(context, null);

    }

    public SlideView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.SlideLockView, defStyleAttr, 0);
        mSuccessDrawableId = R.drawable.ic_dg;
        mLockDrawableId = tp.getResourceId(R.styleable.SlideLockView_lock_drawable, -1);
        mLockRadius = tp.getDimensionPixelOffset(R.styleable.SlideLockView_lock_radius, 1);
        mTipText = tp.getString(R.styleable.SlideLockView_lock_tips_tx);
        mSuccessText = tp.getString(R.styleable.SlideLockView_lock_success);
        mTipsTextSize = tp.getDimensionPixelOffset(R.styleable.SlideLockView_locl_tips_tx_size,12);
        mTipsTextColor = tp.getColor(R.styleable.SlideLockView_lock_tips_tx_color, Color.BLACK);
        mContext = context;
        tp.recycle();
        init(context);
    }
    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTipsTextSize);
        mPaint.setColor(mTipsTextColor);
        mSuccessBitmap = BitmapFactory.decodeResource(context.getResources(), mSuccessDrawableId);
        mLockBitmap = BitmapFactory.decodeResource(context.getResources(), mLockDrawableId);
        int oldSize = mLockBitmap.getHeight();
        int newSize = mLockRadius * 2;
        float scale = (float) (newSize * 1.0f / oldSize/2.5);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        mLockBitmap = Bitmap.createBitmap(mLockBitmap, 0, 0, oldSize, oldSize, matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(mTipsTextRect);
        int cHeight = mTipsTextRect.height();
        int cWidth = mTipsTextRect.width();
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTipsTextRect);
        float x = cWidth / 2f - mTipsTextRect.width() / 2f - mTipsTextRect.left;
        float y = cHeight / 2f + mTipsTextRect.height() / 2f - mTipsTextRect.bottom;
        mPaint.setColor(Color.WHITE);
        canvas.drawText(mTipText, x, y, mPaint);

        int rightMax = getWidth() - mLockRadius * 2;


        RectF oval=new RectF();                     //RectF对象
        oval.left=0;                              //左边
        oval.top=0;                                   //上边
        oval.right=180;                             //右边
        oval.bottom=105;
        mPaint.setColor(mContext.getResources().getColor(R.color.title_back_color));
//        Log.i("com.whmaster.tl.whmaster>>",mLocationX+"=======");
        if (mLocationX < 0) {
            oval.right=180;
            canvas.drawRoundRect(oval,10,10,mPaint);
            canvas.drawBitmap(mLockBitmap, 55, 30, mPaint);
        } else if (mLocationX > rightMax) {
            oval.right=120+rightMax;
            canvas.drawRoundRect(oval,10,10,mPaint);
            if(mLocationX==680.0){
                canvas.drawBitmap(mSuccessBitmap, rightMax+45, 30, mPaint);
                mPaint.setColor(Color.WHITE);
                canvas.drawText(mSuccessText, x, y, mPaint);
            }
        } else {
            oval.right=180+mLocationX;
            canvas.drawRoundRect(oval,10,10,mPaint);
            canvas.drawBitmap(mLockBitmap, mLocationX+65, 30, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float xPos = event.getX();
                float yPos = event.getY();
//                if (isTouchLock(xPos, yPos)) {
                    mLocationX = xPos - mLockRadius;
                    mIsDragable = true;
                    invalidate();
//                } else {
//                    mIsDragable = false;
//                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                if (!mIsDragable) return true;

                int rightMax = getWidth() - mLockRadius * 2;
                resetLocationX(event.getX(),rightMax);
                invalidate();

                return true;
            }
            case MotionEvent.ACTION_UP: {
                if(mLocationX >= 250){
                    openSuccess();
                }else{
                    resetLock();
                }
                break;
            }
        }
        return super.onTouchEvent(event);

    }
    private void openSuccess(){
        isSuccess = true;
        ValueAnimator anim = ValueAnimator.ofFloat(mLocationX,getWidth());
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLocationX = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
        mOnSuccessListener.onExcute();
    }

    public void setOnSuccessListener(onSuccessInterface onSuccessListener){
        if(onSuccessListener!=null){
            this.mOnSuccessListener = onSuccessListener;
        }
    }

    public void resetLock(){
        ValueAnimator anim = ValueAnimator.ofFloat(mLocationX,0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLocationX = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();

    }
    public void resetXy(){
        mLocationX = 0;
        invalidate();
    }
    public void resetLocationX(float eventXPos,float rightMax){
        float xPos = eventXPos;
        mLocationX = xPos - mLockRadius;
        if (mLocationX < 0) {
            mLocationX = 0;
        }else if (mLocationX >= rightMax) {
            mLocationX = rightMax;
        }
    }
    private boolean isTouchLock(float xPos, float yPox) {
        float centerX = mLocationX + mLockRadius;
        float diffX = xPos - centerX;
        float diffY = yPox - mLockRadius;

        return diffX * diffX + diffY * diffY < mLockRadius * mLockRadius;
    }

    public interface onSuccessInterface{
        void onExcute();
    }
}
