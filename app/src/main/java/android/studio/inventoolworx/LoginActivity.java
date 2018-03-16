package android.studio.inventoolworx;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private final int WAIT_LENGTH = 5000;
    private boolean loginResult = false;

    private JSONArray mUserData;
    public static String mEmail = "";
    public static String mLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            UserRequest.fetchData(
                    getApplicationContext(),
                    DBConnection.LOGIN_URL,
                    new HashMap<String, String>() {{
                        put("email", mEmailView.getText().toString().trim().toLowerCase());
                        put("password", mPasswordView.getText().toString().trim());
                    }},
                    new UserRequest.ServerCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (UserRequest.isJSONValid(result)) {
                                try {
                                    mUserData = new JSONArray(result);
                                    Log.d("Json Login", "LOGIN SUCCESS");
                                    Log.d("Json Login", "Email: " + mUserData.getJSONObject(0).getString("email"));
                                    UserData.setmEmail(mUserData.getJSONObject(0).getString("email"));
                                    UserData.setmLevel(mUserData.getJSONObject(0).getString("ulevel"));


                                    loginSuccess(Integer.parseInt(UserData.getmLevel(), 10));

                                } catch (JSONException e) {
                                    Log.d("Json ERROR", "Content: " + e.toString() + " -- Message: " + e.getMessage());
                                    Toast.makeText(getApplicationContext(), "JSON ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                                loginResult = true;
                            } else {
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                                Log.d("Json ERROR", "Login Failed");
                                showProgress(false);

                            }

                        }
                    }
            );
            //login();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 2s
                    if (loginResult == true)
                        loginSuccess(Integer.parseInt(UserData.getmLevel(),10));
                    else
                        showProgress(false);
                }
            }, WAIT_LENGTH);*/

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.equals(email.trim());
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    /*private boolean login() {
        //Getting values from edit texts
        final String email = mEmailView.getText().toString().trim().toLowerCase();
        final String password = mPasswordView.getText().toString().trim();

        *//*pDialog.setMessage("Login Process...");
        showDialog();*//*
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConnection.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d("Json Response", response);
                        //If we are getting success from server
                        if (response.length() > 10) {
                            try {
                                mUserData = new JSONArray(response);
                                loginResult = true;
                                Log.d("Json Response", "LOGIN SUCCESS");
                                Log.d("Json Response", "Email: " + mUserData.getJSONObject(0).getString("email"));
                                UserData.setmEmail(mUserData.getJSONObject(0).getString("email"));
                                UserData.setmLevel(mUserData.getJSONObject(0).getString("ulevel"));


                            } catch (JSONException e) {
                                Log.d("Json ERROR", "Content: " + e.toString() + " -- Message: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "JSON ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                            loginResult = true;
                        } else {
                            //Displaying an error message on toast
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                            Log.d("Json ERROR", "Login Failed");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "The server unreachable: " + error, Toast.LENGTH_LONG).show();
                        Log.d("Json Error", error.getMessage() + " -- " + error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("email", email);
                params.put("password", password);

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

        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
        return loginResult;
    }*/
    private void loginSuccess(int level) {
        //UserData.fetchDataInventory(getApplicationContext());
        UserRequest.fetchData(
                getApplicationContext(),
                DBConnection.INVENTORY_URL,
                new HashMap<String, String>() {{
                    put("function", "VIEW_ALL");
                    put("user", UserData.getmEmail());
                }},
                new UserRequest.ServerCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("JSON SUCCESS", "onSuccess: " + result);
                        if (UserRequest.isJSONValid(result)) {
                            try {
                                JSONArray mInventory = new JSONArray(result);

                                UserData.mapMerk = new ArrayList<String>();
                                UserData.mapTipe = new ArrayList<String>();
                                UserData.mapUkuran = new ArrayList<String>();
                                UserData.mapBahan = new ArrayList<String>();
                                UserData.mapJumlah = new ArrayList<String>();
                                UserData.mapLokasi = new ArrayList<String>();

                                Log.d("Json Response:Inventory", "INVENTORY FETCH SUCCESS : " + mInventory.length() + "DATA");
                                for (int i = 0; i < mInventory.length(); i++) {

                                    UserData.mapMerk.add(mInventory.getJSONObject(i).getString("merk"));
                                    UserData.mapTipe.add(mInventory.getJSONObject(i).getString("tipe"));
                                    UserData.mapUkuran.add(mInventory.getJSONObject(i).getString("ukuran"));
                                    UserData.mapBahan.add(mInventory.getJSONObject(i).getString("bahan"));
                                    UserData.mapJumlah.add(mInventory.getJSONObject(i).getString("jumlah"));
                                    UserData.mapLokasi.add(mInventory.getJSONObject(i).getString("lokasi"));
                                    UserData.valSpinner.add(mInventory.getJSONObject(i).getString("merk") + "," + mInventory.getJSONObject(i).getString("tipe") + "," + mInventory.getJSONObject(i).getString("ukuran") + "," + mInventory.getJSONObject(i).getString("bahan") + "," + mInventory.getJSONObject(i).getString("lokasi"));
                                    UserData.mapSpinner.put(i,mInventory.getJSONObject(i).getString("id"));
                                    Log.d("Json Response:Inventory", "i = " + i + "\t" + mInventory.getJSONObject(i).getString("merk") + "\t" + mInventory.getJSONObject(i).getString("tipe") + "\t" + mInventory.getJSONObject(i).getString("ukuran") + "\t" + mInventory.getJSONObject(i).getString("bahan") + "\t" + mInventory.getJSONObject(i).getString("jumlah") + "\t" + mInventory.getJSONObject(i).getString("lokasi"));
                                }
                            } catch (JSONException e) {
                                Log.d("Json ERROR", "Content: " + e.toString() + " -- Message: " + e.getMessage());
                            }
                        } else {
                            Log.d("Json ERROR", "Login Failed");
                        }
                        if (Integer.parseInt(UserData.getmLevel()) == 1) {
                            Intent intent = new Intent(LoginActivity.this, TakenActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, TakenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
        );

    }
}

