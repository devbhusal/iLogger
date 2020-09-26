package devbhusal.iloggerproject;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class settings extends AppCompatActivity {


    public CheckBox gps;
    public CheckBox inter;
    public CheckBox blue;
    public CheckBox wifii;
    public BluetoothAdapter bluetoothAdapter ;
    public ConnectivityManager connectivityManager ;
   public  WifiManager  wifi ;
    public LocationManager lm ;
   public TextView tt;
   public Intent xx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        checksetting();
      setsettingauto();

        checksetting();

    }

    public void checkandstart(View v){
    boolean a,b,c,d;

        checksetting();
        a=gps.isChecked();
        b=inter.isChecked();
        c=blue.isChecked();
        d=wifii.isChecked();
        if (a && b && c && d){
        tt.setVisibility(View.INVISIBLE);
         startActivity(xx);

        }
        else{
            tt.setVisibility(View.VISIBLE);
            setsettingauto();
            checksetting();
        }
    }

public void init(){
    xx=new Intent(this,showmap.class);
    gps=(CheckBox)findViewById(R.id.gpscheckBox);
    tt=(TextView)findViewById(R.id.textView);
     inter=(CheckBox)findViewById(R.id.internetcheckBox);
    blue=(CheckBox)findViewById(R.id.bluetoothcheckBox);
   wifii=(CheckBox)findViewById(R.id.wificheckBox);
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
     wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


}
public void checksetting(){
    if (isNetworkAvailable()) inter.setChecked(true);

    if (bluetoothAdapter.isEnabled()) blue.setChecked(true);
    if (wifi.isWifiEnabled()) wifii.setChecked(true);
    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) gps.setChecked(true);

}
    public void setsettingauto(){

        enablewifi();
        checkgps();
        setBluetooth(true);



    }

    private boolean isNetworkAvailable() {

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public  void  setBluetooth(boolean enable) {

        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
             bluetoothAdapter.enable();

        }
        else if(!enable && isEnabled) {
             bluetoothAdapter.disable();

        }
        // No need to change bluetooth state

    }






    public void checkgps(){

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, You have to Enable it")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void enablewifi(){

        if (!wifi.isWifiEnabled())
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
