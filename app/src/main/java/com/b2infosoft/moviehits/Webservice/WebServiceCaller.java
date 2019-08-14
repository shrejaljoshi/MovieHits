package com.b2infosoft.moviehits.Webservice;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.moviehits.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public abstract class WebServiceCaller extends AsyncTask<String, Integer, String> {

    public static final int POST_TASK = 1;
    public static final int GET_TASK = 2;
    public static final int POST_TASK_Log = 3;
    public static final int GET_TASK_Log = 4;
    // connection timeout, in milliseconds (waiting to connect)
    private static final int CONN_TIMEOUT = 60000;
    // socket timeout, in milliseconds (waiting for data)
    private static final int SOCKET_TIMEOUT = 60000;
    private static String TAG = "=Shoukin==";
    static Context mContext;
    HttpEntity entity;

    String weburls = "";
    HttpClient httpclient;
    private int taskType = GET_TASK;
    private String processMessage = "Processing...";
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private Dialog pDlg = null;
    String token = "";
    private boolean DialogYesNo;


    public WebServiceCaller(int taskType, Context mContext, String processMessage, boolean DialogYesNo) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.DialogYesNo = DialogYesNo;
        TAG = mContext.getClass().getCanonicalName();

    }

    public void addNameValuePair(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
        //   Log.d(TAG, "=params====" + params);
    }

    public void addEntity(HttpEntity entity) {
        this.entity = entity;
    }

    public void addNameValuePairArray(ArrayList<NameValuePair> params) {

        this.params = params;
    }

    public void addJsonPair(String name, String value) {

        params.add(new BasicNameValuePair(name, value));
    }

    private void showProgressDialog() {


        if (processMessage.equalsIgnoreCase("Please wait...")) {
            processMessage = "Loading...";
        }
        pDlg = new Dialog(mContext, R.style.Theme_AppCompat_Dialog_Alert);

        Drawable d = new ColorDrawable(Color.TRANSPARENT);
        d.setAlpha(200);
        pDlg.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pDlg.getWindow().setBackgroundDrawable(d);
        pDlg.setContentView(R.layout.progress_dialog);
        TextView tvMessage;
        tvMessage = pDlg.findViewById(R.id.tvMessage);
        tvMessage.setText(processMessage);
        pDlg.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pDlg.create();
        }
        pDlg.show();
    }

    @Override
    protected void onPreExecute() {
        // hideKeyboard()
        if (DialogYesNo) {

            if (!((Activity) mContext).isFinishing()) {
                //show dialog
                showProgressDialog();
            }

        }

    }

    private HttpParams getHttpParams() {
        HttpParams htpp = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
        return htpp;
    }

    private HttpResponse doResponse(String url) {
        // Use our connection and data timeouts as parameters for our
        // DefaultHttpClient
        //  System.out.println(TAG+ "=Method====" + String.valueOf(taskType)+ "====url====" + url);
        httpclient = new DefaultHttpClient(getHttpParams());
        HttpResponse response = null;
        //  HttpPost httppost = null;

        token = "8cbaa15d8f9a73b686594fbdccce84ca36878cee ";

        try {
            switch (taskType) {
                case POST_TASK:

                    HttpPost httppost = new HttpPost(url);
                    if (params.size() > 0) {
                         System.out.println("params====" + String.valueOf(params));
                        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    }
                    if (entity != null) {
                        httppost.setEntity(entity);
                    }
                    response = httpclient.execute(httppost);
                    break;
                case POST_TASK_Log:
                    httppost = new HttpPost(url);
                    httppost.setHeader("User-Agent", "Mozilla");

                    if (params.size() > 0) {
                        System.out.println("params====" + String.valueOf(params));
                        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    }
                    if (entity != null) {
                        //       System.out.println("entity====" + String.valueOf(entity));
                        httppost.setEntity(entity);
                    }
                    response = httpclient.execute(httppost);
                    break;
                case GET_TASK:
                    System.out.println("params====" + String.valueOf(params));
                    System.out.println("user_token==get==" +token);
                    HttpGet httpget = new HttpGet(url);
                    httpget.addHeader("Authorization", "Bearer" + token );
                    response = httpclient.execute(httpget);
                    break;
                case GET_TASK_Log:
                    httpget = new HttpGet(url);
                    httpget.setHeader("User-Agent", "Mozilla");
                    response = httpclient.execute(httpget);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + e.getLocalizedMessage() + e);
        }
        return response;
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }

    protected String doInBackground(String... urls) {
        String url = urls[0];
        String result = "";
        weburls = url;
        HttpResponse response = doResponse(url);
        if (response == null) {
            return result;
        } else {
            try {
                result = inputStreamToString(response.getEntity().getContent());
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        try {

            Log.d(mContext.getPackageCodePath().toString() + "==response===", response);
            handleResponse(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (response.isEmpty()) {

            Toast.makeText(mContext, processMessage, Toast.LENGTH_SHORT).show();

        }
        if (DialogYesNo) {
            if (pDlg != null && pDlg.isShowing()) {
                pDlg.dismiss();
                pDlg = null;
            }
        }
    }

    public abstract void handleResponse(String response) throws JSONException;

    // Establish connection and socket (data retrieval) timeouts

}

