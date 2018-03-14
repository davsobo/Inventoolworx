package android.studio.inventoolworx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("JSON MAINACTIVITY", "Try WebView");
        WebView myWebView = (WebView) findViewById(R.id.webview);
        Log.d("JSON MAINACTIVITY", "Make WebView");
        WebSettings webSettings = myWebView.getSettings();
        Log.d("JSON MAINACTIVITY", "Make WebSetting");
        webSettings.setJavaScriptEnabled(true);
        Log.d("JSON MAINACTIVITY", "Try JavaScript");
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("JSON OPF", "onPageFinished: " + url);
                DBConnection.setCOOKIE(getCookie("http://invento.html-5.me/", "__test"));
                Log.d("JSON COOKIE", DBConnection.COOKIE);

                UserData.remainMap.put("merk", "");
                UserData.remainMap.put("tipe", "");
                UserData.remainMap.put("ukuran", "");
                UserData.remainMap.put("bahan", "");

                //UserData.fetchDataInventory(getApplicationContext());

                UserData.remainDataInventory(getApplicationContext(), new UserData.VolleyCallback() {
                    @Override
                    public void onSuccess(String string) {
                        Log.d("JSON SUCCESS", "onSuccess: HERE i AM as A success");
                    }
                });

                UserRequest.fetchData(
                        getApplicationContext(),
                        DBConnection.INVENTORY_URL,
                        new HashMap<String, String>() {{
                            put("function", "MASTERTIPE");
                        }},
                        new UserRequest.ServerCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("JSON SUCCESS", "onSuccess: " + result);
                                if (UserRequest.isJSONValid(result)) {
                                    try {
                                        JSONArray temp = new JSONArray(result);
//                                        UserData.mapMerk = new ArrayList<String>();
                                        UserData.mapTipe = new ArrayList<String>();
//                                        UserData.mapUkuran = new ArrayList<String>();
//                                        UserData.mapBahan = new ArrayList<String>();

                                        for (int i = 0; i < temp.length(); i++) {
//                                            UserData.mapMerk.add(temp.getJSONObject(i).getString("merk"));
                                            UserData.mapTipe.add(temp.getJSONObject(i).getString("tipe"));
//                                            UserData.mapUkuran.add(temp.getJSONObject(i).getString("ukuran"));
//                                            UserData.mapBahan.add(temp.getJSONObject(i).getString("bahan"));
//                                            Log.d("Json: Inventory", "i = " + i + "\t" + temp.getJSONObject(i).getString("merk") + "\t" + temp.getJSONObject(i).getString("tipe") + "\t" + temp.getJSONObject(i).getString("ukuran") + "\t" + temp.getJSONObject(i).getString("bahan"));
                                        }
                                    } catch (JSONException e) {
                                        Log.d("JSON ERROR", "onSuccess: " + e.getMessage());
                                    }
//                                    UserData.mapTipe= UserRequest.removeDuplicates(UserData.mapTipe);
//                                    UserData.mapUkuran= UserRequest.removeDuplicates(UserData.mapUkuran);
//                                    UserData.mapBahan = UserRequest.removeDuplicates(UserData.mapBahan);
//                                    UserData.mapMerk = UserRequest.removeDuplicates(UserData.mapMerk);
                                    ok = true;
                                }
                            }
                        }
                );

            }
        });
        Log.d("JSON MAINACTIVITY", "SetChromeClient");
        myWebView.loadUrl("http://invento.html-5.me/login.php");
        Log.d("JSON MAINACTIVITY", "Try Load URL");
        //DBConnection.setCOOKIE(getCookie("http://invento.html-5.me/","__test"));

    }

    //*****-----Ini buat ganti ke page berikutnya-----*****//
    public void sendMessage(View view) {
        if (ok == true) {
            Intent intent = new Intent(MainActivity.this, JenisActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;
        CookieManager cookieManager = CookieManager.getInstance();

        String cookies = cookieManager.getCookie(siteName);
        //cookieManager.removeAllCookies(null);
        String[] temp = cookies.split(";");
        for (String ar1 : temp) {
            if (ar1.contains(CookieName)) {
                String[] temp1 = ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }
}
