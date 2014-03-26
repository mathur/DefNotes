package com.rohan.voicerecognition;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Peter on 3/25/14.
 */
public class Dictionary {

    static ArrayList<String> definitions = new ArrayList<String>();

    Dictionary(){
        definitions.clear();
        findDefinitions();
    }

    public static void findDefinitions() {
        for (int i = 0; i < Alchemy.keywordsList.size(); i++) {
            String term = Alchemy.keywordsList.get(i);
            new DictionaryAsyncTask().execute(term, i + "");
        }
    }

    private static class DictionaryAsyncTask extends
            AsyncTask<String, String, ArrayList<String>> {
        @Override
        // params[0] is term, params[1] is term number
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> out = new ArrayList<String>();
            String output = postDictionaryData(params[0]);
            out.add(params[0]);
            out.add(output);
            out.add(params[1]);
            Log.e("param 0", params[0]);
            return out;
        }

        protected void onPostExecute(ArrayList<String> out) {
            String term = out.get(0);
            String output = out.get(1);
            String id = out.get(2);
            Log.e("term", term);
            try {
                JSONObject data = new JSONObject(output);
                JSONArray definitions = data.getJSONArray("definitions");
                if (definitions.length() > 0) {
                    JSONObject definition = definitions.getJSONObject(0);
                    String definitionText = definition.getString("text");
                    Email.emailContentsHTML += term + " : " + definitionText
                            + "<br><br>";
                    Email.emailContentsText += term + " : " + definitionText + "\n\n";
                }
                if (Integer.parseInt(id) >= Alchemy.keywordsList.size() - 1) {
                    MainActivity.email.sendEmail(MainActivity.userEmail,MainActivity.lectureName);
                }
            } catch (JSONException e) {
                Log.e("error", e.getMessage());
            }
        }

    }

    public static String postDictionaryData(String word) {
        HttpClient httpclient = new DefaultHttpClient();
        // specify the URL you want to post to
        StringTokenizer stk = new StringTokenizer(word);
        String firstWord;
        if (stk.hasMoreTokens()) {
            firstWord = stk.nextToken();
        } else {
            firstWord = word;
        }

        HttpPost httppost = new HttpPost(
                "https://montanaflynn-dictionary.p.mashape.com/define?word="
                        + firstWord);
        try {

            httppost.addHeader("X-Mashape-Authorization",
                    "KTGQojeCF3QsYcBoz5dJpz6679Cbgvqz");

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
