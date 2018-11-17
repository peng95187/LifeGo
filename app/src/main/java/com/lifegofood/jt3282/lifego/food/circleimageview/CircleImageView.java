package com.lifegofood.jt3282.lifego.food.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;

import com.lifegofood.jt3282.lifego.R;


/**
 * Created by jt3282 on 2017/12/13.
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;

    private int borderWidth = DEFAULT_BORDER_WIDTH;
    private int borderColor = DEFAULT_BORDER_COLOR;

    private Paint paint;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_border_corlor, DEFAULT_BORDER_COLOR);
        typedArray.recycle();

        init();
    }
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }
    private Bitmap squareBitmap(Bitmap bitmap) {
        Bitmap squareBitmap = null;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int squareSide = 0;
        int x = 0, y = 0;
        if(bitmapHeight > bitmapWidth) {
            squareSide = bitmapWidth;
            y = (bitmapHeight - bitmapWidth);
            squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareSide, squareSide);
        }
        else if(bitmapHeight < bitmapWidth) {
            squareSide = bitmapHeight;
            x = (bitmapWidth - bitmapHeight);
            squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareSide, squareSide);
        }
        else {
            squareBitmap = bitmap;
        }

        return squareBitmap;
    }
    private Bitmap circleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Rect rect = new Rect(0, 0, width, height);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawCircle(width / 2.0f, height / 2.0f, width / 2.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return circleBitmap;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        if (drawable.getClass() == NinePatchDrawable.class) {
            super.onDraw(canvas);
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap squareBitmap = squareBitmap(bitmap);

        // 处理padding
        int defaultWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int defaultHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int scaleSize = Math.min(defaultWidth, defaultHeight);
        float top = getPaddingTop() + (defaultHeight - scaleSize) / 2.0f;
        float left = getPaddingLeft() + (defaultWidth - scaleSize) / 2.0f;

        // temp 用来处理边框与图像的边缘
        int temp = 0;
        if(borderWidth > 0)
            temp = 1;
        bitmap = Bitmap.createScaledBitmap(squareBitmap, scaleSize - borderWidth * 2 + temp, scaleSize - borderWidth * 2 + temp, true);
        Bitmap circleBitmap = circleBitmap(bitmap);
        canvas.drawBitmap(circleBitmap, borderWidth - temp / 2.0f + left, borderWidth - temp / 2.0f + top, paint);
        drawCircleBorder(canvas, scaleSize / 2.0f + left, scaleSize / 2.0f + top, scaleSize / 2.0f);
    }
    private void drawCircleBorder(Canvas canvas, float x, float y, float radius) {
        if(borderWidth <= 0)
            return;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(x, y, radius - borderWidth / 2.0f, paint);
    }
}
