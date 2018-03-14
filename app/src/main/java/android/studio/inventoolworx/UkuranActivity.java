package android.studio.inventoolworx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class UkuranActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String j = intent.getExtras().getString("jenis");
        UserData.userInput.put("tipe",j);
        Log.d("leeloo", "j=" +j);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ukuran);

    }
}
