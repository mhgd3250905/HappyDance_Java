package com.example.happydance_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.happydance_java.databinding.ActivityMainBinding;
import com.example.happydance_java.model.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mMapView;
    private MainViewModel viewModel;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        binding.setMainInfo(viewModel);
        binding.setListener(new EventListener());
        binding.setLifecycleOwner(this);

        //获取地图控件引用
        mMapView = binding.map;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //在activity执行onCreated的时候执行onCreate方法
        mMapView.onCreate(savedInstanceState);

        getLifecycle().addObserver(new LocationUtils(getApplicationContext(), mMapView, new LocationUtils.OnLocationCheckListener() {
            @Override
            public void locationChecked(AMapLocation aMapLocation) {
                double altitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                viewModel.getPosData().setValue(new PosBean(altitude,longitude));
            }

            @Override
            public void locationCheckFailed(AMapLocation aMapLocation) {
                Toast.makeText(MainActivity.this, "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
            }
        }));
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    public class EventListener{

        public void SignMap() {
            double latitude = viewModel.getPosData().getValue().getLatitude();
            double longitude = viewModel.getPosData().getValue().getLongitude();

            ArrayList<MarkerOptions> markerList = new ArrayList<>();

            LatLng latLng = new LatLng(latitude , longitude);
            markerList.add(new MarkerOptions().position(latLng).title("家").snippet("DefaultMarker"));

            /**
             * 参数1 MarkList
             * 参数2 是否在绘制标记后自适应地图区域到所有标记
             */
            aMap.addMarkers(markerList,false);
        }
    }
}
