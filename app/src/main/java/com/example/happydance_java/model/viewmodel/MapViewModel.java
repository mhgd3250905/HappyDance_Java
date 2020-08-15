package com.example.happydance_java.model.viewmodel;

import com.amap.api.maps.model.LatLng;
import com.example.happydance_java.PosBean;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {
    private MutableLiveData<PosBean> posData = new MutableLiveData<>();
    private MutableLiveData<Boolean> signEnable=new MutableLiveData<>();
    private MutableLiveData<List<LatLng>> latlngList=new MutableLiveData<>();

    public MutableLiveData<PosBean> getPosData() {
        return posData;
    }

    public MutableLiveData<Boolean> getSignEnable() {
        return signEnable;
    }

    public MutableLiveData<List<LatLng>> getLatlngList() {
        return latlngList;
    }

    public MapViewModel() {
        posData.setValue(new PosBean(0, 0));
        signEnable.setValue(false);
        latlngList.setValue(new ArrayList<LatLng>());
    }

    public void toggleSignEnbale(){
        signEnable.setValue(!signEnable.getValue());
    }

}
