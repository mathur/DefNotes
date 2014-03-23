package com.orchestr8.android.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class AlchemyAPI {
    private String _apiKey;
    private String _requestUri = "http://access.alchemyapi.com/calls/";

    private AlchemyAPI() {
    }
    
    static public AlchemyAPI GetInstanceFromString(String apiKey)
    {
        AlchemyAPI api = new AlchemyAPI();
        api.SetAPIKey(apiKey);

        return api;
    }
    
    public void SetAPIKey(String apiKey) {
        _apiKey = apiKey;

        if (null == _apiKey || _apiKey.length() < 5)
            throw new IllegalArgumentException("Too short API key.");
    }
    
    public void SetAPIHost(String apiHost) {
        if (null == apiHost || apiHost.length() < 2)
            throw new IllegalArgumentException("Too short API host.");

        _requestUri = "http://" + apiHost + ".alchemyapi.com/calls/";
    }
    
    public Document URLGetRankedNamedEntities(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetRankedNamedEntities(url, new AlchemyAPI_NamedEntityParams());
	}
	
	public Document URLGetRankedNamedEntities(String url, AlchemyAPI_NamedEntityParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
		CheckURL(url);
		
		params.setUrl(url);
	
		return GET("URLGetRankedNamedEntities", "url", params);
	}
	
	public Document HTMLGetRankedNamedEntities(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetRankedNamedEntities(html, url, new AlchemyAPI_NamedEntityParams());
	}
	
	
	public Document HTMLGetRankedNamedEntities(String html, String url, AlchemyAPI_NamedEntityParams params)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetRankedNamedEntities", "html", params);
	}
	
	public Document TextGetRankedNamedEntities(String text)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return TextGetRankedNamedEntities(text, new AlchemyAPI_NamedEntityParams());
	}
	
	public Document TextGetRankedNamedEntities(String text, AlchemyAPI_NamedEntityParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
		CheckText(text);
	
		params.setText(text);
	
		return POST("TextGetRankedNamedEntities", "text", params);
	}
	
    public Document URLGetRankedConcepts(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetRankedConcepts(url, new AlchemyAPI_ConceptParams());
	}
	
	public Document URLGetRankedConcepts(String url, AlchemyAPI_ConceptParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
		CheckURL(url);
	
		params.setUrl(url);
	
		return GET("URLGetRankedConcepts", "url", params);
	}    
	
	
	public Document HTMLGetRankedConcepts(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetRankedConcepts(html, url, new AlchemyAPI_ConceptParams());
	}
	
	public Document HTMLGetRankedConcepts(String html, String url, AlchemyAPI_ConceptParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetRankedConcepts", "html", params);
	}
	
	public Document TextGetRankedConcepts(String text) throws IOException, SAXException,
	        ParserConfigurationException {
	    return TextGetRankedConcepts(text, new AlchemyAPI_ConceptParams());
	}
	
	public Document TextGetRankedConcepts(String text, AlchemyAPI_ConceptParams params) throws IOException, SAXException,
	ParserConfigurationException 
	{
		CheckText(text);
		
		params.setText(text);
		
		return POST("TextGetRankedConcepts", "text", params);
	}
	
	
	
	public Document URLGetRankedKeywords(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetRankedKeywords(url, new AlchemyAPI_KeywordParams());
	}
	
	public Document URLGetRankedKeywords(String url, AlchemyAPI_KeywordParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
		CheckURL(url);
	
		params.setUrl(url);
	
		return GET("URLGetRankedKeywords", "url", params);
	}    
	
	
	public Document HTMLGetRankedKeywords(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetRankedKeywords(html, url, new AlchemyAPI_KeywordParams());
	}
	
	public Document HTMLGetRankedKeywords(String html, String url, AlchemyAPI_KeywordParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetRankedKeywords", "html", params);
	}
	
	public Document TextGetRankedKeywords(String text) throws IOException, SAXException,
	        ParserConfigurationException {
	    return TextGetRankedKeywords(text, new AlchemyAPI_KeywordParams());
	}
	
	public Document TextGetRankedKeywords(String text, AlchemyAPI_KeywordParams params) throws IOException, SAXException,
	ParserConfigurationException 
	{
		CheckText(text);
		
		params.setText(text);
		
		return POST("TextGetRankedKeywords", "text", params);
	}
	
	public Document URLGetLanguage(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetLanguage(url, new AlchemyAPI_LanguageParams());
	}
	
	public Document URLGetLanguage(String url, AlchemyAPI_LanguageParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	    
	    params.setUrl(url);
	
	    return GET("URLGetLanguage", "url", params);
	}
	
	public Document HTMLGetLanguage(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetLanguage(html, url, new AlchemyAPI_LanguageParams());
	}
	
	public Document HTMLGetLanguage(String html, String url, AlchemyAPI_LanguageParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetLanguage", "html", params);
	}
	
	public Document TextGetLanguage(String text)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return TextGetLanguage(text, new AlchemyAPI_LanguageParams());
	}
	
	public Document TextGetLanguage(String text, AlchemyAPI_LanguageParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckText(text);
	
	    params.setText(text);
	
	    return POST("TextGetLanguage", "text", params);
	}
	
	public Document URLGetCategory(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetCategory(url, new AlchemyAPI_CategoryParams());
	}
	
	public Document URLGetCategory(String url, AlchemyAPI_CategoryParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	
	    params.setUrl(url);
	
	    return GET("URLGetCategory", "url", params);
	}
	
	public Document HTMLGetCategory(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetCategory(html, url, new AlchemyAPI_CategoryParams());
	}
	
	public Document HTMLGetCategory(String html, String url, AlchemyAPI_CategoryParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetCategory", "html", params);
	}
	
	public Document TextGetCategory(String text)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return TextGetCategory(text, new AlchemyAPI_CategoryParams());
	}
	
	public Document TextGetCategory(String text, AlchemyAPI_CategoryParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckText(text);
	    
	    params.setText(text);
	
	    return POST("TextGetCategory", "text", params);
	}
	
	public Document URLGetText(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetText(url, new AlchemyAPI_TextParams());
	}
	
	public Document URLGetText(String url, AlchemyAPI_TextParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	    
	    params.setUrl(url);
	
	    return GET("URLGetText", "url", params);
	}
	
	public Document HTMLGetText(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetText(html, url, new AlchemyAPI_TextParams());
	}
	
	public Document HTMLGetText(String html, String url, AlchemyAPI_TextParams params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetText", "html", params);
	}
	
	public Document URLGetRawText(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetRawText(url, new AlchemyAPI_Params());
	}
	
	public Document URLGetRawText(String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	
	    params.setUrl(url);
	
	    return GET("URLGetRawText", "url", params);
	}
	
	public Document HTMLGetRawText(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetRawText(html, url, new AlchemyAPI_Params());
	}
	
	public Document HTMLGetRawText(String html, String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetRawText", "html", params);
	}
	
	public Document URLGetTitle(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetTitle(url, new AlchemyAPI_Params());
	}
	
	public Document URLGetTitle(String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	    
	    params.setUrl(url);
	
	    return GET("URLGetTitle", "url", params);
	}
	
	public Document HTMLGetTitle(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetTitle(html, url, new AlchemyAPI_Params());
	}
	
	public Document HTMLGetTitle(String html, String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetTitle", "html", params);
	}
	
	public Document URLGetFeedLinks(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetFeedLinks(url, new AlchemyAPI_Params());
	}
	
	public Document URLGetFeedLinks(String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	    
	    params.setUrl(url);
	
	    return GET("URLGetFeedLinks", "url", params);
	}
	
	public Document HTMLGetFeedLinks(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetFeedLinks(html, url, new AlchemyAPI_Params());
	}
	
	public Document HTMLGetFeedLinks(String html, String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetFeedLinks", "html", params);
	}
	
	public Document URLGetMicroformats(String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return URLGetMicroformats(url, new AlchemyAPI_Params());
	}
	
	public Document URLGetMicroformats(String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckURL(url);
	
	    params.setUrl(url);
	
	    return GET("URLGetMicroformatData", "url", params);
	}
	
	public Document HTMLGetMicroformats(String html, String url)
	    throws IOException, SAXException,
	           ParserConfigurationException
	{
	    return HTMLGetMicroformats(html, url, new AlchemyAPI_Params());
	}
	
	public Document HTMLGetMicroformats(String html, String url, AlchemyAPI_Params params)
	throws IOException, SAXException,
	       ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetMicroformatData", "html", params);
	}
	
	public Document URLGetConstraintQuery(String url, String query)
	    throws IOException,
	           SAXException, ParserConfigurationException
	{
	    return URLGetConstraintQuery(url, query, new AlchemyAPI_ConstraintQueryParams());
	}
	
	public Document URLGetConstraintQuery(String url, String query, AlchemyAPI_ConstraintQueryParams params)
	throws IOException,
	       SAXException, ParserConfigurationException
	{
	    CheckURL(url);
	    if (null == query || query.length() < 2)
	        throw new IllegalArgumentException("Invalid constraint query specified");
	    
	    params.setUrl(url);
	    params.setCQuery(query);
	
	    return POST("URLGetConstraintQuery", "url", params);
	}
	
	
	public Document HTMLGetConstraintQuery(String html, String url, String query)
	    throws IOException,
	           SAXException, ParserConfigurationException
	{
	    return HTMLGetConstraintQuery(html, url, query, new AlchemyAPI_ConstraintQueryParams());
	}
	
	public Document HTMLGetConstraintQuery(String html, String url, String query, AlchemyAPI_ConstraintQueryParams params)
	throws IOException,
	       SAXException, ParserConfigurationException
	{
	    CheckHTML(html, url);
	    if (null == query || query.length() < 2)
	        throw new IllegalArgumentException("Invalid constraint query specified");
	
	    params.setUrl(url);
	    params.setHtml(html);
	    params.setCQuery(query);
	
	    return POST("HTMLGetConstraintQuery", "html", params);
	}
	
    public Document HTMLGetTextSentiment(String html, String url)
        throws IOException, SAXException,
               ParserConfigurationException
    {
        return HTMLGetTextSentiment(html, url, new AlchemyAPI_Params());
    }
    
    public Document HTMLGetTextSentiment(String html, String url, AlchemyAPI_Params params)
    throws IOException, SAXException,
           ParserConfigurationException
	{
	    CheckHTML(html, url);

	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetTextSentiment", "html", params);
	}

    public Document TextGetTextSentiment(String text) throws IOException, SAXException,
            ParserConfigurationException {
        return TextGetTextSentiment(text, new AlchemyAPI_Params());
    }
    
    public Document TextGetTextSentiment(String text, AlchemyAPI_Params params) throws IOException, SAXException,
    ParserConfigurationException 
    {
		CheckText(text);
		
		params.setText(text);
		
		return POST("TextGetTextSentiment", "text", params);
	}
	
	public Document URLGetRelations(String url)
        throws IOException, SAXException, ParserConfigurationException
    {
        return URLGetRelations(url, new AlchemyAPI_RelationParams());
    }
    
    public Document URLGetRelations(String url, AlchemyAPI_RelationParams params)
    throws IOException, SAXException, ParserConfigurationException 
	{
	    CheckURL(url);

	    params.setUrl(url);
	
	    return GET("URLGetRelations", "url", params);
	}

    public Document HTMLGetRelations(String html, String url)
        throws IOException, SAXException, ParserConfigurationException
    {
        return HTMLGetRelations(html, url, new AlchemyAPI_RelationParams());
    }
    
    public Document HTMLGetRelations(String html, String url, AlchemyAPI_RelationParams params)
    throws IOException, SAXException, ParserConfigurationException
	{
	    CheckHTML(html, url);
	    
	    params.setUrl(url);
	    params.setHtml(html);
	
	    return POST("HTMLGetRelations", "html", params);
	}

    public Document TextGetRelations(String text)
        throws IOException, SAXException, ParserConfigurationException
    {
        return TextGetRelations(text, new AlchemyAPI_RelationParams());
    }
    
    public Document TextGetRelations(String text, AlchemyAPI_RelationParams params)
    throws IOException, SAXException, ParserConfigurationException
	{
	    CheckText(text);
	    
	    params.setText(text);
	
	    return POST("TextGetRelations", "text", params);
	}
	
	
	public Document URLGetAuthor(String url)
    	throws IOException, SAXException, ParserConfigurationException 
	{
		return URLGetAuthor(url, new AlchemyAPI_Params());
	}
    
    public Document URLGetAuthor(String url, AlchemyAPI_Params params)
    	throws IOException, SAXException, ParserConfigurationException 
	{
		CheckURL(url);
		
		params.setUrl(url);
		
		return GET("URLGetAuthor", "url", params);
	}
	
	public Document HTMLGetAuthor(String html, String url)
    	throws IOException, SAXException, ParserConfigurationException 
	{
		return HTMLGetAuthor(html, url, new AlchemyAPI_Params());
	}
    
    public Document HTMLGetAuthor(String html, String url, AlchemyAPI_Params params)
    	throws IOException, SAXException, ParserConfigurationException 
	{
		CheckHTML(html, url);
		
		params.setHtml(html);
		params.setUrl(url);
		
		return GET("HTMLGetAuthor", "html", params);
	}

    public Document URLGetTargetedSentiment(String url, String target) throws IOException, SAXException, ParserConfigurationException {
        return URLGetTargetedSentiment(url, target, new AlchemyAPI_TargetedSentimentParams());
    }
    	
    public Document URLGetTargetedSentiment(String url, String target, AlchemyAPI_TargetedSentimentParams params) throws 
		IOException, SAXException, ParserConfigurationException  {
    	CheckURL(url);
    	CheckText(target);
   	
    	params.setUrl(url);
    	params.setSentimentPhrase(target);
    	
    	return GET("URLGetTargetedSentiment", "url", params);
    }
	
    public Document HTMLGetTargetedSentiment(String html, String url, String target) throws IOException, SAXException, ParserConfigurationException {
        return HTMLGetTargetedSentiment(html, url, target, new AlchemyAPI_TargetedSentimentParams());
    }
    	
    public Document HTMLGetTargetedSentiment(String html, String url, String target, AlchemyAPI_TargetedSentimentParams params) throws 
		IOException, SAXException, ParserConfigurationException  {
    	CheckHTML(html, url);
    	CheckText(target);
    	
		params.setHtml(html);
    	params.setUrl(url);
    	params.setSentimentPhrase(target);
    	
    	return GET("HTMLGetTargetedSentiment", "html", params);
    }

    public Document TextGetTargetedSentiment(String text, String target) throws IOException, SAXException, ParserConfigurationException {
        return TextGetTargetedSentiment(text, target, new AlchemyAPI_TargetedSentimentParams());
    }
    	
    public Document TextGetTargetedSentiment(String text, String target, AlchemyAPI_TargetedSentimentParams params) throws 
		IOException, SAXException, ParserConfigurationException  {
    	CheckText(text);
    	CheckText(target);
    	
    	params.setText(text);
    	params.setSentimentPhrase(target);
    	
    	return GET("TextGetTargetedSentiment", "text", params);
    }

    private void CheckHTML(String html, String url) {
        if (null == html || html.length() < 5)
            throw new IllegalArgumentException("Enter a HTML document to analyze.");

        if (null == url || url.length() < 10)
            throw new IllegalArgumentException("Enter an URL to analyze.");
    }

    private void CheckText(String text) {
        if (null == text || text.length() < 5)
            throw new IllegalArgumentException("Enter some text to analyze.");
    }

    private void CheckURL(String url) {
        if (null == url || url.length() < 10)
            throw new IllegalArgumentException("Enter an URL to analyze.");
    }

    private Document GET(String callName, String callPrefix, AlchemyAPI_Params params)
    throws IOException, SAXException,
           ParserConfigurationException
	{
	    StringBuilder uri = new StringBuilder();
	    uri.append(_requestUri).append(callPrefix).append('/').append(callName)
	       .append('?').append("apikey=").append(this._apiKey).append("&fromSrc=android");
	    uri.append(params.getParameterString());
	
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(uri.toString());
	    
        HttpResponse response = httpclient.execute(httpget);

        System.out.println("Login form get: " + response.getStatusLine());
        InputStream is = response.getEntity().getContent();
        DataInputStream istream = new DataInputStream(is);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(istream);

        is.close();
        return doc;
	}
    
    private Document POST(String callName, String callPrefix, AlchemyAPI_Params params)
    throws IOException, SAXException,
           ParserConfigurationException
	{	
    	DefaultHttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost = new HttpPost(_requestUri + callPrefix + "/" + callName);  

	    StringBuilder data = new StringBuilder();
	    data.append("apikey=").append(this._apiKey);
		data.append("&fromSrc=android");
	    data.append(params.getParameterString());
	    httppost.setEntity(new StringEntity(data.toString()));
 
        HttpResponse response = httpclient.execute(httppost);

        System.out.println("Login form get: " + response.getStatusLine());
        InputStream is = response.getEntity().getContent();
        DataInputStream istream = new DataInputStream(is);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(istream);

        is.close();
        return doc;
	}
    
}
