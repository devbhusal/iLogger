package devbhusal.iloggerproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class newwifiscan extends AppCompatActivity {
    public ListView ll;

    public ProgressBar linlaHeaderProgress;
   // public TextView selectedmarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifiscan);
        linlaHeaderProgress=(ProgressBar)findViewById(R.id.pbHeaderProgress11);
        linlaHeaderProgress.setVisibility(View.INVISIBLE);


    }

    public void savetofile(View v){
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
        if (!folder1.exists()) folder1.mkdir();
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (!folder2.exists()) folder2.mkdir();
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp/wifi.txt");
        file.delete();
        try {


            FileWriter buf = new FileWriter(file);
            ListView ll;
            ll=(ListView)findViewById(R.id.listView);

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

    public void wifie(View v){
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        ll=(ListView)findViewById(R.id.listView);

        new Thread(new Runnable() {


            public void run(){

                ll.post (new Runnable() {
                    public void run() {
                       //View.setImageBitmap(bitmap);
                        showwifilist();
                    }
                });



          }
        }).start();



    }


    public void showwifilist(){
        String ssid;

        WifiManager  mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
      //  Toast.makeText(this, "Scanning...." , Toast.LENGTH_SHORT).show();
        mWifiManager.startScan();
        List<ScanResult> mScanResults = mWifiManager.getScanResults();

        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < mScanResults.size(); i++) {
           ssid= mScanResults.get(i).SSID;
            if(ssid.length()!=0) values.add(ssid);


        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,values);


        ll.setAdapter(adapter);
        //Toast.makeText(this, "DONE" , Toast.LENGTH_SHORT).show();
        linlaHeaderProgress.setVisibility(View.INVISIBLE);
        if (ll.getCount()==0){
            Toast.makeText(this, "Zero access point found" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "DONE! tap 'save' to save to database" , Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
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
