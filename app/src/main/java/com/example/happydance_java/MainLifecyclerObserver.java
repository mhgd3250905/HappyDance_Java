package com.example.happydance_java;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MainLifecyclerObserver implements LifecycleObserver {
    private static final String TAG = "MainLifecyclerObserver";


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(){
        Log.d(TAG, "onCreate() called");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(){
        Log.d(TAG, "onResume() called");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(){
        Log.d(TAG, "onPause() called");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestory(){
        Log.d(TAG, "onDestory: ");
    }
}
