package com.rohan.voicerecognition;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sendgrid.SendGrid;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int REQUEST_OK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
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
	        final String recognizedText = thingsSaid.get(0);
	        final String lectureName = ((EditText)findViewById(R.id.etLectureName)).getText().toString();

            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                    	
                        SendGrid sendgrid = new SendGrid("rohan32", "hackru");
                        sendgrid.addTo("rohanmathur34@gmail.com");
                        sendgrid.addTo("mitranopeter@gmail.com");
                        sendgrid.setFrom("rohan@rmathur.com");
                        sendgrid.setSubject("Your " + lectureName + " study guide here");
                        sendgrid.setText(recognizedText);
                        sendgrid.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
	        }
	    }
}