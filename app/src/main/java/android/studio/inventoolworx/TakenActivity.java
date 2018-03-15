package android.studio.inventoolworx;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TakenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken);
        TableLayout tl=(TableLayout)findViewById(R.id.maintable);
        tl.addView(CreateRow(new HashMap<String,String>(){{
            put("tipe","a");
            put("ukuran","b");
            put("bahan","c");
            put("merk","d");
            put("jumlah","e");
            put("lokasi","f");
        }}));
    }

    private TableRow CreateRow (Map<String,String> data)
    {
        TableRow result = new TableRow(this);
        result.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView text = new TextView(this);
        text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        //text.setBackgroundResource(R.drawable.border);
        text.setGravity(Gravity.RIGHT | Gravity.CENTER);
        text.setSingleLine(false);
        text.setPadding(10,10,10,10);
        text.setText("Tipe:\n"+ "Ukuran:\n"+ "Bahan:\n"+ "Merk:\n"+ "Jumlah:\n"+ "Lokasi:");
        result.addView(text);
        TextView fill = new TextView(this);
        fill.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 4f));
//        fill.setBackgroundResource(R.drawable.border);
        fill.setGravity(Gravity.LEFT | Gravity.CENTER);
        fill.setSingleLine(false);
        fill.setPadding(10,10,10,10);
        fill.setText(data.get("tipe")+"\n"+ data.get("ukuran")+"\n"+ data.get("bahan")+"\n"+ data.get("merk")+"\n"+ data.get("jumlah")+"\n"+ data.get("lokasi"));
        result.addView(fill);

        EditText taken = new EditText(this);
        taken.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        taken.setGravity(Gravity.CENTER | Gravity.CENTER);
        taken.setHint("0");
        result.addView(taken);
        Button cancel = new Button(this);

        cancel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cancel.setGravity(Gravity.CENTER | Gravity.CENTER);
        cancel.setText("X");

        result.addView(cancel);
        return result;
    }
}
