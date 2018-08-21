package com.andy.jaa.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;

import com.andy.jaa.MapView;
import com.andy.jaa.PointData;
import com.andy.jaa.R;
import com.andy.jaa.utils.MapMath;

import java.util.List;

/**
 * MarkLayer
 *
 * @author: andyJaa
 */
public class MarkLayer extends MapBaseLayer {
    private static final String TAG = MarkLayer.class.getSimpleName() + ">>";
    private float DETAILTEXTSIZE = 40f;
    private float TITLETEXTSIZE = 45f;
    private int TITLECLOR = Color.BLUE;
    private int DETAILCOLOR = Color.BLUE;

    private MarkIsClickListener listener;
    private List<PointData> pointDatas;
    private Bitmap bmpMark, bmpMarkTouch;

    private float radiusMark;
    private boolean isClickMark = false;
    private int num = -1;

    private Paint paint;


    public MarkLayer(MapView mapView, List<PointData> pointDatas) {
        super(mapView);
        this.pointDatas = pointDatas;
        initLayer();
    }

    private void initLayer() {
        radiusMark = setValue(10f);

        bmpMark = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.mark);
        bmpMarkTouch = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.mark_touch);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (pointDatas != null) {
            if (!pointDatas.isEmpty()) {
                float[] goal = mapView.convertMapXYToScreenXY(event.getX(), event.getY());
                for (int i = 0; i < pointDatas.size(); i++) {
                    final PointData data = pointDatas.get(i);
                    final PointF pointF = data.getPointF();
                    if (MapMath.getDistanceBetweenTwoPoints(goal[0], goal[1],
                            pointF.x - bmpMark.getWidth() / 2, pointF.y - bmpMark
                                    .getHeight() / 2) <= 50) {
                        num = i;
                        isClickMark = true;
                        break;
                    }

                    if (i == pointDatas.size() - 1) {
                        isClickMark = false;
                    }
                }
            }

            if (listener != null && isClickMark) {
                listener.markIsClick(num);
                mapView.refresh();
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        if (isVisible && pointDatas != null) {
            canvas.save();
            if (!pointDatas.isEmpty()) {
                for (int i = 0; i < pointDatas.size(); i++) {
                    final PointF mark = pointDatas.get(i).getPointF();
                    float[] goal = {mark.x, mark.y};
                    currentMatrix.mapPoints(goal);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(radiusMark);
                    //mark name
                    final String markName = pointDatas.get(i).getMarkTitle();
                    final String markContent = pointDatas.get(i).getMarkContent();
                    if (mapView.getCurrentZoom() > 1.0 && pointDatas != null) {
                        canvas.drawText(markName, goal[0] - radiusMark, goal[1] -
                                radiusMark / 2, paint);
                    }
                    //mark ico
//                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
                    canvas.drawBitmap(bmpMark, goal[0] - bmpMark.getWidth() / 2,
                            goal[1] - bmpMark.getHeight() , paint);
                    if (i == num && isClickMark) {
//                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        final Bitmap tmpBitmap = drawTextToBitmap(
                                bmpMarkTouch, markName, markContent);
                        if (tmpBitmap != null) {
                            canvas.drawBitmap(tmpBitmap, goal[0] - tmpBitmap.getWidth() / 2,
                                    goal[1] - tmpBitmap.getHeight(), paint);
                        }
                    }
                }
            }
            canvas.restore();
        }
    }

    public void setTitleColor(@ColorInt int color) {
        this.TITLECLOR = color;
    }

    public void setDetailColor(@ColorInt int color) {
        this.DETAILCOLOR = color;
    }

    public void setTitleColor(@NonNull String color) {
        try {
            this.TITLECLOR = Color.parseColor(color);
        }catch (IllegalArgumentException e){}
    }

    public void setDetailColor(@NonNull String color) {
        try {
            this.DETAILCOLOR = Color.parseColor(color);
        }catch (IllegalArgumentException e){}
    }

    public void setTitleTextSize(float size) {
        this.TITLETEXTSIZE = size;
    }

    public void setDetailTextSize(float size) {
        this.DETAILTEXTSIZE = size;
    }

    public boolean isClickMark() {
        return isClickMark;
    }

    public void setMarkIsClickListener(MarkIsClickListener listener) {
        this.listener = listener;
    }

    public interface MarkIsClickListener {
        void markIsClick(int num);
    }

    /**
     * @param bitmap
     * @param mText
     * @param detail
     * @return
     */
    public Bitmap drawTextToBitmap(Bitmap bitmap, String mText, String detail) {
        try {
            float scale = mapView.getResources().getDisplayMetrics().density;
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            paint.setDither(true); //获取跟清晰的图像采样
            paint.setFilterBitmap(true);//过滤一些
            paint.setColor(TITLECLOR);
//            paint.setTextSize((int) (2 * scale));
            paint.setTextSize(TITLETEXTSIZE);
//            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);//阴影制作半径，x偏移量，y偏移量，阴影颜色
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            final int bitHei = bitmap.getHeight();
            final int bitWid = bitmap.getWidth();
            int x = 5;
            canvas.drawText(mText, x * scale, bitHei / 4, paint);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(DETAILCOLOR);
            textPaint.setTextSize(DETAILTEXTSIZE);
            //可自动换行layout
            StaticLayout layout = new StaticLayout(detail, textPaint, bitWid, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            canvas.translate(x, bitHei / 3);
            layout.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "失败" + e.toString());
            return null;
        }
    }
}
