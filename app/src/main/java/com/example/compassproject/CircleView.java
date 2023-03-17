package com.example.compassproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class CircleView extends View {
    private Paint paint;
    private int radius;
    private int index;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Create red circle
    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        radius = 20;
    }

    // Define size
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = radius * 2;
        int width = resolveSize(size, widthMeasureSpec);
        int height = resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    // Draw circle on canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        canvas.drawCircle(x, y, radius, paint);
    }

    // Set radius of circle
    public void setRadius(int radius) {
        this.radius = radius;
    }

    // Set index of circle
    public void setIndex(int index)
    {
        this.index = index;
    }

    // Get index of circle
    public int getIndex()
    {
        return this.index;
    }

    // Set color of circle
    public void setColor(int color){
        this.paint.setColor(color);
    }

    // Get color of circle
    public int getColor(){
        return this.paint.getColor();
    }
}