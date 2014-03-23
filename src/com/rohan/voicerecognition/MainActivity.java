package com.rohan.voicerecognition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	// TextView tv = (TextView)findViewById(R.id.txtText);

	protected static final int REQUEST_OK = 1;
	public static String text = "In any case, the deal was at least a temporary reprieve for President Bashar al-Assad and his Syrian government, and it formally placed international decision-making about Syria into the purview of Russia, one of Mr. Assad’s staunchest supporters and military suppliers.That reality was bitterly seized on by the fractured Syrian rebel forces, most of which have pleaded for American airstrikes. Gen. Salim Idris, the head of the Western-backed rebels’ nominal military command, the Supreme Military Council, denounced the initiative.";

	public String recognizedText = "";
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e("HEY", "LOG TEST");
		findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
		context = this;
	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String output = postData();
			return output;
		}

		protected void onPostExecute(String output) {
			Toast.makeText(getApplicationContext(), "command sent",
					Toast.LENGTH_LONG).show();
			try {
				JSONObject data = new JSONObject(output);
				JSONArray keywords = data.getJSONArray("keywords");
				String keywordString="";
				for (int i = 0; i < keywords.length(); i++) {
					JSONObject keyword=keywords.getJSONObject(i);
					String text = keyword.get("text").toString();
					keywordString+=text;
					Log.e("text"+i,text);
				}
				((TextView) findViewById(R.id.txtText)).setText(keywordString);
			} catch (JSONException e) {
				Log.e("error", e.getMessage());
			}
		}

	}

	public String postData() {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(
				"http://access.alchemyapi.com/calls/text/TextGetRankedKeywords");
		try {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("apikey",
					"65339f480b58cb7d9e4dcd3728e4c4cfeca2ffa6"));
			pairs.add(new BasicNameValuePair("text", text));
			pairs.add(new BasicNameValuePair("outputMode", "json"));
			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			// send the variable and value, in other words post, to the URL
			HttpResponse response = httpclient.execute(httppost);

			// parse
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
			return builder.toString();
		} catch (Exception e) {
			Log.e("error", e.getMessage());
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                // if about is pressed, then open the dialog box
                showDialog(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
    @SuppressWarnings("deprecation")
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                // Create our About Dialog
                TextView aboutMsg  = new TextView(this);
                aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
                aboutMsg.setPadding(30, 30, 30, 30);
                aboutMsg.setText(Html.fromHtml("Recorder, summarizer, and distributor of lectures everywere.<br><br><font color='black'><small>Developed for hackBCA Spring 2014</small></font>"));

                Builder builder = new AlertDialog.Builder(this);
                builder.setView(aboutMsg)
                        .setTitle(Html.fromHtml("<b><font color='black'>About LecMail</font></b>"))
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });

                return builder.create();
        }
        return super.onCreateDialog(id);
    }
    
	@Override
	public void onClick(View v) {
		new MyAsyncTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        if (requestCode == REQUEST_OK  && resultCode == RESULT_OK) {
	        		ArrayList<String> thingsSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        		((TextView)findViewById(R.id.txtText)).setText(thingsSaid.get(0));
	        		recognizedText = thingsSaid.get(0);
	        }
	    }
	
	public void sendEmail(View view){
        final String lectureName = ((EditText)findViewById(R.id.etLectureName)).getText().toString();
        final String userEmail = ((EditText)findViewById(R.id.editText1)).getText().toString();
        
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    SendGrid sendgrid = new SendGrid("rohan32", "hackru");
                    sendgrid.addTo(userEmail);
                    sendgrid.setFrom("info@lecmail.com");
                    sendgrid.setSubject("Your " + lectureName + " study guide here");
                    sendgrid.setText(recognizedText);
                    sendgrid.send();
                    Toast.makeText(context, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
	}
}