package jaa.andy.com.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import jaa.andy.com.demo.R;

/**
 * Created by quanxi on 2018/8/1.
 */

public class MyMapView extends SubsamplingScaleImageView {
    private int screenWid,screenHei;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+"map.jpg";

    public MyMapView(Context context) {
        this(context,null);
    }

    public MyMapView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHei = dm.heightPixels;
        screenWid = dm.widthPixels;
        int[] imgData = getImageWidthHeight(path);
        int imgWid = imgData[0];
        int imgHei = imgData[1];
        Log.e(">?>>>>>:",imgWid+","+imgHei+"-"+screenWid+","+screenHei);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap,961,984,new Paint());
    }

    public static int[] getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth,options.outHeight};
    }
}
