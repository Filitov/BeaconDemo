package tw.fili.fibeacontest;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tw.fili.fibeacon.BeaconActivity;
import tw.fili.fibeacon.BeaconScanner;


public class MainActivity extends BeaconActivity {
    private static final String TAG = "filitovBeaconTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBeaconsDiscovered(List<BeaconScanner.Beacon> beacons) {
        super.onBeaconsDiscovered(beacons);

        List<String> list = new ArrayList<>();
        for(int i=0; i<beacons.size(); i++){
            BeaconScanner.Beacon bc = beacons.get(i);
            list.add( bc.getMacAddress() + "  (" + bc.getRssi() + ")" );
            Log.d(TAG, bc.toString());
        }
        Collections.sort(list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        ListView lv = (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);


    }
}
