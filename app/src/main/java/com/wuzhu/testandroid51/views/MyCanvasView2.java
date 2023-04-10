package com.wuzhu.testandroid51.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.testandroid51.R;

/**
 * @author Hdq on 2022/11/17.
 */
public class MyCanvasView2 extends View {
    private static final String TAG = "MyCanvasView";
    private Paint paint;
    private RectF rect;
    private Bitmap bm;
    private int saveCount;
    public MyCanvasView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
//        paint.setXfermode(xfermode);

        rect = new RectF(0, 0, 600, 600);
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_image);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());// 1

        canvas.drawColor(Color.GREEN);
        saveCount = canvas.saveLayer(rect, paint, Canvas.ALL_SAVE_FLAG);
        Log.d(TAG, "onDraw: " + canvas.getSaveCount() + "  " + saveCount);
        canvas.drawColor(0x3FFF0000);
        canvas.drawBitmap(bm, 200, 200, paint);
        canvas.restore();

        canvas.drawBitmap(bm, 400, 400, paint);


        paint.setColor(Color.BLUE);
        canvas.drawCircle(100,100,50,paint);
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());// 1

    }
}
