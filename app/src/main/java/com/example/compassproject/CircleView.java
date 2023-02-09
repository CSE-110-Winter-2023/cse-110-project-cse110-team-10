package com.example.compassproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CircleView extends View {

    private Paint paint;
    private int color = Color.BLACK;

    public CircleView(Context context, int color) {
        super(context);
        this.color = color;

        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        canvas.drawCircle(width/2, height/2, radius, paint);
    }

    public void setColor(int c) {
        color = c;
        paint.setColor(c);
        invalidate();
    }

}
