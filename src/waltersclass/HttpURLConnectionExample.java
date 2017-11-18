package waltersclass;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.TimeZone;




//import java.util.Scanner;
//import org.json.*;
import javax.net.ssl.HttpsURLConnection;







//import org.json.JSONException;
import org.json.JSONObject;
//import java.util.Arrays;
//import java.util.Collections;
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;

class CandleStick{
	float open;
	float close;
	float high;
	float low;
	String timeStamp;
	CandleStick(PriceData [] prices, int arrayLength){
		//Set timestamp
		timeStamp = prices[0].getLastUpdateTime();
		// Initialize candlestick parameters
		high = prices[0].ask;
		low = prices[0].ask;
		open = prices[0].ask;
		close = prices[arrayLength-1].ask;
		// Sort through array of priceData points to find actual high and low
		for (int i= 0; i<arrayLength; i++ ){
	//	System.out.println(prices[i].ask+" >>>>>>>"+i);
			if(high<prices[i].ask)
				high = prices[i].ask;
			else if(low>prices[i].ask)
				low=prices[i].ask;
			}

	}
	
	public String getCandleStickData(){
		return (timeStamp+"\topen:\t"+open+"\thigh:\t"+high+"\tlow:\t"+low+"\tclose:\t"+close);
	}
	public void printCandleStickData(){
		System.out.print(timeStamp+"\topen:\t"+open+"\thigh:\t"+high+"\tlow:\t"+low+"\tclose:\t"+close);
	}
}




class PriceData{
	
	public float bid;
	public float ask;
	public String state;
	public int last_updated;
	//Constructor
	PriceData(JSONObject priceJSON){
		ask = priceJSON.getJSONObject("BTC-USD").getFloat("ask");
		bid = priceJSON.getJSONObject("BTC-USD").getFloat("bid");
		state = priceJSON.getJSONObject("BTC-USD").getString("state");
		last_updated = priceJSON.getJSONObject("BTC-USD").getInt("last_updated");
	}
	
	
	public String getLastUpdateTime(){
		Date date = new Date(last_updated * 1000L);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
		String formatted = format.format(date);
		return formatted;
	}
	public void printBTCData(){
		System.out.println("Current market info");
		System.out.print("ask: \t");
		System.out.print(ask);
		System.out.print("\tbid: \t");
		System.out.print(bid);
		System.out.print("\tstate: \t");
		System.out.print(state);
		System.out.print("\tlast updated: \t");
		System.out.println(getLastUpdateTime());

		
	}
}


public class HttpURLConnectionExample {

	private final String USER_AGENT = "walter";

	public static void main(String[] args) throws Exception {


		HttpURLConnectionExample http = new HttpURLConnectionExample();
		PriceData[] prices = new PriceData[5];
		for (int i= 0; i<prices.length; i = i +1 ){
			System.out.println("Testing 1 - Send Http GET request");
			JSONObject priceJSON= http.sendGet("https://api.whaleclub.co/v1/price/BTC-USD", "Bearer ab7d8bd3-98b5-438a-8d38-0247ee9578d0");
			prices[i] = new PriceData(priceJSON);
		//Print data
			prices[i].printBTCData();
//		Posting Code
//		int threshold = 1;
//		// Create new bid if surpassed
//		
//		if(btcPrice.ask<threshold){
//			System.out.println("Opening Position");
//			http.sendPost();
//			}
//			else{
//				System.out.println("No new position for you");
//			}
//			System.out.println("\nTesting 2 - Send Http POST request");
//			askPrices[i]=btcPrice.ask;
			
			Thread.sleep(5 * 1000);
	}
		

		CandleStick testStick = new CandleStick(prices, prices.length);
		testStick.printCandleStickData();
		
		}
		

	
	
	
	
	
	
	
	
	// HTTP GET request
	private JSONObject sendGet(String url, String apiKey) throws Exception {
		//String url = "https://api.whaleclub.co/v1/price/BTC-USD";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", apiKey);

//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		

//		System.out.println(response.toString());
		
		return new JSONObject(response.toString());
		
		}
	
	

		
	
	
	
	
	
	
	
	// HTTP POST request
	public void sendPost() throws Exception {

		String HttpsURLConnection = "https://api.whaleclub.co/v1/position/new";

		URL url = new URL(HttpsURLConnection);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Authorization", "Bearer ab7d8bd3-98b5-438a-8d38-0247ee9578d0");
		

		String urlParameters = "direction=long&market=BTC-USD&leverage=1&size=100000000";
		// Send post request
		con.setDoOutput(true);
		con.setDoInput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + HttpsURLConnection);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		

	}
}