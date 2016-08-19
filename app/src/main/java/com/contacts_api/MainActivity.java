package com.contacts_api;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tv_jsonlist;
    private Button btn_hit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_jsonlist = (TextView) findViewById(R.id.tv_jsonlist);
        btn_hit = (Button) findViewById(R.id.btn_hit);
        btn_hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncHttpTask().execute();
            }
        });
    }
    public class AsyncHttpTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://api.androidhive.info/contacts/");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream steam = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(steam));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String str = buffer.toString();

                JSONObject jsonRootObject = new JSONObject(str);
                JSONArray jsonArray = jsonRootObject.optJSONArray("contacts");

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String Name = jsonObject.getString("name");
                    String year = jsonObject.getString("email");
                    String Gender = jsonObject.getString("gender");
//                    String Mobile = jsonObject.getString("mobile");

                    stringBuffer.append(Name + " - " + year + "\n" + Gender + "\n\n");
                }
                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tv_jsonlist.setText(result);
        }
    }
}
