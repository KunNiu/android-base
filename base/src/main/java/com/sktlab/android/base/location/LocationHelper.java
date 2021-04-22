package com.sktlab.android.base.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sktlab.android.base.Callback;
import com.sktlab.android.base.result.Result;
import com.sktlab.android.base.result.ResultImp;
import com.sktlab.android.base.threadpool.Executors;
import com.sktlab.android.base.util.AppUtil;
import com.sktlab.android.base.util.PermissionHelper;

public class LocationHelper {
    private static final String TAG = LocationHelper.class.getSimpleName();

    @SuppressLint("MissingPermission")
    public static void getLocation(@NonNull Context context, long delay, @NonNull Callback callback) {
        Executors.location().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "start to get location");
                if (!PermissionHelper.checkLocationPermission(context)) {
                    Log.e(TAG, "no location permission");
                    Executors.main().execute(() -> callback.onComplete(ResultImp.failed(new Result.Code(Result.Code.NO_PERMISSION))));
                    return;
                }
                if (!AppUtil.isLocationOpen(context)) {
                    Log.e(TAG, "location not open");
                    Executors.main().execute(() -> callback.onComplete(ResultImp.failed(new Result.Code(Result.Code.LOCATION_NOT_OPEN))));
                    return;
                }
                final Location[] bestLocation = {null};
                final boolean[] hasSendResult = {false};
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d(TAG, "getLastKnownLocation GPS=" + location);
                    bestLocation[0] = location;
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.d(TAG, "getLastKnownLocation NETWORK=" + location);
                    if (isBetterLocation(location, bestLocation[0])) {
                        bestLocation[0] = location;
                    }
                }
                Listener gpsListener = new Listener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "gain and return GPS=" + location);
                        if (isBetterLocation(location, bestLocation[0])) {
                            bestLocation[0] = location;
                        }
                        locationManager.removeUpdates(this);
                        if (bestLocation[0] != null) {
                            Executors.main().execute(() -> callback.onComplete(ResultImp.success(bestLocation[0])));
                            hasSendResult[0] = true;
                        }
                    }
                };
                Listener networkListener = new Listener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "gain NETWORK=" + location);
                        if (isBetterLocation(location, bestLocation[0])) {
                            bestLocation[0] = location;
                        }
                        locationManager.removeUpdates(this);
                    }
                };
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.d(TAG, "requestSingleUpdate GPS");
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, gpsListener, Looper.getMainLooper());
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.d(TAG, "requestSingleUpdate NETWORK");
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, networkListener, Looper.getMainLooper());
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
                }
                Log.d(TAG, "sleep to wait");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!hasSendResult[0]) {
                    Log.d(TAG, "return location=" + bestLocation[0]);
                    Executors.main().execute(() -> callback.onComplete(ResultImp.success(bestLocation[0])));
                }
                locationManager.removeUpdates(gpsListener);
                locationManager.removeUpdates(networkListener);
            }
        });
    }

    private static class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private static boolean isBetterLocation(Location newLocation, Location oldLocation) {
        if (newLocation == null) {
            return false;
        }
        if (oldLocation == null) {
            return true;
        }
        long TWO_MINUTES = 1000 * 60 * 2;
        long timeDelta = newLocation.getTime() - oldLocation.getTime();
        if (timeDelta > TWO_MINUTES) {
            return true;
        }
        if (timeDelta < -TWO_MINUTES) {
            return false;
        }
        boolean isNewer = timeDelta > 0;
        int accuracyDelta = (int) (newLocation.getAccuracy() - oldLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        boolean isFromSameProvider = newLocation.getProvider().equals(oldLocation.getProvider());
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }
}
