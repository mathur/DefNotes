package com.rohan.voicerecognition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.os.Environment;
import android.os.StrictMode;
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

import com.github.sendgrid.SendGrid;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int REQUEST_OK = 1;


	public String recognizedText = "", emailContents = "";
	public static final String intro = "Key Terms/Definitions:",
			article = "The United States and Russia reached a sweeping agreement on Saturday that called for Syria�s arsenal of chemical weapons to be removed or destroyed by the middle of 2014 and indefinitely stalled the prospect of American airstrikes.The joint announcement, on the third day of intensive talks in Geneva, also set the stage for one of the most challenging undertakings in the history of arms control.�This situation has no precedent,� said Amy E. Smithson, an expert on chemical weapons at theJames Martin Center for Nonproliferation Studies. �They are cramming what would probably be five or six years� worth of work into a period of several months, and they are undertaking this in an extremely difficult security environment due to the ongoing civil war.�",
			startHtml = "<style type='text/css'>body{font-size: 13;}h1{font-size: 14;font-family:serif;}table,th,td,tr {color:#000;width:700px;}</style><table><tr><td><img border='0' src='http://www.rmathur.com/images/BANNER.png' width=700></td></tr><tr><td><h1>Hey there! Look we what managed to find out about that boring lecture you weren't forced to sit though!</h1></td></tr><tr><td><p wdith=700>",
			endHtml = "</p></td></tr></table>";

	ArrayList<String> keywordsList = new ArrayList<String>(),
			definitions = new ArrayList<String>();
	Context context;
	private static String FILE = "c:/temp/FirstPdf.pdf";
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
		context = this;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
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
			TextView aboutMsg = new TextView(this);
			aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
			aboutMsg.setPadding(30, 30, 30, 30);
			aboutMsg.setText(Html
					.fromHtml("Recorder, summarizer, and distributor of lectures everywere.<br><br><font color='black'><small>Developed for hackBCA Spring 2014</small></font>"));

			Builder builder = new AlertDialog.Builder(this);
			builder.setView(aboutMsg)
					.setTitle(
							Html.fromHtml("<b><font color='black'>About LecMail</font></b>"))
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
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		try {
			startActivityForResult(i, REQUEST_OK);
		} catch (Exception e) {
			Toast.makeText(this, "Error initializing speech to text engine.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_OK && resultCode == RESULT_OK) {
			ArrayList<String> thingsSaid = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			((TextView) findViewById(R.id.txtText)).setText(thingsSaid.get(0));
			recognizedText = thingsSaid.get(0);
		}

	}

	public void parseData(View view) {
		new AlchemyAsyncTask().execute();
	}

	public void sendEmail() {

		Toast.makeText(getApplicationContext(), "Sending Email",
				Toast.LENGTH_SHORT).show();

		final String userEmail = ((EditText) findViewById(R.id.editText1))
				.getText().toString();
		final String lectureName = ((EditText) findViewById(R.id.etLectureName))
				.getText().toString();
		
    	Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
		
		Document document = new Document();
		String file = Environment.getExternalStorageDirectory().getPath() + "/" + dateFormat.format(date) + "deflecture.pdf";
		try {
			PdfWriter.getInstance(document,new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.open();
		Paragraph p1 = new Paragraph(lectureName);
		Paragraph p2 = new Paragraph(emailContents);
		try {
			document.add(p1);
			document.add(p2);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();		
		
		SendGrid sendgrid = new SendGrid("rohan32", "hackru");
		sendgrid.addTo(userEmail);
		sendgrid.setFrom("DefNotes@defnotes.com");
		sendgrid.setSubject("Your " + lectureName + " study guide here");
		sendgrid.setHtml(startHtml + emailContents + endHtml);
		sendgrid.send();
		Toast.makeText(context, "Email sent successfully.", Toast.LENGTH_SHORT)
				.show();
	}
	
	public void findDefinitions() {
		for (int i = 0; i < keywordsList.size(); i++) {
			String term = keywordsList.get(i);
			new DictionaryAsyncTask().execute(term, i + "");
		}
	}

	private class DictionaryAsyncTask extends
			AsyncTask<String, String, ArrayList<String>> {
		@Override
		// params[0] is term, params[1] is term number
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> out = new ArrayList<String>();
			String output = postDictionaryData(params[0]);
			out.add(params[0]);
			out.add(output);
			out.add(params[1]);
			return out;
		}

		protected void onPostExecute(ArrayList<String> out) {
			String term = out.get(0);
			String output = out.get(1);
			String id = out.get(2);
			try {
				JSONObject data = new JSONObject(output);
				JSONArray definitions = data.getJSONArray("definitions");
				if (definitions.length() > 0) {
					JSONObject definition = definitions.getJSONObject(0);
					String definitionText = definition.getString("text");
					emailContents += term + " : " + definitionText + "<br><br>";
				}
				if (Integer.parseInt(id) >= keywordsList.size() - 1) {
					sendEmail();
				}
			} catch (JSONException e) {
				Log.e("error", e.getMessage());
			}
		}

	}

	public String postDictionaryData(String word) {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(
				"https://montanaflynn-dictionary.p.mashape.com/define?word="
						+ word);
		try {

			httppost.addHeader("X-Mashape-Authorization",
					"KTGQojeCF3QsYcBoz5dJpz6679Cbgvqz");

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

	private class AlchemyAsyncTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String output = postAlchemyData();
			return output;
		}

		protected void onPostExecute(String output) {
			try {
				JSONObject data = new JSONObject(output);
				JSONArray keywords = data.getJSONArray("keywords");
				for (int i = 0; i < keywords.length(); i++) {
					JSONObject keyword = keywords.getJSONObject(i);
					String text = keyword.get("text").toString();
					keywordsList.add(text);
				}
				findDefinitions();
			} catch (JSONException e) {
				Log.e("error", e.getMessage());
			}
		}

	}

	public String postAlchemyData() {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(
				"http://access.alchemyapi.com/calls/text/TextGetRankedKeywords");
		try {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("apikey",
					"65339f480b58cb7d9e4dcd3728e4c4cfeca2ffa6"));
			pairs.add(new BasicNameValuePair("text", recognizedText));
			// pairs.add(new BasicNameValuePair("text", article));
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

}