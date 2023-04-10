package com.wuzhu.testandroid51.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Hdq on 2022/11/17.
 */
public class MyCanvasView extends View {
    private static final String TAG = "MyCanvasView";
    private Paint paint;
    private Rect rect;
    public MyCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        rect = new Rect(10, 10, 400, 400);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());// 1
        canvas.drawRect(rect, paint);
        int count = canvas.save();
        Log.d(TAG, "onDraw count: " + count);// 1
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());// 2
        canvas.translate(100,100);
        paint.setColor(Color.BLUE);
        canvas.drawRect(rect, paint);
        canvas.restore();
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());// 1
        canvas.translate(150,150);
        paint.setColor(Color.GREEN);
        canvas.drawRect(rect, paint);

    }
}
