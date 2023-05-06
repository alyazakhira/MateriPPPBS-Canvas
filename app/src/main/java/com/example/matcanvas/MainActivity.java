package com.example.matcanvas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    // untuk menggambar
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
    private Bitmap mBitmap;
    private ImageView mImageView;

    // alat bantu buat bikin bentuk
    private Rect mRect = new Rect();
    private Rect mBounds = new Rect();

    //pembatas
    private static final int OFFSET = 120;
    private int mOffset = OFFSET;
    private static final int MULTIPLIER = 100;

    //color
    private int mColorBackground;
    private int mColorRectangle;
    private int mColorCircle;
    private int mColorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign color
        mColorBackground = ResourcesCompat.getColor(getResources(), R.color.colorBackground, null);
        mColorRectangle = ResourcesCompat.getColor(getResources(), R.color.colorRectangle, null);
        mColorCircle = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        mColorText = ResourcesCompat.getColor(getResources(), R.color.black, null);

        // setting paint
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(mColorText);
        mPaintText.setTextSize(70);

        // assign image view
        mImageView = findViewById(R.id.my_image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawSomething(view);
            }
        });
    }

    public void drawSomething(View view){
        // ambil lebar dan tinggi dari view
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();

        // ambil separuh lebar & tinggi
        int halfWidth = vWidth/2;
        int halfHeight = vHeight/2;

        // memulai draw
        if (mOffset == OFFSET) {
            mBitmap = Bitmap.createBitmap(vWidth,vHeight,Bitmap.Config.ARGB_8888);
            mImageView.setImageBitmap(mBitmap);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(mColorBackground);

            mCanvas.drawText(getString(R.string.keep_tapping), 100,100,mPaintText);
        } else {
            // buat kotak kalau lebar + tinggi kurang dari separuh
            // kalau ga ya buat lingkaran
            if (mOffset < halfWidth && mOffset < halfHeight) {
                mPaint.setColor(mColorRectangle - MULTIPLIER*mOffset);
                mRect.set(mOffset, mOffset, vWidth-mOffset,  vHeight-mOffset);
                mCanvas.drawRect(mRect,mPaint);
            } else {
                // draw circle + coloring
                mPaint.setColor(mColorCircle - MULTIPLIER*mOffset);
                mCanvas.drawCircle(halfWidth, halfHeight , halfHeight/3, mPaint);

                // set text to center of the circle
                String text = getString(R.string.done);
                mPaintText.getTextBounds(text, 0, text.length(), mBounds);
                int x = halfWidth - mBounds.centerX();
                int y = halfHeight - mBounds.centerY();
                mCanvas.drawText(text, x, y, mPaintText);

                // draw triangle; draw point, make a line from point to another
                mPaint.setColor(mColorBackground - MULTIPLIER*mOffset);
                Point a = new Point(halfWidth-50, halfHeight-50);
                Point b = new Point(halfWidth+50, halfHeight-50);
                Point c = new Point(halfWidth, halfHeight+250);
                Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.lineTo(a.x, a.y);
                path.lineTo(b.x, b.y);
                path.lineTo(c.x, c.y);
                path.lineTo(a.x, a.y);
                path.close();

                mCanvas.drawPath(path, mPaint);
            }

        }

        mOffset += OFFSET;
        view.invalidate();
    }
}