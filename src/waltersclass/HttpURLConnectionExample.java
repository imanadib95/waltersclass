package waltersclass;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;


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
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
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

		float prevOpen = 0;
		float prevClose = 0;
		float prevHigh = 0;
		float prevLow = 0;
	
		
		float open;
		float close;
		float high;
		float low;
		float bodyskew;
		float longenough;
		float length;
		float topthird;
		float bottomthird;
		float body;
		boolean isbodygood = false;
		boolean iswithinrange = false;
		boolean islongenough = false;

		HttpURLConnectionExample http = new HttpURLConnectionExample();
		PriceData[] prices = new PriceData[4];
		
		for (int candleStickRuns = 0; candleStickRuns < 3; candleStickRuns++){
		
			for (int i= 0; i<prices.length; i = i +1 ){
				System.out.println("Testing 1 - Send Http GET request");
				JSONObject priceJSON= http.sendGet("https://api.whaleclub.co/v1/price/BTC-USD", "Bearer 144177e8-ef1c-4826-83d2-8d9f852ef3b6");
				prices[i] = new PriceData(priceJSON);
			//Print data
				prices[i].printBTCData();
	//		Posting Code

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
	
			bodyskew = 1;
			longenough = 0;
			high = prices[0].ask;
			low = prices[0].ask;
			open = prices[0].ask;
			close = prices[prices.length-1].ask;
			for (int i= 0; i<prices.length; i++ ){
	
		//	System.out.println(prices[i].ask+" >>>>>>>"+i);
				if(high<prices[i].ask)
					high = prices[i].ask;
				else if(low>prices[i].ask)
					low=prices[i].ask;
				}
			
			length = high - low; 
			body = Math.abs(open - close);
			topthird = high - (length*bodyskew);
			bottomthird = low + length*bodyskew;
			
			//Red or green
			float bodyhigh = 0;
			float bodylow = 0;
			
				if (open >= close)
				{	bodyhigh = open;
				bodylow = close;
				}
				else
				{
					bodyhigh = close;
					bodylow = open;
				}
			
			
			//Criteria 1 check
			if ((open >= topthird && close >= topthird))
				{
					isbodygood = true; 
					
				}
			else if ((open <= bottomthird && close <= bottomthird))
				{
					isbodygood = true; 
					
				}
			
			//Criteria 2 check
			float ps = 0;
			ps =  prevHigh - prevLow;
			for (int g = 2; g <=5; g++)
				if ((prevHigh - prevLow)>ps)
					ps =  prevHigh - prevLow;
			
			if (length > longenough*ps)
				islongenough = true; 
			
	
			
			//Criteria 3 check
			if(bodyhigh < prevHigh && bodylow > prevLow)
				iswithinrange= true; 
			
			if(isbodygood && iswithinrange && islongenough || prices.length>1){
			//if(isbodygood && iswithinrange && islongenough){
			System.out.println("\nOpening Position");
			http.sendPost();
			}
			else{
				System.out.println("\nNo new position for you");
			}
			
			prevOpen = open;
			prevClose = close;
			prevHigh = high;
			System.out.println(prevHigh +"prevhigh");
			prevLow = low;
			System.out.println(prevLow +"prevlow");
			float risk = 0;
			risk = prevHigh - prevLow;
		
		}
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

		
///////////////////////////////////////////////

///////////////////////////////////////////////
		String HttpsURLConnection = "https://api.whaleclub.co/v1/position/new";

		URL url = new URL(HttpsURLConnection);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Authorization", "Bearer 144177e8-ef1c-4826-83d2-8d9f852ef3b6");
		
		
	
	
		//Object stoploss;
		float stoploss = 88;
		//stoploss = prevLow;
		float takeprofit = 999999;
		//float takeprofit = ((selltop*risk)+prevHigh);
		
		//String urlParameters = "direction=long&market=BTC-USD&leverage=1&size=100000000&stop_loss=prevLow&take_profit=((selltop*risk)+ask)";
		String urlParameters = String.format("direction=long&market=BTC-USD&leverage=1&size=100000000&stop_loss=%1$f&take_profit=%2$f", stoploss, takeprofit);
		
		//String.format("%d", 93);
		//float low = prices[0].ask;
	
		
		//Send post request
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