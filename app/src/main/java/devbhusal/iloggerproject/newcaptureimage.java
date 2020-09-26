package devbhusal.iloggerproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;



public class newcaptureimage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcaptureimage);
        checkgps();



    }
    public void saveit(View v){
        File imgFile = new  File(Environment.getExternalStorageDirectory(), "/ilogger_data/temp/Attachment" + ".jpg");

        if(imgFile.exists()){
            finish();
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
