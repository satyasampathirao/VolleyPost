package com.satman.satya.volleypost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etei, etepw;
    String sei, sepw;

    boolean isconnected;

    String empid, emploginid, empname, empdesig,empemail ;

    String url = "Paste your URL here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


       // session = new UserSession(getApplicationContext());

//        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();



        etei = (EditText) findViewById(R.id.etei);
        etepw = (EditText) findViewById(R.id.etepw);



    }



    public void emplogin(View view){

        sei = etei.getText().toString().trim();
        sepw = etepw.getText().toString().trim();

        if (TextUtils.isEmpty(sei)){

            etei.setError("Please Enter Employee ID");
            etei.requestFocus();


        }else if (sei.length() <6 ){

            etei.setError("Employee ID too short");
            etei.requestFocus();


        }else if (TextUtils.isEmpty(sepw)){

            etepw.setError("Please enter password");
            etepw.requestFocus();

        } else if (sepw.length() <3 ){

            etepw.setError("Password too short");
            etepw.requestFocus();


        } else {

//            Toast.makeText(getApplicationContext(), sei+"\n"+sepw,Toast.LENGTH_SHORT).show();

            isconnected = isNetworkConnected();

            if (isconnected==true){
                sendlogin();
            }else {
                Toast.makeText(getApplicationContext(),"please connect to Internet",Toast.LENGTH_SHORT).show();
            }




        }





    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void sendlogin(){


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.d("empres",response);
                        pDialog.hide();
                        JSONObject json = null;

                        //    String k = response;

                        try {
                            json = new JSONObject(response);
                            JSONObject e = json.getJSONObject("Employeelogin");
                            String statuscode = e.getString("StatusCode");
                            String statusmessage = e.getString("StatusText");

                            //     Toast.makeText(getApplicationContext(),statuscode+"\n"+statusmessage,Toast.LENGTH_SHORT).show();

                            if (statuscode.equals("200")) {


                                Log.d("testotp",response+"\n\n"+statuscode);

                                // Toast.makeText(getApplicationContext(),statusmessage,Toast.LENGTH_SHORT).show();

                                //JSONArray Data = e.getJSONArray("Data");

                                JSONObject Data = e.getJSONObject("Data");

                                //empid, emploginid, empname, empimage, empdesig,empemail;

                                empid = Data.getString("EmployeeID");
                                empname =Data.getString("EmployeeNAME");
                                emploginid = Data.getString("EmployeeUniqueId");
                                empemail= Data.getString("EmployeeEMAIL");
                                empdesig=Data.getString("EmployeeDESIG");



                                Log.d("empgood", empid+"\n"+empname+"\n"+empdesig);



                                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                               /* Intent inst = new Intent(getApplicationContext(),HomeActivity.class);
                                inst.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(inst);*/

                            }  else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), statusmessage , Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                // params.put("name", nameString);
                params.put("usernamekey", sei);
                params.put("passwordkey", sepw);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);





    }















}
