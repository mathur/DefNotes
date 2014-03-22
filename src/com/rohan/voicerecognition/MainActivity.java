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
	        }
	    }



}