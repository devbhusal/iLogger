package devbhusal.iloggerproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class listenoldaudio extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    // private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    public String foldername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listenoldaudio);
        Bundle b = getIntent().getExtras();


        foldername=b.getString("foldername");
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ foldername +"/audio.3gp");

        if(!file.exists()){
            Toast.makeText(this, "Audio wasnt recorded", Toast.LENGTH_SHORT).show();
            finish();

        }


        mFileName =file.getAbsolutePath();
    }

public void done(View v){
    finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listenoldaudio, menu);
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
