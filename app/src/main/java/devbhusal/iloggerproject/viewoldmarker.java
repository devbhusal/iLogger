package devbhusal.iloggerproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class viewoldmarker extends AppCompatActivity {
public String markedtitle;
    public TextView marktextview;
    public TextView gplace;
    public Double markedlat,markedlng;
    String foldername;
    String bluepoint;
    public String wifipoint;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    // private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewoldmarker);
        Bundle b = getIntent().getExtras();
              markedtitle=b.getString("marker");
        markedlat=b.getDouble("lat");
        markedlng=b.getDouble("lng");


        marktextview=(TextView)findViewById(R.id.markername);
       marktextview.setText(markedtitle);
        String str;
        gplace=(TextView)findViewById(R.id.test);

        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ markedtitle +"/gplace.txt");
        //Boolean v=true;
        if(!file.exists()){
            gplace.setText("Place info not provided");
           // v=false;

        }
        else {
            str = "";
            Scanner in = null;
            try {
                in = new Scanner(new FileReader(file));

                while (in.hasNextLine()) {
                    str = str + in.nextLine() + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            gplace.setText(str);

        }
        TextView timedisplay=(TextView)findViewById(R.id.timed);

        File file2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ markedtitle +"/timedate.txt");
        //Boolean v=true;
        if(!file2.exists()){
            timedisplay.setText("Time tag not found");
            // v=false;

        }
        else {
            str = "";
            Scanner in = null;
            try {
                in = new Scanner(new FileReader(file2));

                while (in.hasNextLine()) {
                    str = str + in.nextLine() + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            timedisplay.setText(str);

        }


    }
    public void blueshare(View v){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        // Uri screenshotUri = Uri.parse(picURI);
        TextView tt=(TextView)findViewById(R.id.test);
        String data;

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "infoaboutplace.txt");
        data=markedtitle +"\n"
                +"Latitude:"+markedlat+"\n"
                +"Longitude:"+markedlng+"\n"
               + tt.getText().toString();

        try {


            //   fp=Environment.getExternalStorageDirectory().getAbsolutePath()+"/infoaboutplace.txt";
            // BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            FileWriter buf = new FileWriter(file);
            buf.write(data);
            buf.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }







        sharingIntent.setType("text/plain");
        sharingIntent.setPackage("com.android.bluetooth");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(sharingIntent, "Share file"));
    }


    public  boolean isTablet() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }


    public int showoldsavedwifi(View v) {
        if (isTablet()){
            foldername=markedtitle;

            File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ foldername +"/wifi.txt");

            if(!file.exists()){
                Toast.makeText(this, "Wifi wasn`t scanned in this location", Toast.LENGTH_SHORT).show();
                return 0;

            }

            //Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.activity_showoldwifi);

            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();


            TextView tt=(TextView)findViewById(R.id.wifis);
            wifipoint="";
            Scanner in = null;
            try {
                in = new Scanner(new FileReader(file));
                while(in.hasNextLine() ) {
                    wifipoint=wifipoint+in.nextLine()+"\n";
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            finally {
                try { in.close() ; } catch(Exception e) {  }
            }
            tt.setText(wifipoint);
            if (wifipoint.length()==0){

                Toast.makeText(this, "Zero access point was found", Toast.LENGTH_SHORT).show();

            }


        }
        else {



        Intent xx = new Intent(this, showoldwifi.class);
        xx.putExtra("foldername", markedtitle);
        startActivity(xx);
    }
        return 1;
   }
    public void finishnow(View v){

    }
    public  void finishnowblue(View v){

    }
    public int showoldsavedblue(View v){

        if (isTablet()){
            foldername=markedtitle;

            File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ foldername +"/blue.txt");

            if(!file.exists()){
                Toast.makeText(this, "Bluetooth devices weren`t scanned in this location", Toast.LENGTH_SHORT).show();
                return 0;

            }
            Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.activity_showoldblue);
            fragment.setArguments(arguments);
            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();
            TextView tt=(TextView)findViewById(R.id.blues);




            bluepoint="";
            Scanner in = null;
            try {
                in = new Scanner(new FileReader(file));
                while(in.hasNextLine() ) {
                    bluepoint=bluepoint+in.nextLine()+"\n";
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            finally {
                try { in.close() ; } catch(Exception e) {  }
            }
            tt.setText(bluepoint);
            if (bluepoint.length()==0){

                Toast.makeText(this, "Zero Bluetooth devices was found", Toast.LENGTH_SHORT).show();

            }


        }



        else {
            Intent xx = new Intent(this, showoldblue.class);
            xx.putExtra("foldername", markedtitle);
            startActivity(xx);
        }
       return 1;
    }

    public int listen(View v){
        if (isTablet()) {
            foldername = markedtitle;
            //foldername=b.getString("foldername");
            File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ foldername +"/audio.3gp");

            if(!file.exists()){
                Toast.makeText(this, "Audio wasnt recorded", Toast.LENGTH_SHORT).show();
                return 0;

            }


            mFileName =file.getAbsolutePath();


            //Bundle arguments = new Bundle();

            frag fragment = new frag(R.layout.activity_listenoldaudio);

            //fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainbox, fragment)
                    .commit();
        }
        else {


            Intent xx = new Intent(this, listenoldaudio.class);
            xx.putExtra("foldername", markedtitle);
            startActivity(xx);
        }
        return 1;

    }
    public void playstartold(View v) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void playstopold(View v) {
        mPlayer.release();
        mPlayer = null;
    }
    public void done(View v){
        //finish();
    }


    public int showoldpic(View v){
        //String file;
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ markedtitle +"/Attachment.jpg");

        if(!file.exists()){
            Toast.makeText(this, "Picture wasnt taken at this site", Toast.LENGTH_SHORT).show();
            return 0;
            }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
        return 1;

    }
    public void exitonclick(View v){
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewoldmarker, menu);
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
