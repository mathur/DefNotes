package com.rohan.voicerecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
	

    private static final int REQUEST_OK = 1;

    
    public static String recognizedText = "", userEmail = "", lectureName = "";
    static final Email email = new Email();
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context=this;
        findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override   public boolean onOptionsItemSelected(MenuItem item) {
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
                                Html.fromHtml("<b><font color='black'>About DefNotes</font></b>"))
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
            if (thingsSaid != null) {
                recognizedText = thingsSaid.get(0);
            }
        }
    }

    public void parseData(View view) {
        Toast.makeText(getApplicationContext(), "Submitting data... please wait",
                Toast.LENGTH_LONG).show();

        lectureName = ((EditText) findViewById(R.id.etLectureName)).getText().toString();
        userEmail = ((EditText) findViewById(R.id.editText1)).getText().toString();
        new Alchemy();

    }

    public void startRawText(View view) {
        Intent intent = new Intent(this, RawText.class);
        intent.putExtra("message", recognizedText);
        startActivity(intent);
    }

    public void startKeywords(View view) {
        Intent intent = new Intent(this, Keywords.class);
        intent.putExtra("message", Email.emailContentsText);
        startActivity(intent);
    }
}