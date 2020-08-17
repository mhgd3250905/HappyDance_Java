package com.example.happydance_java.model.viewmodel;

import android.app.Application;
import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.example.happydance_java.PosBean;
import com.example.happydance_java.db.LocalRoomRequestManager;
import com.example.happydance_java.db.Position;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends AndroidViewModel {
    private static final String TAG = "MapViewModel";

    private Context application;

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

    public LiveData<List<Position>> getPositionsLive(){
        return LocalRoomRequestManager.getInstance(application).getPositionsLive();
    }

    public MapViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        posData.setValue(new PosBean(0, 0));
        signEnable.setValue(false);
        latlngList.setValue(new ArrayList<LatLng>());
    }

    public void toggleSignEnbale(){
        signEnable.setValue(!signEnable.getValue());
    }

    public void insertPositions(Position... positions){
        LocalRoomRequestManager.getInstance(application).insertPositions(positions);
    }

    public void updatePositions(Position... positions){
        LocalRoomRequestManager.getInstance(application).updatePositions(positions);
    }

    public void deletePositions(Position... positions){
        LocalRoomRequestManager.getInstance(application).deletePositions(positions);
    }
    public void deleteAllPositions(){
        LocalRoomRequestManager.getInstance(application).deleteAllPositions();
    }
}
