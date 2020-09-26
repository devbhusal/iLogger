package devbhusal.iloggerproject;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class addnewmarker extends AppCompatActivity {
    public double lat;
   public  double lng;
    public String gplace;
    public ListView ll;

    public ProgressBar linlaHeaderProgress;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    // private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewmarker);

      Bundle b = getIntent().getExtras();
         lat=b.getDouble("lat");
        lng=b.getDouble("lng");
       gplace=b.getString("place");


        //gplace="info not found";
        TextView tt=(TextView)findViewById(R.id.test);
        tt.setText(gplace);

    }
    public void saveit(View v){
        File imgFile = new  File(Environment.getExternalStorageDirectory(), "/ilogger_data/temp/Attachment" + ".jpg");

        if(imgFile.exists()){
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
    public void setCamera(View v){
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
        if (!folder1.exists()) folder1.mkdir();
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (!folder2.exists()) folder2.mkdir();


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/ilogger_data/temp/Attachment" + ".jpg")));
        startActivityForResult(cameraIntent, 1001);
    }

    public void checkgps(){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
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
            // if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) checkgps();
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){

            ImageView img=(ImageView)findViewById(R.id.imageView);



            File imgFile = new  File(Environment.getExternalStorageDirectory(), "/ilogger_data/temp/Attachment" + ".jpg");

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                img.setImageBitmap(myBitmap);

            }





            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            Location location = (Location) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            File f = new File(Environment.getExternalStorageDirectory(),"/ilogger_data/temp/Attachment" + ".jpg");

            geoTag(f.getAbsolutePath(), latitude, longitude);

        }
    }

    public void geoTag(String filename, double latitude, double longitude){
        // ExifInterface exif;

        try {
            // exif = new ExifInterface(filename);
            ExifInterface exif=new ExifInterface(filename);


            latitude = Math.abs(latitude);
            longitude = Math.abs(longitude);


            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;
            String lat = num1Lat + "/1," + num2Lat + "/1," + num3Lat + "/1000";
            String lon = num1Lon + "/1," + num2Lon + "/1," + num3Lon + "/1000";
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);


            if (latitude < 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (Exception e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
            Toast.makeText(this, "Scanning....", Toast.LENGTH_SHORT).show();
        }

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

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void wifie(View v){
      //  linlaHeaderProgress.setVisibility(View.VISIBLE);

        ll=(ListView)findViewById(R.id.listView);

        new Thread(new Runnable() {


            public void run(){

                ll.post(new Runnable() {
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
       // linlaHeaderProgress=(ProgressBar)findViewById(R.id.pbHeaderProgress11);
       // linlaHeaderProgress.setVisibility(View.INVISIBLE);

        WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
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
        //linlaHeaderProgress.setVisibility(View.INVISIBLE);
        if (ll.getCount()==0){
            Toast.makeText(this, "Zero access point found" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "DONE! tap 'save' to save to database" , Toast.LENGTH_SHORT).show();
        }

    }
    public void playstart(View v) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        Button playst=(Button)findViewById(R.id.PlayStop);
        playst.setVisibility(View.VISIBLE);
    }

    public void playstop(View v) {
        mPlayer.release();
        mPlayer = null;
    }

    public  void recstart(View v) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();

        Button playst1=(Button)findViewById(R.id.PlayStart);
        playst1.setVisibility(View.INVISIBLE);
        Button playst2=(Button)findViewById(R.id.PlayStop);
        playst2.setVisibility(View.INVISIBLE);

      //  Toast.makeText(this, "testing testing", Toast.LENGTH_SHORT).show();
    }

    public void recstop(View v) {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Button playst=(Button)findViewById(R.id.PlayStart);
        playst.setVisibility(View.VISIBLE);

    }



    public int savenewmarker(View v){
        EditText tt=(EditText)findViewById(R.id.markname);
       if ( tt.getText().toString().length()==0){
           Toast.makeText(this, "Type marker name to save it", Toast.LENGTH_SHORT).show();
           return 0;
        }
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
        if (!folder1.exists()) folder1.mkdir();
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (!folder2.exists()) folder2.mkdir();
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/All_markers.txt");
        String filename=tt.getText().toString().replaceAll("[^a-zA-Z0-9.-]", "_"); // to remove all abd filename chaacter
        //file.delete();

        //TODO check if merker already used
                 //myString = myString.replaceAll
        // Attach new marker to msrker list
        String temp1="temp",temp2="You_are_here",temp3="Selected_location";
        if ( filename.equals(temp1)||filename.equals(temp2)||filename.equals(temp3)){
            Toast.makeText(this, "You cannot use this marker name", Toast.LENGTH_SHORT).show();
            return 0;
        }

        Boolean result=false;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(file));
            while(in.hasNextLine() && !result) {
                result = in.nextLine().indexOf(filename) >= 0;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            try { in.close() ; } catch(Exception e) { /* ignore */ }
        }


     if (result){
         Toast.makeText(this, "Marker name already exist. please use another ", Toast.LENGTH_SHORT).show();
         return 0;
     }



        try {


            FileWriter buf = new FileWriter(file,true);


                buf.append(filename+"\n");

            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        //make marker folder

        File folder3 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+filename);
        if (!folder3.exists()) folder3.mkdir();
        File file1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+filename+"/location.txt");
        //write location
        try {


            FileWriter buf = new FileWriter(file1);


            buf.append(Double.toString(lat)+"\n");
            buf.append(Double.toString(lng));

            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


        // wrte google place
        File file2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+filename+"/gplace.txt");
        //write location
        try {


            FileWriter buf = new FileWriter(file2);


            buf.write(gplace);

            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        //TODO write date and time inof
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        File filetime = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+filename+"/timedate.txt");
        //write location
        try {


            FileWriter buf = new FileWriter(filetime);


            buf.write(mydate);

            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        // copy all data from temp folder

        try {
            FileUtils.copyDirectory( folder2, folder3);

        }
        catch (IOException e) {
        Log.e("Exception", "File write failed: " + e.toString());
    }

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    return 1;
    }

    public void showwifiscanmobilemode(View v){
        if (isTablet()){

            //Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.wifiscan);

            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();


        }
        else {
            Intent a = new Intent(this, newwifiscan.class);
            startActivity(a);
        }


    }
    public  boolean isTablet() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }


    public void  showbluescanmobile(View v){



        if (isTablet()){
            Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.bluescan);
            fragment.setArguments(arguments);
            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();


        }
        else {


            Intent a = new Intent(this, newbluescan.class);
            startActivity(a);
        }
   }

    public void  capturenewimage(View v){

        if (isTablet()){
            checkgps();
            Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.activity_newcaptureimage);
            fragment.setArguments(arguments);
            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();


        }
        else {

            Intent a = new Intent(this, newcaptureimage.class);
            startActivity(a);
        }
    }

    public void recordnewaudio(View v){

        if (isTablet()){
            File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
            if (!folder1.exists()) folder1.mkdir();
            File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
            if (!folder2.exists()) folder2.mkdir();
            File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp/audio.3gp");



            mFileName =file.getAbsolutePath();
            Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.activity_newrecordaudio);
            fragment.setArguments(arguments);
          //  fragment.onCreateView()
            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();


        }
        else {
            Intent a = new Intent(this, newrecordaudio.class);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addnewmarker, menu);
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
