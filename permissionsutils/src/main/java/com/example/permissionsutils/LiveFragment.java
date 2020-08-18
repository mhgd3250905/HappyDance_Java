package com.example.permissionsutils;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

/**
 * @author  :   shengkai
 * create   :   2020/8/18/018-23:31
 * package  :   com.example.permissionsutils
 * desc     :   用于申请权限的Fragment
 */
public class LiveFragment extends Fragment {
    private MutableLiveData<PermissionResult> permissionLive;
    private int PERMISSIONS_REQUEST_CODE = 100;

    public MutableLiveData<PermissionResult> getPermissionLive() {
        return permissionLive;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void requestPermissions(String[] permissions){
        permissionLive=new MutableLiveData<>();
        requestPermissions(permissions,PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSIONS_REQUEST_CODE){
            List<String> denyPermissions=new ArrayList<>();
            List<String> rationalePermissions=new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i]== PackageManager.PERMISSION_DENIED){
                    if (shouldShowRequestPermissionRationale(permissions[i])){
                        rationalePermissions.add(permissions[i]);
                    }else {
                        denyPermissions.add(permissions[i]);
                    }
                }
            }
            if (denyPermissions.isEmpty()&&rationalePermissions.isEmpty()){
                permissionLive.setValue(new GrandResult());
            }else {
                if (!rationalePermissions.isEmpty()){
                    permissionLive.setValue(new RationaleResult(rationalePermissions));
                }else if (!denyPermissions.isEmpty()){
                    permissionLive.setValue(new DenyResult(rationalePermissions));
                }
            }

        }
    }
}
