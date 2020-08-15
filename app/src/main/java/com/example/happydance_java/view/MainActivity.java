package com.example.happydance_java.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.example.happydance_java.LocationUtils;
import com.example.happydance_java.PosBean;
import com.example.happydance_java.R;
import com.example.happydance_java.databinding.ActivityMainBinding;
import com.example.happydance_java.model.viewmodel.MapViewModel;
import com.example.happydance_java.utils.SizeUtils;
import com.example.happydance_java.view.customize.FontIconView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mMapView;
    private MapViewModel viewModel;
    private AMap aMap;
    private EventListener eventListener;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MapViewModel.class);
        binding.setMapInfo(viewModel);
        eventListener = new EventListener();
        binding.setListener(eventListener);
        binding.setLifecycleOwner(this);


        //获取地图控件引用
        mMapView = binding.map;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //在activity执行onCreated的时候执行onCreate方法
        mMapView.onCreate(savedInstanceState);

        getLifecycle().addObserver(new LocationUtils(getApplicationContext(), mMapView, new LocationUtils.OnMapEventListener() {
            @Override
            public void locationChecked(AMapLocation aMapLocation) {
                double altitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                viewModel.getPosData().setValue(new PosBean(altitude, longitude));
            }

            @Override
            public void locationCheckFailed(AMapLocation aMapLocation) {
                Toast.makeText(MainActivity.this, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(LatLng latLng) {
                eventListener.signPoint(latLng);
            }

            @Override
            public void onLongClick(LatLng latLng) {

            }

            @Override
            public void onMarkerClick(Marker marker) {
                eventListener.clickMarker(marker);
            }
        }));
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    public class EventListener {

        public void toggleSignEnbale() {
            viewModel.toggleSignEnbale();
        }

        public String getTitle(boolean signEnable) {
            return signEnable ? "可以标记" : "禁止标记";
        }

        /**
         * 绘制点
         *
         * @param latLng
         */
        public void signPoint(LatLng latLng) {
            if (!viewModel.getSignEnable().getValue()) {
                return;
            }

            FontIconView iconView = new FontIconView(MainActivity.this);
            iconView.setTextColor(Color.RED);
            iconView.setText(R.string.marker_test);
            iconView.setTextSize(40);

            ArrayList<MarkerOptions> markerList = new ArrayList<>();
            markerList.add(new MarkerOptions()
                    .position(latLng)
                    .title("家")
                    .icon(BitmapDescriptorFactory.fromView(iconView))
                    .snippet("latitude: " + latLng.latitude + "\nlongitude: " + latLng.longitude));

            /**
             * 参数1 MarkList
             * 参数2 是否在绘制标记后自适应地图区域到所有标记
             */
            aMap.addMarkers(markerList, false);

            //添加标记点到列表中
            viewModel.getLatlngList().getValue().add(latLng);
        }

        /**
         * 绘制线
         */
        public void signLine() {
            List<LatLng> latLngs = viewModel.getLatlngList().getValue();
            aMap.addPolyline(new PolylineOptions()
                    .addAll(latLngs)
                    .width(10)
                    .color(Color.GREEN));
        }

        /**
         * 绘制面
         */
        public void signSurface() {
            double latitude = viewModel.getPosData().getValue().getLatitude();
            double longitude = viewModel.getPosData().getValue().getLongitude();

            LatLng latLng = new LatLng(latitude - 0.5, longitude - 0.5);
            aMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(1000)
                    .fillColor(Color.YELLOW)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(15));


            /**
             * 视图操作
             * CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
             * 这里latLngBounds的初始化传入两个地图坐标
             */
            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(latitude - 1, longitude - 1), new LatLng(latitude, longitude));
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100),
                    1000, new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            Log.d(TAG, "onFinish() called");
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "onCancel() called");
                        }
                    });

        }

        /**
         * 点击标记
         *
         * @param marker
         */
        public void clickMarker(Marker marker) {
            Toast.makeText(MainActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
        }
    }
}
