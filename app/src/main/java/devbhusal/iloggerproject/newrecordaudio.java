package devbhusal.iloggerproject;


  import android.content.Context;
        import android.location.Location;
        import android.location.LocationManager;
        import android.media.AudioFormat;
        import android.media.AudioRecord;
        import android.media.ExifInterface;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;

public class newrecordaudio extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    // private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;





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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecordaudio);
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data");
        if (!folder1.exists()) folder1.mkdir();
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (!folder2.exists()) folder2.mkdir();
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp/audio.3gp");



        mFileName =file.getAbsolutePath();
        //mFileName =
    }






 public void savemusic(View f){
     File imgFile = new  File(Environment.getExternalStorageDirectory(), "/ilogger_data/temp/audio.3gp");

     if(imgFile.exists()){
         finish();
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
