package devbhusal.iloggerproject;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class showoldblue extends AppCompatActivity {
 String foldername;
    String bluepoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showoldblue);

        TextView tt=(TextView)findViewById(R.id.blues);
        Bundle b = getIntent().getExtras();


        foldername=b.getString("foldername");

        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/"+ foldername +"/blue.txt");

        if(!file.exists()){
            Toast.makeText(this, "Bluetooth devices weren`t scanned in this location", Toast.LENGTH_SHORT).show();
            finish();

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showoldblue, menu);
        return true;
    }
    public  void finishnowblue(View v){
        finish();
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
