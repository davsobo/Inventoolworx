package android.studio.inventoolworx;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TakenActivity extends AppCompatActivity {
    private boolean go = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken);
        TableLayout tl=(TableLayout)findViewById(R.id.maintable);
        Log.d("JSON Create T", "onCreate: "+UserData.listUserInput.toString());
        for(Map<String, String> temp: UserData.listUserInput)
        {
            Log.d("JSON T DATA", "onCreate: "+temp);
            tl.addView(CreateRow(temp));
        }
        /*tl.addView(CreateRow(new HashMap<String,String>(){{
            put("tipe","a");
            put("ukuran","b");
            put("bahan","c");
            put("merk","d");
            put("jumlah","e");
            put("lokasi","f");
        }}));*/
    }

    private TableRow CreateRow (final Map<String,String> data)
    {
        Log.d("JSON DATA", "CreateRow: "+data);
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
        text.setMovementMethod(new ScrollingMovementMethod());
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
        final EditText taken = new EditText(this);
        taken.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, trHeight, 1f));
//        taken.setBackgroundResource(R.drawable.border);
        taken.setGravity(Gravity.CENTER);
        taken.setHint(data.get("jumlah"));
        taken.setText(data.get("jumlah"));
        taken.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        taken.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {



            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(UserData.getmLevel().equals("1"))
                {
                    if (taken.getText().toString().length() <= 0) {
                        taken.setError("Enter Number");
                        go = false;
                    } else {
                        taken.setError(null);
                        data.put("jumlah",taken.getText().toString());
                        go = true;
                    }
                }
                else if(UserData.getmLevel().equals("2"))
                {
                    if (taken.getText().toString().compareTo(data.get("jumlah")) > 0) {
                        taken.setError("Masukan Angka Kurang Dari Jumlah");
                        go = false;
                    } else if (taken.getText().toString().length() <= 0) {
                        taken.setError("Masukan Angka");
                        go = false;
                    }
                    else {
                        taken.setError(null);
                        data.put("jumlah",taken.getText().toString());
                        go = true;
                    }
                }
            }
        });
        result.addView(taken);
        Button cancel = new Button(this);

        /**Batal*/
        cancel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, trHeight, 1f));
        cancel.setGravity(Gravity.CENTER );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.listUserInput.remove(UserData.listUserInput.indexOf(data));
                View row = (View) view.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        });
        cancel.setText("X");
        result.addView(cancel);

        return result;
    }
    public void widgetTambah(View view)
    {
        Intent intent = new Intent(TakenActivity.this, JenisActivity.class);
        startActivity(intent);
        finish();
    }
    public void nhentai(View view )
    {
        if(go)
        {
            final JSONArray json = new JSONArray(UserData.listUserInput);
            UserRequest.fetchData(
                    getApplicationContext(),
                    DBConnection.INVENTORY_URL,
                    new HashMap<String, String>() {{
                        put("function", "BATCHUPDATE");
                        put("json", json.toString());
                    }},
                    new UserRequest.ServerCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d("JSON SUCCESS", "onSuccess: " + result);
                            Intent intent = new Intent(TakenActivity.this, SaveActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }
        else if(UserData.listUserInput.size() <= 0)
        {
            Toast.makeText(getApplicationContext(), "Tidak ada Data",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Benarkan input anda",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void fakku(View view)
    {
        UserData.listUserInput.clear();
        Intent intent = new Intent(TakenActivity.this, TakenActivity.class);
        startActivity(intent);
        finish();
    }
}
