//package com.restracks.android.ble;
//
//import android.app.Activity;
//import android.content.*;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.ExpandableListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapsActivity extends Activity implements LocationListener {
//
//    private final static String TAG = DeviceControlActivity.class.getSimpleName();
//
//    private ExpandableListView mGattServicesList;
//    private BluetoothLeService mBluetoothLeService;
//    private String mDeviceName;
//    private String mDeviceAddress;
//    private boolean mConnected = false;
//    private int curRate=0;
//
//    // Google Map
//    private GoogleMap googleMap;
//    PolylineOptions polOptions = new PolylineOptions().width(5).color(Color.GRAY);
//    CircleOptions circleOptions = new CircleOptions().radius(15).strokeColor(Color.TRANSPARENT);
//    private List<LocationRate> locrateHistory = new ArrayList<LocationRate>();
//
//
//    // Code to manage Service lifecycle.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//            // Automatically connects to the device upon successful start-up initialization.
//            mBluetoothLeService.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mBluetoothLeService = null;
//        }
//    };
//
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
//            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                //clearUI();
//            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                // Show all the supported services and characteristics on the user interface.
//                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
//            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//            }
//        }
//    };
//
//    private void updateConnectionState(final int resourceId) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //mConnectionState.setText(resourceId);
//            }
//        });
//    }
//
//    private void displayData(String data) {
//        if (data != null) {
//            curRate=Integer.parseInt(data);
//            //Toast.makeText(getApplicationContext(), "rate:"+curRate, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        try {
//            // Subscribe to retrieve heart-rate
//            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
//            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//
//            // Loading map
//            initilizeMap();
//            setupGPS();
////            plotFakeLocations();
////
////            List<Location> locs = new ArrayList<Location>();
////            for (LocationRate loc :locrateHistory){locs.add(loc.location);};
////            double meters = RtShared.CalculateDistance(locs,true);
////            ((TextView)findViewById(R.id.tvDistance)).setText(String.valueOf(meters));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.menu_settings:
//                Intent prefIntent = new Intent("android.intent.action.PREFS");
//                startActivity(prefIntent);
//                return true;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void setupGPS(){
//        LocationManager locationManager = (LocationManager) getSystemService(getBaseContext().LOCATION_SERVICE);
//        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
//                3000,       // 3 sec minimum interval
//                3, this);   // 3 meter minimum distance change
//
//        // intially zoom to last position on the network, to get the most reasonable location to start with
//        // when user is going outside, onLocationChanged should be triggered with GPS location
//        Location lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        zoomCurrentPosition(lastKnown);
//    }
//
//    private void initilizeMap() {
//        if (googleMap == null) {
//            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.map)).getMap();
//
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//
////        LatLng l1 =new LatLng(52.126528,4.652345);
////        LatLng l2 =new LatLng(52.126344,4.652152);
////        LatLng l3 =new LatLng(52.126133,4.651798);
////        LatLng l4 =new LatLng(52.125395,4.649051);
////
////
////
////        //googleMap.addPolyline(new PolylineOptions().add(l1,l2,l3,l4).width(8)); //.color(Color.BLUE));
////
////        // Instantiates a new CircleOptions object and defines the center and radius
////        CircleOptions circleOptions = new CircleOptions()
////                .center(l1)
////                .radius(15)
////                .fillColor(0x5AFF0000)//90% transparent red
////                .strokeColor(Color.TRANSPARENT)
////                ;
////        Circle circle = googleMap.addCircle(circleOptions);
////        circleOptions.center(l2);
////        circle = googleMap.addCircle(circleOptions);
////        circleOptions.center(l3);
////        circle = googleMap.addCircle(circleOptions);
////        circleOptions.center(l4);
//    }
//
//    private void plotFakeLocations(){
//        int startHR =75;
//        List<LatLng> latlangs = new ArrayList<LatLng>();
//
//        latlangs.add(new LatLng(52.252269,4.436460));
//        latlangs.add(new LatLng(52.251533,4.435859));
//        latlangs.add(new LatLng(52.251218,4.435580));
//        latlangs.add(new LatLng(52.250902,4.435322));
//        latlangs.add(new LatLng(52.250587,4.435065));
//        latlangs.add(new LatLng(52.250272,4.434807));
//        latlangs.add(new LatLng(52.249957,4.434550));
//        latlangs.add(new LatLng(52.249641,4.434292));
//        latlangs.add(new LatLng(52.249326,4.434035));
//        latlangs.add(new LatLng(52.249011,4.433777));
//        latlangs.add(new LatLng(52.248695,4.433520));
//        latlangs.add(new LatLng(52.248380,4.433262));
//        latlangs.add(new LatLng(52.248065,4.433005));
//        latlangs.add(new LatLng(52.247763,4.432769));
//        latlangs.add(new LatLng(52.247461,4.432533));
//        latlangs.add(new LatLng(52.247329,4.432275));
//        latlangs.add(new LatLng(52.247198,4.433348));
//        latlangs.add(new LatLng(52.247066,4.433906));
//        latlangs.add(new LatLng(52.247658,4.434421));
//        latlangs.add(new LatLng(52.248341,4.434893));
//        latlangs.add(new LatLng(52.248814,4.435537));
//        latlangs.add(new LatLng(52.249221,4.436095));
//        latlangs.add(new LatLng(52.249497,4.436760));
//        latlangs.add(new LatLng(52.249878,4.437511));
//        latlangs.add(new LatLng(52.250771,4.438798));
//        latlangs.add(new LatLng(52.251375,4.439507));
//        latlangs.add(new LatLng(52.251809,4.438863));
//
//
//        for (LatLng latlang : latlangs){
//            Location loc = new Location("dummy");
//            loc.setLatitude(latlang.latitude);
//            loc.setLongitude(latlang.longitude);
//            LocationRate locRate = new LocationRate(loc,startHR+=8);
//            locrateHistory.add(locRate);
//
//            polOptions.add(latlang);
//            googleMap.addPolyline(polOptions);
//
//            circleOptions.fillColor(getRateColor(startHR)).center(latlang);
//            googleMap.addCircle(circleOptions);
//        };
//
//        zoomCurrentPosition(new LatLng(52.247253,4.445815));
//    }
//
//    private int getRateColor(int startHR) {
//        if (startHR <=70){return 0xbb48ff00;}
//        if (startHR <=80){return 0xbb6cff00;}
//        if (startHR <=90){return 0xbb9cff00;}
//        if (startHR <=100){return 0xbbc0ff00;}
//        if (startHR <=110){return 0xbbe4ff00;}
//        if (startHR <=120){return 0xbbfff600;}
//        if (startHR <=130){return 0xbbffde00;}
//        if (startHR <=140){return 0xbbffcc00;}
//        if (startHR <=150){return 0xbbffa800;}
//        if (startHR <=160){return 0xbbff9600;}
//        if (startHR <=170){return 0xbbff8400;}
//        if (startHR <=180){return 0xbbff6600;}
//        if (startHR >=190){return 0xbbff3000;}
//        return 0;
//    }
//
//    private void zoomCurrentPosition(Location location){
//        if (googleMap!=null){
//            double lat= location.getLatitude();
//            double lng = location.getLongitude();
//            LatLng ll = new LatLng(lat, lng);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
//        }
//    }
//
//    private void zoomCurrentPosition(LatLng latlang){
//        if (googleMap!=null){
//            double lat= latlang.latitude;
//            double lng = latlang.longitude;
//            LatLng ll = new LatLng(lat, lng);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
//        initilizeMap();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        //if debug.. toast
//        String str = "lat: "+location.getLatitude()+" long: "+location.getLongitude();
//        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
//
//        LocationRate locRate = new LocationRate(location,75);
//
//        Location prevLocation = locrateHistory.get(locrateHistory.size()).location;
//
//        if (prevLocation.getLatitude() != location.getLatitude() && prevLocation.getLongitude() != location.getLongitude() ){
//            locrateHistory.add(locRate);
//        }
//
//        zoomCurrentPosition(location);
//
//        LatLng ltlng = new LatLng(location.getLatitude(),location.getLongitude());
//        polOptions.add(ltlng);
//
//        googleMap.addPolyline(polOptions);
//
//        circleOptions.fillColor(0x5AFF0000).center(ltlng);
//        googleMap.addCircle(circleOptions);
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }
//
//    class LocationRate{
//        public LocationRate(Location _location, int _heartRate){
//            this.location=_location;
//            this.heartRate=_heartRate;
//        }
//        Location location;
//        int heartRate;
//    }
//
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }
//}
