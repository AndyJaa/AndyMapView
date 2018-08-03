package com.andy.jaa.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.andy.jaa.MapView;

/**
 * MapLayer
 *
 * @author: andyJaa
 */
public class MapLayer extends MapBaseLayer {

    private static final String TAG = MapLayer.class.getSimpleName();

    private Picture image;
    private boolean hasMeasured;

    public MapLayer(MapView mapView) {
        super(mapView);
        level = MAP_LEVEL;
    }

    public void setImage(Picture image) {
        this.image = image;

        if (mapView.getWidth() == 0) {
            ViewTreeObserver vto = mapView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (!hasMeasured) {
                        initMapLayer();
                        hasMeasured = true;
                    }
                    return true;
                }
            });
        } else {
            initMapLayer();
        }
    }


    /**
     * init map image layer
     */
    private void initMapLayer() {
        final int mapViewWid = mapView.getWidth();
        final int mapViewHei = mapView.getHeight();
        final int imageWid = image.getWidth();
        final int imageHei = image.getHeight();
        //计算合适的缩放比
        float zoom = getInitZoom(mapViewWid, mapViewHei, imageWid, imageHei);
        Log.i(TAG, Float.toString(zoom));
        mapView.setCurrentZoom(zoom, 0, 0);

        float width = mapViewWid - zoom * imageWid;
        float height = mapViewHei - zoom * imageHei;

        mapView.translate(width / 2, height / 2);
    }

    /**
     * calculate init zoom
     *
     * @param viewWidth
     * @param viewHeight
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    private float getInitZoom(float viewWidth, float viewHeight, float imageWidth,
                              float imageHeight) {
        float widthRatio = viewWidth / imageWidth;
        float heightRatio = viewHeight / imageHeight;

        Log.i(TAG, "widthRatio:" + widthRatio);
        Log.i(TAG, "widthRatio:" + heightRatio);

        if (widthRatio * imageHeight <= viewHeight) {
            return widthRatio;
        } else if (heightRatio * imageWidth <= viewWidth) {
            return heightRatio;
        }
        return 0;
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        canvas.save();
        canvas.setMatrix(currentMatrix);
        if (image != null) {
            canvas.drawPicture(image);
        }
        canvas.restore();
    }

    public Picture getImage() {
        return image;
    }
}
