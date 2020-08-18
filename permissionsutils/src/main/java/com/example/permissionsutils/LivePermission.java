package com.example.permissionsutils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

/**
 * @author :   shengkai
 * create   :   2020/8/18/018-23:30
 * package  :   com.example.permissionsutils
 * desc     :   使用LiveData动态申请权限
 */
public class LivePermission {
    private static final String TAG = "LivePermission";
    private LiveFragment liveFragment;

    public LivePermission(AppCompatActivity activity) {
        liveFragment = getInstance(activity.getSupportFragmentManager());
    }

    public LivePermission(Fragment fragment) {
        liveFragment = getInstance(fragment.getChildFragmentManager());
    }

    public MutableLiveData<PermissionResult> request(String permission) {
        return this.requestArray(new String[]{permission});
    }

    public MutableLiveData<PermissionResult> requestArray(String[] permissions) {
        if (liveFragment != null) {
            liveFragment.requestPermissions(permissions);
            return liveFragment.getPermissionLive();
        }
        return null;
    }

    private LiveFragment getInstance(FragmentManager fragmentManager) {
        LiveFragment tempFragment = null;
        synchronized (this) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                tempFragment = new LiveFragment();
                fragmentManager.beginTransaction().add(tempFragment, TAG).commitNow();
            } else {
                tempFragment = (LiveFragment) fragmentManager.findFragmentByTag(TAG);
            }
        }
        return tempFragment;
    }
}
