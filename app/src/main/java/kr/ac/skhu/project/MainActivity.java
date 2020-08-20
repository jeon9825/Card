package kr.ac.skhu.project;

import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.Symbol;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, NaverMap.OnMapClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    MapFragment mapFragment;

    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    Marker marker = new Marker();

    List<Marker> list = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START] 지도 보여주기
        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        // [END]

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setCircleRadius(30); // 원 반경 지정

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(true); // 기본값 : true
        uiSettings.setZoomControlEnabled(true); // 기본값 : true
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false

        naverMap.setBuildingHeight(1.0f); // 건물 높이를 1.0f으로 설정

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); // 위치 추적 모드를 follow로 설정

        naverMap.setExtent(new LatLngBounds
                (new LatLng(31.43, 122.37),
                        new LatLng(44.35, 132)));
        // 화면에 보이는 범위 설정
        list.add(new Marker(new LatLng(37.316173, 126.837670)));
        list.add(new Marker(new LatLng(37.361777, 126.738313)));

        for (Marker marker:list){
            marker.setMap(naverMap);
        }

        // [START] 지도 클릭하는 메소드 (onClick에서 onSymbolClick으로 변경) => 확인 바람
        naverMap.setOnSymbolClickListener(new NaverMap.OnSymbolClickListener() {
            // 지도 심벌을 클릭하면
            @Override
            public boolean onSymbolClick(@NonNull Symbol symbol) {
                return false;
            }
        });
        //[END]

        // [START]
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {

            }
        });
        // [END]
    }
        @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

}
