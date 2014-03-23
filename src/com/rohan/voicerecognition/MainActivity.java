package com.rohan.voicerecognition;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.github.sendgrid.SendGrid;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int REQUEST_OK = 1;
	public String recognizedText = "";
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
		context = this;
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
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
	    try {
	    	startActivityForResult(i, REQUEST_OK);
	    } catch (Exception e) {
	        Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
	    }
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
                    Toast.makeText(context, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                    sendgrid.send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
	}
}