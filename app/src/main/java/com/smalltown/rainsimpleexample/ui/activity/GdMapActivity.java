package com.smalltown.rainsimpleexample.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.*;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;

/**
 * Created by Diagrams on 2016/1/8 14:19
 */
public class GdMapActivity extends BaseActivity implements LocationSource,AMapLocationListener {

    private MapView mapView;
    private AMap aMap;//高德地图管理
    private OnLocationChangedListener mListener;//地图位置改变监听
    private AMapLocationClient mlocationClient;//地图位置客户端
    private AMapLocationClientOption mLocationOption;//地图位置客户端选项

    public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度

    @Override
    protected int setContentLayout() {
        return R.layout.activity_gdmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        init();
//        setMarkLocation();
        setMarkets();
    }

    /**
     * 设置一个标志位
     */
    private void setMarkets() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ZHONGGUANCUN);
        markerOptions.title("中关村");
        markerOptions.snippet("中关村软件园,不错不错。");
        markerOptions.draggable(false);//不可自由移动
        markerOptions.visible(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.start));

        Marker marker = aMap.addMarker(markerOptions);
//        marker.setRotateAngle(120f);//设置角度
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            initLocation();
        }
    }

    /**
     * 设置一个最初的位置
     */
    private void setMarkLocation() {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                ZHONGGUANCUN, 18, 0, 30)), 1000, null);
    }

    /**
     * 初始化位置
     */
    private void initLocation() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f), null);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    /*************************定位监听**********************************/
    /**
     *激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                mlocationClient.stopLocation();//停止定位
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
//                mlocationClient.stopLocation();//停止定位
            }
        }
    }
    /*******************************生命周期依附*************************/

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    /**
     * 此方法需要有
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    /**
     * 此方法需要有
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    /**
     * 此方法需要有
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
