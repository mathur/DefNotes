package com.orchestr8.android.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AlchemyAPI_TargetedSentimentParams extends AlchemyAPI_Params {
	
	private Boolean showSourceText;
	private String sentimentPhrase;
		
	public boolean isShowSourceText() {
		return showSourceText;
	}
	
	public void setShowSourceText(boolean showSourceText) {
		this.showSourceText = showSourceText;
	}
	
	public String getSentimentPhrase(){
		return sentimentPhrase;
	}
	
	public void setSentimentPhrase(String sentimentPhrase) {
		this.sentimentPhrase = sentimentPhrase;
	}
	
	public String getParameterString(){
		String retString = super.getParameterString();
		try{
			if(showSourceText!=null) retString+="&showSourceText="+(showSourceText?"1":"0");
			if(sentimentPhrase!=null) retString+="&target="+URLEncoder.encode(sentimentPhrase, "UTF-8");
					
		}
		catch(UnsupportedEncodingException e ){
			retString = "";
		}
		
		return retString;
	}
}
