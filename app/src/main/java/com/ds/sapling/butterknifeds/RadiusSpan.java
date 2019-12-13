package com.ds.sapling.butterknifeds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;
/**
 *  作者 cral
 *  创建日期 2019/9/10
 *  爆款直降 span
 **/
public class RadiusSpan extends ReplacementSpan {
    private Context context;
    private float width;//绘制文字宽
    private Paint paint;
    private int textSize;//文字大小
    private int textColor;//文字颜色
    private int bgColor;//文字背景
    private int radius;//圆角
    private Paint paintRect;//矩形边框
    private int borderWidth;

    private int paddingLeft ;

    /**
     *
     * @param context
     * @param textSize 字体大小
     * @param textColor 字体颜色
     * @param bgColor 背景色
     * @param radius 圆角
     */
    public RadiusSpan(Context context, int textSize, int textColor, int bgColor, int radius) {
//        this(context,textSize,textColor,bgColor,radius,0,0);
        this(context,textSize,textColor,bgColor,radius, Color.parseColor("#FFA9A9"),0.5f);
    }

    /**
     *
     * @param context
     * @param textSize 字体大小
     * @param textColor 字体颜色
     * @param bgColor 背景色
     * @param radius 圆角
     * @param borderColor 边框颜色
     * @param borderWidth 边框宽度
     */
    public RadiusSpan(Context context, int textSize, int textColor, int bgColor, int radius,int borderColor,float borderWidth) {
        this.context = context;
        this.textSize = textSize;
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.radius = radius;
        this.borderWidth = dip2px(context,borderWidth);

        paddingLeft = dip2px(context,2);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if (borderColor != 0) {
            paintRect = new Paint();
            paintRect.setAntiAlias(true);
            paintRect.setStyle(Paint.Style.STROKE);
            paintRect.setColor(borderColor);
            paintRect.setStrokeWidth(this.borderWidth);
        }
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));
        width = paint.measureText(text, start, end) + paddingLeft*2;
        return (int) width;
    }
    /**
     * draw
     * @param text 完整文本
     * @param start setSpan里设置的start
     * @param end setSpan里设置的start
     * @param x
     * @param top 当前span所在行的上方y
     * @param y y其实就是metric里baseline的位置
     * @param bottom 当前span所在行的下方y(包含了行间距)，会和下一行的top重合
     * @param paint 使用此span的画笔
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setColor(bgColor);
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();

        RectF rectF = new RectF(0 + borderWidth,metrics.descent / 2 ,x + width,y + metrics.descent);
        canvas.drawRoundRect(rectF,radius,radius,paint);

        if (paintRect != null){
            RectF borderRect = new RectF(0 + borderWidth,metrics.descent / 2 ,x + width,y + metrics.descent);
            canvas.drawRoundRect(borderRect,radius,radius,paintRect);
        }

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(textColor);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));

        int textRectHeight = metrics.bottom - metrics.top;//文字区域高度
        int bgRectHeight = (int) (rectF.bottom - rectF.top);//背景区域高度

        int textY = (int) (rectF.top + (bgRectHeight - textRectHeight)/2 - metrics.top);
        canvas.drawText(text,start,end,width/2,textY,paint);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
