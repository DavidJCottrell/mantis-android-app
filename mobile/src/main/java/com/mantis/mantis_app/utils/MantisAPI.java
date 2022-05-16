package com.mantis.mantis_app.utils;

import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mantis.mantis_app.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MantisAPI {
    String baseURL = "http://10.0.2.2:9000"; // Points to port 9000 of local machine

    RequestQueue queue;
    AppCompatActivity context;

    public MantisAPI(AppCompatActivity currentContext){
        context = currentContext;
        queue = Volley.newRequestQueue(context);
    }

    public interface VolleyObjectCallback{ void onSuccess(JSONObject result); }
    public interface VolleyArrayCallback{ void onSuccess(JSONArray result); }

    void onAPIErrorFunc(VolleyError error){
        if(error instanceof AuthFailureError){
            Auth.logout(context);
        }else{
            try {
                if(error.networkResponse != null){
                    // API error alert dialog
                    String errorResponse = new String(error.networkResponse.data, "utf-8").replace("\"", "");
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage(errorResponse);
                    alert.setCancelable(true);
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(JSONObject body){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                baseURL + "/users/login/",
                body,
                response -> {
                    try {
                        String token = response.getString("token");
                        User.parseUserData(response);
                        Auth.login(this.context, token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, this::onAPIErrorFunc);
        queue.add(jsonObjectRequest);
    }

    public void signUp(JSONObject body){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                baseURL + "/users/register/",
                body,
                response -> {
                    try {
                        String token = response.getString("token");
                        User.parseUserData(response.getJSONObject("user"));
                        Auth.login(this.context, token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, this::onAPIErrorFunc
        );
        queue.add(jsonObjectRequest);
    }

    public void arrayReq(String route, int method, JSONArray body, String token, final VolleyArrayCallback callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method, baseURL + route, body,
                callback::onSuccess,
                this::onAPIErrorFunc
        )
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("auth-token", token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void objReq(String route, int method, JSONObject body, String token, final VolleyObjectCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, baseURL + route, body,
                callback::onSuccess,
                this::onAPIErrorFunc
        )
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("auth-token", token);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
