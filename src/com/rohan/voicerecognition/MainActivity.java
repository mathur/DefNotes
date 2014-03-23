package com.rohan.voicerecognition;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sendgrid.SendGrid;
import com.orchestr8.android.api.AlchemyAPI;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int REQUEST_OK = 1;
	public String AlchemyAPI_Key = "c43827ddcf36ade5d12e3846edc787415e6840ae";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnVoiceRecognize).setOnClickListener(this);
		AlchemyAPI api;
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
	        
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                    	
                        SendGrid sendgrid = new SendGrid("rohan32", "hackru");
                        sendgrid.addTo("mitranopeter@gmail.com");
                        sendgrid.setFrom("rohan@rmathur.com");
                        sendgrid.setSubject("Your SubjectName study guide here");
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