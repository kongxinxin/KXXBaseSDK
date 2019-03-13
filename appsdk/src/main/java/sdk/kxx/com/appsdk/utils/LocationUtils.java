package sdk.kxx.com.appsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import java.util.List;

/**
 * @author : kongxx
 * @Created Date : 2019/3/9 15:39
 * @Description : 定位管理类
 */
public class LocationUtils {

    private LocationManager locationManager;

    public Location getLocation(Context mContext) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(i);
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);

        return location;
    }

}
