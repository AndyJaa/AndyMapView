package com.andy.jaa.layer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.andy.jaa.MapView;
import com.andy.jaa.PointData;
import com.andy.jaa.R;
import com.andy.jaa.utils.DialogUtils;
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
    private Context mContext;
    private boolean isDefault = true;
    private boolean isShowPointName = true;


    public MarkLayer(Context context, MapView mapView, List<PointData> pointDatas, boolean isShowPointName, boolean isDefault) {
        super(mapView);
        this.pointDatas = pointDatas;
        this.mContext = context;
        this.isShowPointName = isShowPointName;
        this.isDefault = isDefault;
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
                    final String markName = pointDatas.get(i).getMarkTitle();
                    final String markContent = pointDatas.get(i).getMarkContent();
                    //mark name
                    if (isShowPointName) {
                        if (mapView.getCurrentZoom() > 1.0 && pointDatas != null) {
                            canvas.drawText(markName, goal[0] - radiusMark, goal[1] -
                                    radiusMark / 2, paint);
                        }
                    }
                    //mark ico
//                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
                    canvas.drawBitmap(bmpMark, goal[0] - bmpMark.getWidth() / 2,
                            goal[1] - bmpMark.getHeight(), paint);
                    if (i == num && isClickMark) {
                        if (isDefault) {
//                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                            final Bitmap tmpBitmap = drawTextToBitmap(
                                    bmpMarkTouch, markName, markContent);
                            if (tmpBitmap != null) {
                                canvas.drawBitmap(tmpBitmap, goal[0] - tmpBitmap.getWidth() / 2,
                                        goal[1] - tmpBitmap.getHeight(), paint);
                            }
                        } else {
//                            final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_dialog,null);
//                            final Dialog dialog = new AlertDialog.Builder(mContext)
//                                    .setView(view)
//                                    .setCancelable(false)
//                                    .create();
//                            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();//获取dialog信息
////                            params.width = screenWidth - 20;
//                            params.height = creenHeight / 3 ;
//                            dialog.getWindow().setAttributes(params);//设置大小
//
//                            ((TextView) view.findViewById(R.id.tv_showTitle)).setText(markName);
//                            TextView tv_content = ((TextView) view.findViewById(R.id.tv_showContent));
////                            tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
//                            tv_content.setText(markContent);
//                            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (dialog != null && dialog.isShowing()) {
//                                        dialog.dismiss();
//                                        isClickMark = false;
//                                    }
//                                }
//                            });
//                            if (dialog!=null)dialog.show();

                            final Dialog dialog = new DialogUtils(mContext).getDialog();
                            if (dialog != null) {
                                dialog.show();
                                ((TextView) dialog.findViewById(R.id.tv_showTitle)).setText(markName);
                                TextView tv_content = ((TextView) dialog.findViewById(R.id.tv_showContent));
                                tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
                                tv_content.setText(markContent);
                                dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismiss();
                                            isClickMark = false;
                                        }
                                    }
                                });
                            }
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
        } catch (IllegalArgumentException e) {
        }
    }

    public void setDetailColor(@NonNull String color) {
        try {
            this.DETAILCOLOR = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
        }
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
