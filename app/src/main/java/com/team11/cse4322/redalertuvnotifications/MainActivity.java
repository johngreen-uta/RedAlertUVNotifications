package com.team11.cse4322.redalertuvnotifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    //button listener
    Button  uvButton;

    //zip code from edit text
    EditText uvEdit;

    //text view to paste json
    private TextView uvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find Button for uv search
        uvButton = (Button)findViewById(R.id.button);

        // find text from uv edit text
        uvEdit = (EditText)findViewById(R.id.zipTextField);

        // text view to paste json
        uvData = (TextView)findViewById(R.id.uvJsonItem);

        // on clicking find uv button, app will go to the url provided and grab the json with the help of the UV_Lookup class
        uvButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        new UV_Lookup().execute("https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVDAILY/ZIP/"+ uvEdit.getText().toString() +"/JSON");
                    }
                });
    }

    /*
        This inner class acts as the background to the application. Using the URL provided, it'll go and grab the JSON and parse
        the JSON and return the uv index and uv alert
    */
    public class UV_Lookup extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                // sets up connection
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                // reads the json
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                // json string
                String finalJson = buffer.toString();

                // first the json array
                JSONArray parentArray = new JSONArray(finalJson);

                // the jsonobject inside the array
                JSONObject finalObject = parentArray.getJSONObject(0);

                // the key-value inside the json object
                int uvIndex = finalObject.getInt("UV_INDEX");
                int uvAlert = finalObject.getInt("UV_ALERT");

                // return info in order to display
                return "INDEX:"+ uvIndex + " ALERT:" + uvAlert;

                // exception handling
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if(connection != null){
                    connection.disconnect(); // close connection
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            uvData.setText(result); // display result upon finish
        }

    }


}

