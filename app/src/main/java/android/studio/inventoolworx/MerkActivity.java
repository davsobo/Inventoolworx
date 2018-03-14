package android.studio.inventoolworx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MerkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merk);
        Intent intent = getIntent();
        String b = intent.getExtras().getString("bahan");
        UserData.userInput.put("bahan",b);
        Log.d("leeloo", "b=" +b);
    }

}
