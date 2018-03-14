package android.studio.inventoolworx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BahanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan);
        Intent intent = getIntent();
        String u = intent.getExtras().getString("ukuran");
        UserData.userInput.put("ukuran",u);
        Log.d("leeloo", "u=" +u);
    }
}
