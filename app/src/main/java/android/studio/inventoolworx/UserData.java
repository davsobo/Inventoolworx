package android.studio.inventoolworx;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IT-PC2 on 27/02/2018.
 */

public class UserData {
    private static String mEmail;
    private static String mLevel;

    public static ArrayList<String> mapMerk;
    public static ArrayList<String> mapTipe;
    public static ArrayList<String> mapUkuran;
    public static ArrayList<String> mapBahan;
    public static ArrayList<String> mapJumlah;
    public static ArrayList<String> mapLokasi;

    public static Map<String,String> userInput = new HashMap<>();
    public static Map<Integer,Map<String,String>> listUserInput = new HashMap<Integer, Map<String, String>>();
    public static int nListUserInput = 0;
    //Map<String,String> nambahtambah = new HashMap<>();

    /**
     * Use This Instead for the Spinner SH*T
     */
    public static ArrayList<String> valSpinner = new ArrayList<String>();
    public static Map<Integer, String> mapSpinner = new HashMap<Integer, String>();

//    public static Map<String, Map<String, String>> mapInventory = new HashMap<String, Map<String, String>>();


    public static void setmEmail(String x) {
        mEmail = x;
    }

    public static String getmEmail() {
        return mEmail;
    }

    public static void setmLevel(String x) {
        mLevel = x;
    }

    public static String getmLevel() {
        return mLevel;
    }

    public static void fetchDataInventory(Context context) {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConnection.INVENTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d("Json Response: Inventory", response);
                        //If we are getting success from server
                        if (response.length() > 10) {
                            try {
                                JSONArray mInventory = new JSONArray(response);

                                Log.d("LOL", response);

                                mapMerk = new ArrayList<String>();
                                mapTipe = new ArrayList<String>();
                                mapUkuran = new ArrayList<String>();
                                mapBahan = new ArrayList<String>();
//                                mapJumlah = new ArrayList<String>();
//                                mapLokasi = new ArrayList<String>();
                                Log.d("Json Response: Inventory", "INVENTORY FETCH SUCCESS : " + mInventory.length() + "DATA");
                                for (int i = 0; i < mInventory.length(); i++) {
                                    /*String temp = mInventory.getJSONObject(i).getString("merk") +  "\t" +  mInventory.getJSONObject(i).getString("tipe") +  "\t" +  mInventory.getJSONObject(i).getString("ukuran") +  "\t" +  mInventory.getJSONObject(i).getString("bahan") +  "\t" +  mInventory.getJSONObject(i).getString("jumlah") +  "\t" +  mInventory.getJSONObject(i).getString("lokasi");
                                    valSpinner.add(temp);
                                    mapSpinner.put(i,mInventory.getJSONObject(i).getString("id"));*/

                                    mapMerk.add(mInventory.getJSONObject(i).getString("merk"));
                                    mapTipe.add(mInventory.getJSONObject(i).getString("tipe"));
                                    mapUkuran.add(mInventory.getJSONObject(i).getString("ukuran"));
                                    mapBahan.add(mInventory.getJSONObject(i).getString("bahan"));
//                                    mapJumlah.add(mInventory.getJSONObject(i).getString("jumlah"));
//                                    mapLokasi.add(mInventory.getJSONObject(i).getString("lokasi"));
                                    Log.d("Json Response: Inventory", "i = " + i + "\t" + mInventory.getJSONObject(i).getString("merk") + "\t" + mInventory.getJSONObject(i).getString("tipe") + "\t" + mInventory.getJSONObject(i).getString("ukuran") + "\t" + mInventory.getJSONObject(i).getString("bahan"));
                                }
                                mapTipe= UserRequest.removeDuplicates(mapTipe);
                                mapUkuran= UserRequest.removeDuplicates(mapUkuran);
                                mapBahan = UserRequest.removeDuplicates(mapBahan);
                                mapMerk = UserRequest.removeDuplicates(mapMerk);
                                //mapInventory.put("merk", mapMerk);
//                                mapInventory.put("tipe", mapTipe);
//                                mapInventory.put("ukuran", mapUkuran);
//                                mapInventory.put("bahan", mapBahan);
//                                mapInventory.put("jumlah", mapJumlah);
//                                mapInventory.put("lokasi", mapLokasi);

//                                Log.d("Json Response: Inventory", "Email: " + mUserData.getJSONObject(0).getString("email"));
                                /*for (Map.Entry<String,String> temp : mInventory.EntrySet()) {
                                    
                                }*/

                            } catch (JSONException e) {
                                Log.d("Json ERROR", "Content: " + e.toString() + " -- Message: " + e.getMessage());
                                //Toast.makeText(getApplicationContext(), "JSON ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                        } else {
                            //Displaying an error message on toast
                            //Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                            Log.d("Json ERROR", "Login Failed");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "The server unreachable: "+error, Toast.LENGTH_LONG).show();
                        Log.d("Json Error", error.getMessage() + " -- " + error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("function", "VIEW_ALL");
//                params.put("password", password);

                //returning parameter
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cookie", DBConnection.COOKIE);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

//    ==================================================================
//    ==================================================================
//    ===================TEST CAllBACK FOR VOLLEY=======================
//    ==================================================================
//    ==================================================================

    /**
     * NOTE: ISI VARIABLE remainMap DULU BARU DIJALANIN remainDataInventory nya
     * "merk"
     * "tipe"
     * "ukuran"
     * "bahan"
     */
    public interface VolleyCallback {
        void onSuccess(String string);
    }

    public static Map<String, String> remainMap = new HashMap<>();
    private static int remain = 0;

    public static Integer remainDataInventory(Context context, final VolleyCallback callback) {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConnection.INVENTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d("Json Response: Remains", response);
                        //If we are getting success from server
                        if (response.length() > 10) {
                            try {
                                JSONArray mInventory = new JSONArray(response);
                                JSONObject data = new JSONObject();
                                data = mInventory.getJSONObject(0);
                                remain = Integer.parseInt(data.getString("jumlah"));

                            } catch (JSONException e) {
                                Log.d("Json ERROR", "Content: " + e.toString() + " -- Message: " + e.getMessage());
                            }
                        } else {
                            //Displaying an error message on toast
                            //Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                            Log.d("Json ERROR", "Login Failed");

                        }
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "The server unreachable: "+error, Toast.LENGTH_LONG).show();
                        Log.d("Json Error", error.getMessage() + " -- " + error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("function", "VIEW_ALL");
                params.put("merk", remainMap.get("merk"));
                params.put("tipe", remainMap.get("tipe"));
                params.put("ukuran", remainMap.get("ukuran"));
                params.put("bahan", remainMap.get("bahan"));

//                params.put("password", password);

                //returning parameter
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cookie", DBConnection.COOKIE);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
        return remain;
    }


}
