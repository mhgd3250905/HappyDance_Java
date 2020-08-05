package com.example.happydance_java;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mMapView;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreated的时候执行onCreate方法
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        myLocationStyle = buildMyLocationStyle();

        //设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        //设置定位蓝点
        aMap.setMyLocationStyle(myLocationStyle);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认为false
        aMap.setMyLocationEnabled(true);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d(TAG, "onMyLocationChange() called with: location = [" + location + "]");
            }
        });

    }

    /**
     * 构建MyLocationStyle
     *
     * @return
     */
    private MyLocationStyle buildMyLocationStyle() {
        //初始化定位蓝点的样式
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置持续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效
        myLocationStyle.interval(2000);
        /**
         * 设置定位类型
         * LOCATION_TYPE_SHOW = 0; //只定位一次
         * LOCATION_TYPE_LOCATE = 1; //定位一次，并且将视角移动到地图中心
         * LOCATION_TYPE_FOLLOW = 2; //连续定位，并且将视角移动到地图中心，定位蓝点跟随设备移动（1秒一次定位）
         * LOCATION_TYPE_MAP_ROTATE = 3; //连续定位、切将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动
         * LOCATION_TYPE_LOCATION_ROTATE = 4; //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且跟随设备移动
         * LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER=5; //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且跟随设备移动
         * LOCATION_TYPE_FOLLOW_NO_CENTER = 6; //连续定位，蓝点不会移动到地图中心点，并且蓝点会跟随设备移动
         * LOCATION_TYPE_MAP_ROTATE_NO_CENTER = 7; //连续定位，蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动
         */
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //控制是否显示定位蓝点，注意是在5.1.0之后支持
        myLocationStyle.showMyLocation(true);
        //自定义定位蓝点的图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round);
        myLocationStyle.myLocationIcon(bitmapDescriptor);
        //自定义定位蓝点图的锚点:默认（0。5f,0.5f）
        myLocationStyle.anchor(0.5f, 0.5f);
        //自定义精度圈 边框颜色
        myLocationStyle.strokeColor(Color.BLUE);
        //自定义精度圈 边框宽度
        myLocationStyle.strokeWidth(5.0f);
        //自定义精度圈 填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        return myLocationStyle;
    }


    /**
     * 定位的监听回调
     */
    private void initLocationListener() {
//初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.d(TAG, "onLocationChanged: ********************************");
                Log.d(TAG, "aMapLocation:" + aMapLocation.getAddress());
                //获取纬度
                Log.d(TAG, "aMapLocation:" + aMapLocation.getLatitude());
                Log.d(TAG, "aMapLocation:" + aMapLocation.getLongitude());
                Log.d(TAG, "aMapLocation==null:" + (aMapLocation == null));
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        Log.d(TAG, "aMapLocation:" + aMapLocation.getPoiName());
//                        current_latitude = aMapLocation.getLatitude();
//                        current_longitude = aMapLocation.getLongitude();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.d(TAG, "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }
}
