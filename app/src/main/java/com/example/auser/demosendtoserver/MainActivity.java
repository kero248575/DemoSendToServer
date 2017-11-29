package com.example.auser.demosendtoserver;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private static final String HTTP_URL ="http://192.168.58.40/default.aspx";
EditText et_name,et_age,et_address,et_phonNumber,et_favor1,et_favor2,et_favor3,et_favor4;
RadioButton rbtn_male,rbtn_female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        createJasonString();
    }
    private void findviews(){
        et_name=(EditText)findViewById(R.id.et_name);
        et_age=(EditText)findViewById(R.id.et_age);
        et_address=(EditText)findViewById(R.id.et_address);
        et_phonNumber=(EditText)findViewById(R.id.et_phoneNumber);
        et_favor1=(EditText)findViewById(R.id.et_favor1);
        et_favor2=(EditText)findViewById(R.id.et_favor2);
        et_favor3=(EditText)findViewById(R.id.et_favor3);
        et_favor4=(EditText)findViewById(R.id.et_favor4);
        rbtn_male=(RadioButton) findViewById(R.id.rbtn_male);

    }
    private String createJasonString(){

         String name=et_name.getText().toString();
        Person p;
        Data data;
        if(!name.equals("")){


         String address=et_address.getText().toString();
         int age =Integer.parseInt(et_age.getText().toString());
        String phoneNumber =et_phonNumber.getText().toString();
        boolean isMale=rbtn_male.isChecked();
        ArrayList<String> favorites=new ArrayList<>();
        favorites.add(et_favor1.getText().toString());
        favorites.add(et_favor2.getText().toString());
        favorites.add(et_favor3.getText().toString());
        favorites.add(et_favor4.getText().toString());
        data=new Data(address,phoneNumber);
            p=new Person(name,age,isMale,data,favorites);
        }else {
            p=new Person();
        }
        return new Gson().toJson(p);

    }
    public void onSend(View view){
        String jsonStr=createJasonString();
        System.out.println(createJasonString());
        new MyAsyncTask().execute(jsonStr);
    }

    class MyAsyncTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            return uploadData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("result="+result);
        }
    }


    String uploadData(String jsonString) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            URL url = new URL(HTTP_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Submit", "Submit")
                    .appendQueryParameter("JSON", jsonString);///用Jsan是為了配合網頁,產生與解析
            String query = builder.build().getEncodedQuery();
            out = new BufferedOutputStream(conn.getOutputStream());
            out.write(query.getBytes());///這兩段
            out.flush();///資料上傳伺服器

            in = new BufferedInputStream(conn.getInputStream());///以下四段
            byte[] buf = new byte[1024];///
            in.read(buf);///
            String result = new String(buf);///收到伺服器回應,而且轉成字串
            return result.trim();//trim保留看的見的字

        } catch (Exception e) {
            e.printStackTrace();
            return "Send Failed";
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (conn != null)
                conn.disconnect();
        }
    }
}
