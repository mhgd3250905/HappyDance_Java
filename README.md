# HappyDance_Java
HappyDance_Java 一起跳舞软件，高德地图学习demo



### 高德地图使用笔记

##### 1. 按照区域移动视图

```java
/**
 * 视图操作
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
```

```AMap.animateCamera(CameraUpdate, duration, callback)```，通过```CameraUpdateFactory```来构建不同类型的```CameraUpdate```,其中```CameraUpdateFactory.newLatLngBounds(LatLngBounds, padding)```，是通过经纬度划定区域来进行目标移动，视野最大不超过范围，这里```LatLngBounds```的初始化为两个坐标点进行范围限制，也就是（x1, y1）和（x2, y2）规定一个视野范围，因为北半球经纬度和常规坐标轴完全相反，所以在设置的需要注意。

