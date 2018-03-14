package android.studio.inventoolworx;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David on 3/14/2018.
 */

public class UkuranFragment extends ListFragment implements AdapterView.OnItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] testArray1 = new String[UserData.mapUkuran.size()];
        HashMap<Integer,String> testMap1 = new HashMap<Integer, String>();
        Log.d("RMAP Entry", "onCreate: IM HERE BEFORE THE LOOP");
        for (int i = 0; i < UserData.mapUkuran.size(); i++)
        {
            testMap1.put(i,UserData.mapUkuran.get(i));
            testArray1[i] = UserData.mapUkuran.get(i);
            Log.d("RMAP Entry", "onCreate: IM HERE IN THE LOOP");
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, testArray1);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.numbers, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        Log.d("RMAP Entry", "onCreate: IM HERE AFTER THE LOOP");
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
        Log.d("JSON PARAMETER", "onItemClick: tipe="+UserData.userInput.get("tipe")+", ukuran="+((TextView) view).getText().toString());
        UserRequest.fetchData(
                getActivity(),
                DBConnection.INVENTORY_URL,
                new HashMap<String, String>() {{
                    put("function", "MASTERBAHAN");
                    put("tipe",UserData.userInput.get("tipe"));
                    put("ukuran",((TextView) view).getText().toString());
                }},
                new UserRequest.ServerCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("JSON SUCCESS", "onSuccess: " + result);
                        if (UserRequest.isJSONValid(result)) {
                            try {
                                JSONArray temp = new JSONArray(result);
//                                UserData.mapMerk = new ArrayList<String>();
//                                UserData.mapTipe = new ArrayList<String>();
//                                UserData.mapUkuran = new ArrayList<String>();
                                UserData.mapBahan = new ArrayList<String>();

                                for (int i = 0; i < temp.length(); i++) {
//                                    UserData.mapMerk.add(temp.getJSONObject(i).getString("merk"));
//                                    UserData.mapTipe.add(temp.getJSONObject(i).getString("tipe"));
//                                    UserData.mapUkuran.add(temp.getJSONObject(i).getString("ukuran"));
                                    UserData.mapBahan.add(temp.getJSONObject(i).getString("bahan"));
                                }

                                Intent intent = new Intent(getActivity(), BahanActivity.class);
                                intent.putExtra("ukuran", ((TextView) view).getText());
                                startActivity(intent);

                            } catch (JSONException e) {
                                Log.d("JSON ERROR", "onSuccess: " + e.getMessage());
                            }
                        }
                    }
                }
        );

    }
}