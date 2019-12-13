package com.ds.sapling.butterknifeds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;


/**
 * @author ly
 * @version V1.0
 * @Package com.blueteam.ganjiuhui.myview.stringStyle
 * @Description: 自定义圆角背景Span
 * @date 2017/1/19 16:34
 */
public class RadiusBackgroundV2Span extends ReplacementSpan {
    private int mSize;
    private int mBgColor;
    private int mRadius;
    private int mTxtColor;
    private float mTxtSize;
    private Context mContext;
    private Style mTxtStyle;
    private int mMargin;

    private Paint paintBorder;

    /**
     * @param context
     * @param txtColor 字体颜色
     * @param bgColor  背景颜色
     * @param radius   圆角
     * @param txtSize  字体大小
     * @param margin   左右Margin大小
     * @param style    字体样式
     */
    public RadiusBackgroundV2Span(Context context, int txtColor, int bgColor, int radius, float txtSize, int margin, Style style) {
        this.mContext = context;
        mBgColor = bgColor;
        mTxtColor = txtColor;
        mRadius = radius;
        mTxtStyle = style;
        mTxtSize = txtSize;
        this.mMargin = margin;

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Style.STROKE);
        paintBorder.setStrokeWidth(dip2px(context,1));
        paintBorder.setColor(Color.parseColor("#FF0000"));
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.setTextSize((TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, mTxtSize, mContext.getResources().getDisplayMetrics())));
        mSize = (int) (paint.measureText(text, start, end)) + dip2px(mContext, mMargin) * 2;
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int color = paint.getColor();//保存文字颜色
        paint.setColor(mBgColor);//设置背景颜色
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, mTxtSize, mContext.getResources().getDisplayMetrics()));
        paint.setStyle(mTxtStyle);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int margin = dip2px(mContext, mMargin);
        RectF oval = new RectF(x, fontMetrics.descent / 2, x + mSize, y + fontMetrics.descent);
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径

        canvas.drawRoundRect(oval, mRadius, mRadius,paintBorder);

        paint.setColor(mTxtColor);//恢复画笔的文字颜色
        paint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, mTxtSize, mContext.getResources().getDisplayMetrics()));
        paint.setTextAlign(Paint.Align.CENTER);

        final int textCenterX = (mSize) / 2;
        int baseline = (int) (oval.top + (oval.bottom - oval.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
//        int textBaselineY = (y - fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent;
        canvas.drawText(text, start, end,x+textCenterX, baseline, paint);//绘制文字

    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
