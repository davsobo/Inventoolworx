package android.studio.inventoolworx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String m = intent.getExtras().getString("merk");
        UserData.userInput.put("merk",m);
        Log.d("leeloo", "m=" +m);
        Log.d("leeloo", "userInput: " +UserData.userInput.toString());
        TextView jenisText = (TextView)findViewById(R.id.jenisHasil);
        TextView ukuranText = (TextView)findViewById(R.id.ukuranHasil);
        TextView bahanText = (TextView)findViewById(R.id.bahanHasil);
        TextView merkText = (TextView)findViewById(R.id.merkHasil);
        jenisText.setText(UserData.userInput.get("tipe"));
        ukuranText.setText(UserData.userInput.get("ukuran"));
        bahanText.setText(UserData.userInput.get("bahan"));
        merkText.setText(UserData.userInput.get("merk"));
        UserRequest.fetchData( getApplicationContext(),
                DBConnection.INVENTORY_URL,
                new HashMap<String, String>() {{
                    put("function", "REMAIN");
                    put("tipe", UserData.userInput.get("tipe"));
                    put("ukuran", UserData.userInput.get("ukuran"));
                    put("bahan", UserData.userInput.get("bahan"));
                    put("merk", UserData.userInput.get("merk"));
                }},
                new UserRequest.ServerCallback() {

                    @Override public void onSuccess(String result)
                    {
                        Log.d("JSON SUCCESS", "onSuccess: "+result);
                        if (UserRequest.isJSONValid(result)){
                            try{
                                JSONArray temp = new JSONArray(result);
                                String sisa = temp.getJSONObject(0).getString("jumlah");
                                String lokasi = temp.getJSONObject(0).getString("lokasi");
                                TextView lokasiText = (TextView) findViewById(R.id.lokasiHasil);
                                TextView sisaText = (TextView)findViewById(R.id.sisaHasil);
                                sisaText.setText(sisa);
                                lokasiText.setText(lokasi);
                                UserData.userInput.put("jumlah",sisa);
                                UserData.userInput.put("lokasi",lokasi);
                            }
                            catch (JSONException e)
                            {
                                Log.d("JSON ERROR", "onSuccess: JSON ERROR ="+e.getMessage());
                            }

                        }
                    }
                }
      );

    }

    public void awyiss (View view){
        Map<String,String> a = new HashMap<String,String>();
        a.put("tipe",UserData.userInput.get("tipe"));
        a.put("bahan",UserData.userInput.get("bahan"));
        a.put("ukuran",UserData.userInput.get("ukuran"));
        a.put("jumlah",UserData.userInput.get("jumlah"));
        a.put("lokasi",UserData.userInput.get("lokasi"));
        a.put("merk",UserData.userInput.get("merk"));
        UserData.listUserInput.add(a);
        Log.d("JSON INPUTSTRING",UserData.userInput.toString());
        UserData.nListUserInput++;
        //UserData.userInput.clear();
        Intent intent = new Intent(ResultActivity.this, TakenActivity.class);
        startActivity(intent);
        finish();
    }
    protected void awnouh (View view){
        //UserData.userInput.clear();
        Intent intent = new Intent(ResultActivity.this, TakenActivity.class);
        startActivity(intent);
        finish();
    }
}

