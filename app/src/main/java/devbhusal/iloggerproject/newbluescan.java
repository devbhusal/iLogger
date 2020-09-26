package devbhusal.iloggerproject;

/**
 * Created by Dev on 24-Sep-15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;





public class newbluescan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluescan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    public void savetofileblue(View v){
        ListView ll;
        ll=(ListView)findViewById(R.id.listView);

        File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
        if (!folder1.exists()) folder1.mkdir();
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (!folder2.exists()) folder2.mkdir();
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp/blue.txt");
        file.delete();

        try {


            FileWriter buf = new FileWriter(file);


            for (int i=0;i<ll.getCount();i++) {
                buf.append(ll.getItemAtPosition(i).toString()+"\n");
            }
            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        finish();
    }


    public void  searchnowblue(View v){
        BluetoothAdapter mBluetoothAdapter;
        BroadcastReceiver mReceiver;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ArrayList<String> values = new ArrayList<String>();
                ListView ll;
                ll=(ListView)findViewById(R.id.listView);//Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    values.add(device.getName() );
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,mArrayAdapter);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1,values);
                ll.setAdapter(adapter);
                if (ll.getCount()==0){
                    Toast.makeText(context, "Zero Bluetooth devices found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "DONE! tap 'save' to save to database" , Toast.LENGTH_SHORT).show();
                }


            }


        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
