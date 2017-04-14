package tw.fili.fibeacon;


import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.utils.AprilL;

import java.util.ArrayList;
import java.util.List;

public class BeaconScanner {
    public class Beacon{
        int rssi;
        int measuredPower;
        String macAddress;
        String proximityUUID;
        int major, minor;

        public int getRssi() {
            return rssi;
        }
        public void setRssi(int rssi) {
            this.rssi = rssi;
        }
        public int getMeasuredPower() {
            return measuredPower;
        }
        public void setMeasuredPower(int measuredPower) {
            this.measuredPower = measuredPower;
        }
        public String getMacAddress() {
            return macAddress;
        }
        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }
        public void setProximityUUID(String proximityUUID) {
            this.proximityUUID = proximityUUID;
        }
        public String getProximityUUID() {
            return proximityUUID;
        }
        public void setMajor(int major) {
            this.major = major;
        }
        public int getMajor() {
            return major;
        }
        public void setMinor(int minor) {
            this.minor = minor;
        }
        public int getMinor() {
            return minor;
        }
    }

    public interface IBeaconDiscorverd {
        void onBeaconsDiscovered(List<Beacon> beacons);
    }

    private static final String TAG = "filitovBeabonScanner";
    private static final Region ALL_BEACONS_REGION = new Region(TAG, null, null, null);
    private BeaconManager mBeaconManager;
    private IBeaconDiscorverd mBeaconDiscorved;

    public BeaconScanner(Context ctx, IBeaconDiscorverd bd) {
        this.mBeaconDiscorved = bd;
        AprilL.enableDebugLogging(true);
        mBeaconManager = new BeaconManager(ctx);
        mBeaconManager.setRangingListener(rangingListener);
    }

    private BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, List<com.aprilbrother.aprilbrothersdk.Beacon> list) {
            Log.i(TAG, "Beacons Discovered...");
            if( list==null || list.size()<1 ){
                return;
            }

            List<Beacon> bes = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                com.aprilbrother.aprilbrothersdk.Beacon bo = list.get(i);
                Log.i(TAG, "detail:" + bo);
                Beacon bc = new Beacon();
                bc.setMacAddress( bo.getMacAddress() );
                bc.setProximityUUID( bo.getProximityUUID() );
                bc.setMajor( bo.getMajor() );
                bc.setMinor( bo.getMinor() );
                bc.setMeasuredPower( bo.getMeasuredPower() );
                bc.setRssi( bo.getRssi() );
                bes.add(bc);
            }
            mBeaconDiscorved.onBeaconsDiscovered(bes);
        }
    };


    public void connnect(){
        Log.i(TAG, "Connect...");
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    mBeaconManager.startRanging(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error while starting ranging", e);
                }
            }
        });
    }


    public boolean hasBluetooth(){
        return mBeaconManager.hasBluetooth();
    }

    public boolean isBluetoothEnabled(){
        return mBeaconManager.isBluetoothEnabled();
    }

    public void destory(){
        Log.i(TAG, "Disconnect...");
        mBeaconManager.disconnect();
    }

    public void stop(){
        Log.i(TAG, "Stop...");
        try {
            mBeaconManager.stopRanging(ALL_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.e(TAG, "Error while stopping ranging", e);
        }
    }
}
