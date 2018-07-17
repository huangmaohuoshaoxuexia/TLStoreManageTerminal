package com.whmaster.tl.whmaster.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 2017/11/7.
 */

public class CircleText extends TextView {
    private Context mContext;
    /**
     * 画笔
     */
    private Paint mPaint;

    public CircleText(Context context) {
        super(context);
    }

    public CircleText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context,attrs);
    }

    public CircleText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint(context,attrs);
    }

    /**
     * 初始化画笔和自定义属性
     * @param context
     * @param attrs
     */
    private void initPaint(Context context,AttributeSet attrs){
        mContext = context;
        mPaint = new Paint();
    }

    /**
     * 调用onDraw绘制边框
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        mPaint.setAntiAlias(true);
            //画笔颜色
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
        //创建一个区域,限制圆弧范围
        RectF rectF = new RectF();
        //设置半径,比较长宽,取最大值
        int radius = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
        //设置Padding 不一致,绘制出的是椭圆;一致的是圆形
        rectF.set(getPaddingLeft(),getPaddingTop(),radius-getPaddingRight(),radius-getPaddingBottom());
        //绘制圆弧
        canvas.drawArc(rectF,0,360,false,mPaint);

        //最后调用super方法,解决文本被所绘制的圆圈背景锁覆盖的问题
        super.onDraw(canvas);
    }
}
