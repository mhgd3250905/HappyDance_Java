package com.example.happydance_java.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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
import com.example.happydance_java.db.AppDataBase;
import com.example.happydance_java.db.Position;
import com.example.happydance_java.model.viewmodel.MapViewModel;
import com.example.happydance_java.view.customize.FontIconView;
import com.example.permissionsutils.LivePermission;
import com.example.permissionsutils.PermissionResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mMapView;
    private MapViewModel viewModel;
    private AMap aMap;
    private EventListener eventListener;
    private ActivityMainBinding binding;
    private List<Position> positionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MapViewModel.class);
        binding.setMapInfo(viewModel);
        viewModel.getPositionsLive().observe(this, new Observer<List<Position>>() {
            @Override
            public void onChanged(List<Position> positions) {
                positionList = positions;
            }
        });
        eventListener = new EventListener();/**/
        binding.setListener(eventListener);
        binding.setLifecycleOwner(this);

        new LivePermission(this).requestArray(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }).observe(this, new Observer<PermissionResult>() {
            @Override
            public void onChanged(PermissionResult permissionResult) {
                Log.d(TAG, "onChanged() called with: permissionResult = [" + permissionResult + "]");
            }
        });

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
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                Log.d(TAG, "locationChecked: latitude = " + latitude + ", longitude = " + longitude);
                viewModel.getPosData().setValue(new PosBean(latitude, longitude));
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
            viewModel.insertPositions(new Position(latLng.latitude, latLng.longitude, new Date()));
        }

        /**
         * 绘制线
         */
        public void signLine() {
            List<LatLng> latLngs = new ArrayList<>();
            List<Position> positions = new ArrayList<>();
            if (positionList!=null){
                positions.addAll(positionList);
            }
            for (int i = 0; i < positions.size(); i++) {
                latLngs.add(new LatLng(positions.get(i).latitude, positions.get(i).longitudu));
            }
//            List<LatLng> latLngs = viewModel.getLatlngList().getValue();
            aMap.addPolyline(new PolylineOptions()
                    .addAll(latLngs)
                    .width(10)
                    .color(Color.GREEN));
        }

        public void cleanDb(){
            viewModel.deleteAllPositions();
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
