package com.example.happydance_java;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LocationUtils implements LifecycleObserver {
    private static final String TAG = "LocationUtils";
    private MapView mMapView;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;
    private AMapLocationClient mLocationClient;//声明AMapLocationClient对象
    private AMapLocationListener mLocationListenetr;//初始化定位
    private Context context;
    private OnMapEventListener onMapEventListener;

    public interface OnMapEventListener {
        void locationChecked(AMapLocation aMapLocation);

        void locationCheckFailed(AMapLocation aMapLocation);

        void onClick(LatLng latLng);

        void onLongClick(LatLng latLng);

        void onMarkerClick(Marker marker);
    }

    public LocationUtils(Context context, @NonNull MapView mMapView, OnMapEventListener listener) {
        this.context = context;
        this.mMapView = mMapView;
        this.onMapEventListener = listener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //初始化地图事件
        initMapEvent();

        setupArrowLocationAndStart();

        //初始化定位
        setupLocationAndStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        mMapView.onResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void pause() {
        mMapView.onPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void destroy() {
        mMapView.onDestroy();
    }


    /**
     * 初始化地图事件
     */
    private void initMapEvent() {
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (onMapEventListener != null) {
                    onMapEventListener.onClick(latLng);
                }
            }
        });

        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (onMapEventListener != null) {
                    onMapEventListener.onLongClick(latLng);
                }
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (onMapEventListener != null) {
                    onMapEventListener.onMarkerClick(marker);
                }
                return true;
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
        myLocationStyle.interval(10000);
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
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
     * 初始化箭头定位监听并开始定位
     */
    private void setupArrowLocationAndStart(){
        //构建定位蓝点的样式
        myLocationStyle = buildMyLocationStyle();

        //设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        //设置定位蓝点
        aMap.setMyLocationStyle(myLocationStyle);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认为false
        aMap.setMyLocationEnabled(true);
        //监听定位变化
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d(TAG, "onMyLocationChange() called with: location = [" + location + "]");
            }
        });
    }

    /**
     * 初始化定位监听并开始定位
     */
    private void setupLocationAndStart() {
        //声明定位回调监听器
        mLocationListenetr = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //获取定位结果
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        if (onMapEventListener != null) {
                            onMapEventListener.locationChecked(aMapLocation);
                        }
//                        //解析定位信息
//                        int locationType = aMapLocation.getLocationType();//获取定位来源，如网络定位
//                        double latitude = aMapLocation.getLatitude();//获取维度
//                        double longitude = aMapLocation.getLongitude();//获取经度
//                        float accuracy = aMapLocation.getAccuracy();//获取精度信息
//                        /*
//                         * 获取地址
//                         * 如果option中设置isNeedAddress为false，则没有此结果
//                         * 网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                         */
//                        String address = aMapLocation.getAddress();
//                        String country = aMapLocation.getCountry();//国家信息
//                        String province = aMapLocation.getProvince();//省份信息
//                        String city = aMapLocation.getCity();//城市信息
//                        String district = aMapLocation.getDistrict();//城区信息
//                        String street = aMapLocation.getStreet();//街道信息
//                        String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
//                        String cityCode = aMapLocation.getCityCode();//城市编码
//                        String adCode = aMapLocation.getAdCode();//地区编码
//                        String aoiName = aMapLocation.getAoiName();//获取当前定位的AOI信息
//                        String buildingId = aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                        String floor = aMapLocation.getFloor();//获取当前室内定位的楼层
//                        int gpsAccuracyStatus = aMapLocation.getGpsAccuracyStatus();//获取当前的GPS状态
//                        //获取定位时间
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(aMapLocation.getTime());
//                        String timeStr = df.format(date);
//                        Log.d(TAG, "****************************************");
//                        Log.d(TAG, "onLocationChanged: \n"
//                                + "locationType: " + locationType + "\n"
//                                + "latitude: " + latitude + "\n"
//                                + "longitude: " + longitude + "\n"
//                                + "accuracy: " + accuracy + "\n"
//                                + "address: " + address + "\n"
//                                + "country: " + country + "\n"
//                                + "province: " + province + "\n"
//                                + "city: " + city + "\n"
//                                + "district: " + district + "\n"
//                                + "street: " + street + "\n"
//                                + "streetNum: " + streetNum + "\n"
//                                + "cityCode: " + cityCode + "\n"
//                                + "adCode: " + adCode + "\n"
//                                + "aoiName: " + aoiName + "\n"
//                                + "buildingId: " + buildingId + "\n"
//                                + "floor: " + floor + "\n"
//                                + "gpsAccuracyStatus: " + gpsAccuracyStatus + "\n"
//                                + "timeStr: " + timeStr + "\n"
//                        );

                    } else {
                        if (onMapEventListener != null) {
                            onMapEventListener.locationCheckFailed(aMapLocation);
                        }
//                        //定位失败时可以通过ErrCode来确定失败的原因，errInfo是错误信息
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + aMapLocation.getErrorCode() + ", errInfo:"
//                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListenetr);
        //声明并出初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        /*
         * 选择定位场景
         * 目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        /*
         * 选择定位模式
         * 1.高精度定位模式：同时使用网络定位和GPS定位，优先返回最高的定位结果，以及对应的地址描述信息
         * 2.低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（WI-FI和基站定位）
         * 3.仅用设备定位模式：不需要连接网络，只使用GPS进行定位这种模式不支持室内环境定位，需要到室外
         */
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //获取一次定位结果，该方法默认为false
        mLocationOption.setOnceLocation(false);
        //获取最近3s内精度最高的一次定位结果
        //设置setOnceLocationLatest(boolean)方法为true，启动定位时会返回最近3s内精度最高的一次定位结果
        //且setOnceLocation(boolean)设置为true，反之不会，默认为false
        mLocationOption.setOnceLocationLatest(true);
        //自定义连续定位，设置时间间隔，单位毫秒，默认为2000ms，最低为1000ms
        mLocationOption.setInterval(5000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置，默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        /*
         * 设置请求超时时间，默认为30s,建议超时时间不要低于8000毫秒
         * 如果单次请求超时，定位随机终止
         * 连续定位状态下这一次定位返回超时，但按照既定周期定位下一次继续进行
         */
        mLocationOption.setHttpTimeOut(20000);
        /*
         * 是否开启缓存机制
         * 默认开启，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存
         * 不区分单次定位还是连续定位，GPS定位结果不会被缓存
         */
        mLocationOption.setLocationCacheEnable(true);

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            mLocationClient.stopLocation();
            mLocationClient.startLocation();

            //停止定位
//            mLocationClient.stopLocation();//停止定位后，本地定位服务不会被销毁
            /*
             * 销毁定位客户端
             * 销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象
             */
//            mLocationClient.onDestroy();
        }

    }
}
