package tw.fili.fibeacon;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

public class BeaconActivity extends Activity implements BeaconScanner.IBeaconDiscorverd{
    private static final String TAG = "filitovBeaconActivity";
    private BeaconScanner beaconScanner = null;
    private static final int BluetoothAskActivityResult = 1234;
    private static final int RequestPermissionsResult = 1235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ){
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionsResult );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startBeaconStart();
    }

    @Override
    protected void onStop() {
        if( beaconScanner!=null){
            beaconScanner.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if( beaconScanner!=null) {
            beaconScanner.destory();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothAskActivityResult) {
            if (resultCode == Activity.RESULT_OK) {
                beaconScanner.connnect();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == RequestPermissionsResult ){
            if( grantResults.length>=2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                startBeaconStart();
            } else {
                Toast.makeText(this, "Location not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startBeaconStart(){
        if( beaconScanner==null ) {
            beaconScanner = new BeaconScanner(this, this);
        }
        if (!beaconScanner.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
        } else {
            if (!beaconScanner.isBluetoothEnabled()) {
                Log.d(TAG, "ask enable Bluetooth");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, BluetoothAskActivityResult);
            } else {
                beaconScanner.connnect();
            }
        }
    }

    @Override
    public void onBeaconsDiscovered(List<BeaconScanner.Beacon> beacons) {
        //nothing
    }

}
