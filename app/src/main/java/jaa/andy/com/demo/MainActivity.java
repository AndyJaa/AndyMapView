package jaa.andy.com.demo;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.andy.jaa.MapView;
import com.andy.jaa.MapViewListener;
import com.andy.jaa.PointData;
import com.andy.jaa.layer.MarkLayer;
import com.andy.jaa.utils.DialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaa.andy.com.demo.view.AndyView;
import jaa.andy.com.demo.view.MyMapView;

public class MainActivity extends AppCompatActivity implements AndyView.OnClickAndyView {
    private static final String TAG = MainActivity.class.getSimpleName()+">>>";
    private AndyView andyView;
    private MyMapView imageView;
    private MapView mapView;
    private MarkLayer markLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        andyView = (AndyView) findViewById(R.id.andyView);
//        andyView.setAndyViewClick(this);
//        imageView = findViewById(R.id.imageView);
//
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int action = event.getAction();
//                switch (action){
//                    case MotionEvent.ACTION_DOWN:
//                        int x = (int) event.getX();
//                        int y = (int) event.getY();
//                        Log.e(TAG,x+","+y);
//                        int rawX = (int) event.getRawX();
//                        int rawY = (int) event.getRawY();
//                        Log.e(TAG,rawX+","+rawY);
//                        break;
//                }
//                return false;
//            }
//        });

        mapView = (MapView) findViewById(R.id.mapView);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("u2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.loadMap(bitmap);
        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onMapLoadSuccess() {
                PointData data1 = new PointData(new PointF(452, 325),"Shop1","这是是店铺1的详细信息");
                PointData data2 = new PointData(new PointF(452,94),"Shop2","这是店铺2的详细信息");
                PointData data3 = new PointData(new PointF(723,97),"Shop3","这是店铺3的详细信息");
                final List<PointData> list = new ArrayList<PointData>();
                list.add(data1);
                list.add(data2);
                list.add(data3);
                //默认样式
//                markLayer = new MarkLayer(MainActivity.this,mapView, list,true);
                //dialog样式弹出
                markLayer = new MarkLayer(MainActivity.this,mapView, list,false,false);
//                markLayer.setDetailColor(Color.GREEN);
//                markLayer.setTitleColor("#aabbccdd");
                markLayer.setMarkIsClickListener(new MarkLayer.MarkIsClickListener() {
                    @Override
                    public void markIsClick(int num) {
                    }
                });
                mapView.addLayer(markLayer);
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {

            }

        });
    }

    @Override
    public void clickAndyView() {

    }

}
