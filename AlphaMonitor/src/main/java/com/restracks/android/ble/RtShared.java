package com.restracks.android.ble;

import android.location.Location;

import java.util.List;

/**
 * Created by mvklingeren on 21/02/14.
 */
public class RtShared {
    // Calculate the distance
    public static double CalculateDistance(List<Location> locationList,boolean metric){
        double result=0;
        for (Location loc :locationList.subList(1, locationList.size())){
            result+=loc.distanceTo(locationList.get(locationList.indexOf(loc)-1));
        }
        return Math.round(result);
    }
}
