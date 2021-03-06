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

import java.util.HashMap;

/**
 * Created by David on 3/14/2018.
 */

public class MerkFragment extends ListFragment implements AdapterView.OnItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] testArray1 = new String[UserData.mapMerk.size()];
        HashMap<Integer,String> testMap1 = new HashMap<Integer, String>();
        Log.d("RMAP Entry", "onCreate: IM HERE BEFORE THE LOOP");
        for (int i = 0; i < UserData.mapMerk.size(); i++)
        {
            testMap1.put(i,UserData.mapMerk.get(i));
            testArray1[i] = UserData.mapMerk.get(i);
            Log.d("RMAP Entry", "onCreate: IM HERE IN THE LOOP");
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, testArray1);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.numbers, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        Log.d("RMAP Entry", "onCreate: IM HERE AFTER THE LOOP");
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("merk", ((TextView) view).getText());
        startActivity(intent);
    }
}