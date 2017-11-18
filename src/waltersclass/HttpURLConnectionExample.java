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


class PriceData{
	public float bid = 0;
	public float ask = 0;
	public String state = "";
	public int last_updated = 0;
	
}


public class HttpURLConnectionExample {

	private final String USER_AGENT = "walter";

	public static void main(String[] args) throws Exception {


		HttpURLConnectionExample http = new HttpURLConnectionExample();
		float[] a = new float[10];
		for (int i= 0; i<10; i = i +1 ){
			Thread.sleep(5 * 1000);
			System.out.println("Testing 1 - Send Http GET request");
			;JSONObject priceJSON= http.sendGet("https://api.whaleclub.co/v1/price/BTC-USD", "Bearer ab7d8bd3-98b5-438a-8d38-0247ee9578d0");
			PriceData btcPrice = new PriceData();
			btcPrice.ask = priceJSON.getJSONObject("BTC-USD").getFloat("ask");
			btcPrice.bid = priceJSON.getJSONObject("BTC-USD").getFloat("bid");
			btcPrice.state = priceJSON.getJSONObject("BTC-USD").getString("state");
			btcPrice.last_updated = priceJSON.getJSONObject("BTC-USD").getInt("last_updated");
			
//			String epochtime = String.valueOf((btcPrice.last_updated)*1000);
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//			Date date = df.parse(epochtime);
//			System.out.println(date+">>>>>>>>>>>yes");
	
		//Print data
			
			System.out.println("Current BTC info");
			System.out.print("ask: \t");
			System.out.print(btcPrice.ask);
			System.out.print("\tbid: \t");
			System.out.print(btcPrice.bid);
			System.out.print("\tstate: \t");
			System.out.print(btcPrice.state);
			System.out.print("\tlast updated: \t");
			System.out.println(btcPrice.last_updated);
		
		
			
			
			
				
		int threshold = 1;
		// Create new bid if surpassed
		
		if(btcPrice.ask<threshold){
			System.out.println("Opening Position");
			http.sendPost();
			}
			else{
				System.out.println("No new position for you");
			}
			System.out.println("\nTesting 2 - Send Http POST request");
			a[i]=btcPrice.ask;
	}
		
		float high;
		float low;
		float open;
		float close;
		high = a[0];
		low = a[0];
		open = a[0];
		close = a[9];
		
		for (int g= 0; g<10; g++ ){
		System.out.println(a[g]+" >>>>>>>"+g);
			if(high<a[g])
				high = a[g];
			else if(low>a[g])
				low=a[g];
			}
		
		System.out.println();
		System.out.println(open+" open");
		System.out.println(high+" high");
		System.out.println(low+" low");
		System.out.println(close+" close");
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