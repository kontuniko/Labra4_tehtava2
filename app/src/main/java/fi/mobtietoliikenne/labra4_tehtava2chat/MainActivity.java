package fi.mobtietoliikenne.labra4_tehtava2chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.enums.ReadyState;
import org.w3c.dom.Text;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EchoClientInterface {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    EchoClient echoClient; //Jäsenmuuttuja
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView =(ListView) findViewById(R.id.lvMessageView);
        String[] items = {};
        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this,R.layout.list_items,R.id.textInput,arrayList);
        listView.setAdapter(adapter);


        findViewById(R.id.btnCpenConnection).setOnClickListener(this);
        findViewById(R.id.btnCloseConnection).setOnClickListener(this);
        findViewById(R.id.btnSendMessage).setOnClickListener(this);
        Button btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCpenConnection){
            try {
                echoClient = new EchoClient(new URI("wss://obscure-waters-98157.herokuapp.com"),this);
                echoClient.connect();
                Button btnSendMessage = findViewById(R.id.btnSendMessage);
                btnSendMessage.setEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (v.getId() == R.id.btnCloseConnection){
            if(echoClient != null){
                echoClient.close();
            }
            else{
                Toast.makeText(this,"There is no connection to close",Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == R.id.btnSendMessage) {
            if (echoClient != null && echoClient.getReadyState() == ReadyState.OPEN) {
                EditText etMessageView = findViewById(R.id.etMessageView);
                echoClient.send(etMessageView.getText().toString());
                etMessageView.setText("");
                }
            else{
                Toast.makeText(this,"There is no connection! Please,make the connection first!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMessage(final String message) {     //
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayList.add(message);
                adapter.notifyDataSetChanged();

                //TextView tvMessageView = findViewById(R.id.lvMessageView);
                //tvMessageView.setText(message);
            }
        });
    }

    @Override
    public void onStatusChange(final String newStatus) {
        runOnUiThread(new Runnable() {  //Metodikutsu, jolla varmistetaan,että käyttöliittymän päivitys toimii oikein
            @Override
            public void run() {
                TextView tvStatus = findViewById(R.id.tvStatus);
                tvStatus.setText(newStatus);
            }
        });
    }
}
