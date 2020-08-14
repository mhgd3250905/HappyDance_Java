package com.example.happydance_java.model.viewmodel;

import com.example.happydance_java.PosBean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<PosBean> posData = new MutableLiveData<>();

    public MutableLiveData<PosBean> getPosData() {
        return posData;
    }

    public MainViewModel() {
        posData.setValue(new PosBean(0, 0));
    }

}
