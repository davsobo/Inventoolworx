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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IT-PC2 on 01/03/2018.
 */

public class UserRequest {
    /**
     * ++++++++++++++++++USAGE+++++++++++++++++++++
     * <p>
     * UserRequest.fetchData(
     * getApplicationContext(),
     * <URL></URL>,
     * new HashMap<String, String>() {{
     * put("function", "VIEW_ALL");
     * <PARAMETERS></PARAMETERS>
     * }},
     * new UserRequest.ServerCallback() {
     *
     * @Override public void onSuccess(String result) {
     * Log.d("JSON SUCCESS", "onSuccess: "+result);
     * <DO SOMETHING AFTER SUCCESS>
     * }
     * }
     * );
     */

    public interface ServerCallback {
        void onSuccess(String result);
    }


    public static void fetchData(Context pContext, String pURL, final Map<String, String> pParameter, final ServerCallback callback) {
        Log.d("JSON REQUEST", "Request");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d("Json Response", response);
                        callback.onSuccess(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "The server unreachable: "+error, Toast.LENGTH_LONG).show();
                        Log.d("Json Error", error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = pParameter;

                return params;
//                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Cookie", DBConnection.COOKIE);
                return params;
//                return headers;
            }
        };
        Volley.newRequestQueue(pContext).add(stringRequest);
    }

    public static boolean isJSONValid(String test) {

        try {
            new JSONObject(test);
            return true;
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
                return true;
            } catch (JSONException ex1) {
                return false;
            }
        }
    }


}
