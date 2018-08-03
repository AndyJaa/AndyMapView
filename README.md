# AndyMapView
根据接口（或者其他途径）提供的坐标点，在图片上加标注，并且实现点击事件，弹出提示信息。

USE
---------------------------
Step 1. Add the JitPack repository to your build file:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
        
Step 2. Add the dependency:

dependencies {
	        compile 'com.github.AndyJaa:AndyMapView:v1.0.0'
	}

DEMO
---------------------------

        setContentView(R.layout.activity_main);
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
                PointData data1 = new PointData(new PointF(452, 325),"Shop1","这是店铺1的详细信息");
                PointData data2 = new PointData(new PointF(452,94),"Shop2","这是店铺2的详细信息");
                PointData data3 = new PointData(new PointF(723,97),"Shop3","这是店铺3的详细信息");
                List<PointData> list = new ArrayList<PointData>();
                list.add(data1);
                list.add(data2);
                list.add(data3);
                markLayer = new MarkLayer(mapView, list);
                markLayer.setDetailColor(Color.GREEN);
                markLayer.setTitleColor("#aabbccdd");
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

SCREENSHOT
------------------
![image](https://github.com/AndyJaa/AndyMapView/blob/master/demo.png)
