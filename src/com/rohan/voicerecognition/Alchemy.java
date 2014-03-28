package com.rohan.voicerecognition;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 3/25/14.
 * Hopefully this will make the coder cleaner
 */
public class Alchemy {

	
    static final ArrayList<String> keywordsList = new ArrayList<String>();

    Alchemy() {
        keywordsList.clear();
        new AlchemyAsyncTask().execute();
    }

    private class AlchemyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return postAlchemyData();
        }

        protected void onPostExecute(String output) {
            try {
                JSONObject data = new JSONObject(output);
                JSONArray keywords = data.getJSONArray("keywords");
                for (int i = 0; i < keywords.length(); i++) {
                    JSONObject keyword = keywords.getJSONObject(i);
                    String text = keyword.get("text").toString();
                   Alchemy.keywordsList.add(text);
                }
                Dictionary.findDefinitions();
            } catch (JSONException e) {
                Log.e("error", e.getMessage());
            }
        }

    }

    String postAlchemyData() {
        HttpClient httpclient = new DefaultHttpClient();
        // specify the URL you want to post to
        HttpPost httppost = new HttpPost(
                "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords");
        try {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("apikey",
                    "65339f480b58cb7d9e4dcd3728e4c4cfeca2ffa6"));
            pairs.add(new BasicNameValuePair("text", MainActivity.recognizedText));
            // pairs.add(new BasicNameValuePair("text", article));
            pairs.add(new BasicNameValuePair("outputMode", "json"));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            // send the variable and value, in other words post, to the URL
            HttpResponse response = httpclient.execute(httppost);

            // parse
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return null;
    }

}
