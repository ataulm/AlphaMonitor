package com.restracks.android.ble;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.location.LocationManager;
//import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mvklingeren on 02/03/14.
 */
public class LocationService extends Service implements LocationListener {

    Location ltlng;

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        return true;
    }

    private final IBinder mBinder = new LocalBinder();

    // GPS
    @Override
    public void onLocationChanged(Location location) {
        //ltlng = new LatLng(location.getLatitude(),location.getLongitude());
    }

    // GPS
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // GPS device status change
    }

    // GPS
    @Override
    public void onProviderEnabled(String s) {

    }

    // GPS
    @Override
    public void onProviderDisabled(String s) {

    }

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Make sure Location services is closed
        // close();
        return super.onUnbind(intent);
    }
}
