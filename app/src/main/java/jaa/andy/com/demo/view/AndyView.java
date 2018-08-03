package jaa.andy.com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jaa.andy.com.demo.R;

/**
 * Created by quanxi on 2018/7/24.
 */

public class AndyView extends View {
    private static final String TAG = AndyView.class.getSimpleName() + ">>>";
    private String mTextContent;
    private int mTextColor;
    private int mTextSize;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public interface OnClickAndyView {
        void clickAndyView();
    }

    OnClickAndyView clickListener;
    private int lastX;
    private int lastY;

    public void setAndyViewClick(OnClickAndyView clickListener) {
        this.clickListener = clickListener;
    }

    public AndyView(Context context) {
        this(context, null);
    }

    public AndyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        final TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.AndyView, defStyleAttr, 0);
        final int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            final int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.AndyView_titleTextContent:
                    mTextContent = array.getString(i);
                    break;
                case R.styleable.AndyView_titleTextColor:
                    mTextColor = array.getColor(i, Color.RED);
                    break;
                case R.styleable.AndyView_titleTextSize:
                    mTextSize = (int) array.getDimension(i, 16);
                    break;
            }
        }
        //记得回收
        array.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextContent = randomText();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(mTextContent, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;
        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int widSize = MeasureSpec.getSize(widthMeasureSpec);
        int heiMode = MeasureSpec.getMode(heightMeasureSpec);
        int heiSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widMode) {
            case MeasureSpec.EXACTLY:
                width = widSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mPaint.setTextSize(mTextSize);
                mPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mBound);
                float textWidth = mBound.width();
                int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
                width = desired;
                break;
        }

        switch (heiMode) {
            case MeasureSpec.EXACTLY:
                height = heiSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mPaint.setTextSize(mTextSize);
                mPaint.getTextBounds(mTextContent,0,mTextContent.length(),mBound);
                float textHeight = mBound.height();
                int desired = (int) (getPaddingTop()+textHeight+getPaddingBottom());
                height = desired;
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸点相对于父窗体
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //1、layout(getLeft()+offsetX,getTop()+offsetY,getRight()+offsetX,getBottom()+offsetY);

                //2、
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
            case MotionEvent.ACTION_UP:
                if ((x + getLeft() < getRight() && y + getTop() < getBottom())) {
                    if (clickListener != null) {
                        clickListener.clickAndyView();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }

}
