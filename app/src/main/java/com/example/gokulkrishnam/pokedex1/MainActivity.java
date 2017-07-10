package com.example.gokulkrishnam.pokedex1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.text.CollationElementIterator;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textview2;
    EditText editText;
    ImageView imageview;
    String imageurl;
    String name="pokedex";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        textview2=(TextView)findViewById(R.id.textView2);
        editText=(EditText)findViewById(R.id.editText);
        imageview=(ImageView)findViewById(R.id.imageView);


    }

    public void search(View view)
    {    name = editText.getText().toString();
         textview2.setText(name);
         dialog= new ProgressDialog(this);
         dialog.setMessage("Loading...");
         dialog.show();

        new Jsonmethod().execute("http://pokeapi.co/api/v2/pokemon/"+editText.getText());
    }



    public class Jsonmethod extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            BufferedReader reader=null;

            try {
                URL url= new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream stream= connection.getInputStream();

                reader= new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer=new StringBuffer();


                String line="";

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }

                String finaljson= buffer.toString();
                try {
                    JSONObject parentobject=new JSONObject(finaljson);

                    int baseexperience= parentobject.getInt("base_experience");
                    int height=parentobject.getInt("height");
                    int weight=parentobject.getInt("weight");
                    JSONObject sprites=parentobject.getJSONObject("sprites");
                    imageurl=sprites.getString("front_default");
                    String finalresult="Base experience-"+baseexperience+"\n"+"Height-"+height+"\n"+"Weight-"+weight;
                    return finalresult;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.hide();
            textView.setText(s);
            Picasso.with(getApplicationContext()).load(imageurl).into(imageview);

        }
    }
}

