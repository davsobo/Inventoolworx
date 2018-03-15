package android.studio.inventoolworx;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
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
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int trHeight = (int) (80 * scale + 0.5f);
        int trWidth = (int) (67 * scale + 0.5f);
        TableRow result = new TableRow(this);
//        TableRow.LayoutParams tlparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,trHeight);
        TableRow.LayoutParams tlparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, trHeight);
        result.setLayoutParams(tlparams);
//      result.setMinimumHeight(100);
//      result.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        /**Label*/
        TextView text = new TextView(this);
        text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 6f));
        text.setGravity(Gravity.CENTER | Gravity.TOP);
        text.setMinHeight(300);
        text.setSingleLine(false);
        text.setText("Tipe:"+ data.get("tipe")+
                "\nUkuran:"+ data.get("ukuran")+
                "\nBahan:"+ data.get("bahan")+
                "\nMerk:"+  data.get("merk")+
                "\nJumlah:"+ data.get("jumlah")+
                "\nLokasi:" + data.get("lokasi"));
//        text.setText("Tipe:\n"+ "Ukuran:\n"+ "Bahan:\n"+ "Merk:\n"+ "Jumlah:\n"+ "Lokasi:");
        result.addView(text);

//        /**Item*/
//        TextView fill = new TextView(this);
//        fill.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2f));
//        fill.setGravity(Gravity.LEFT | Gravity.CENTER| Gravity.TOP);
//        fill.setSingleLine(false);
//        text.setMinHeight(150);
//        fill.setText(data.get("tipe")+"\n"+ data.get("ukuran")+"\n"+ data.get("bahan")+"\n"+ data.get("merk")+"\n"+ data.get("jumlah")+"\n"+ data.get("lokasi"));
//        result.addView(fill);

        /**Ambil*/
        EditText taken = new EditText(this);
        taken.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, trHeight, 1f));
//        taken.setBackgroundResource(R.drawable.border);
        taken.setGravity(Gravity.CENTER);
        taken.setHint("0");
        result.addView(taken);
        Button cancel = new Button(this);

        /**Batal*/
        cancel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, trHeight, 1f));
        cancel.setGravity(Gravity.CENTER );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setText("X");
        result.addView(cancel);

        return result;
    }
}
