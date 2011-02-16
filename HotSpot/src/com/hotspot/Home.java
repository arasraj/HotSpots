package com.hotspot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.maps.GeoPoint;

public class Home extends ListActivity {

	static final ArrayList<MapData> mapData = new ArrayList<MapData>();
	static String displayWordsForMap = "";
	static double latitude;
	static double longitude;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        StringBuilder spotsJSON = new StringBuilder();
        final ArrayList<String> tst = new ArrayList<String>();
        final ArrayList<ArrayList<String>> rvw = new ArrayList<ArrayList<String>>();
                
        try {
        	LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        	Criteria crit = new Criteria();
        	crit.setAccuracy(Criteria.ACCURACY_FINE);
        	String provider = lm.getBestProvider(crit, true);
        	Location loc = lm.getLastKnownLocation(provider);
        	double lat = loc.getLatitude();
        	latitude = lat;
    		double lng = loc.getLongitude();
    		longitude = lng;
        	
        	URL hs = new URL("http://xxx.xxx.xxx.xxx:8888/hotspots?lat=" + Double.toString(lat) + "&lng=" + Double.toString(lng));
			URLConnection apiConn = hs.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(apiConn.getInputStream()));
						
			String input;
			while ((input = in.readLine()) != null)
				spotsJSON.append(input);
				
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    try {
	        JSONArray spots = new JSONArray(spotsJSON.toString());
	        for(int i=0;i<(spots.length());i++)
	        {
	            JSONObject jsonObj = spots.getJSONObject(i);
	            String name = jsonObj.getString("name");
	            String herenow =jsonObj.getString("herenow");
	            String address = jsonObj.getString("address");
	            String reviewCount = jsonObj.getString("review_count");
	            String photoUrl = jsonObj.getString("photo_url");
	            String smallPhotoUrl = jsonObj.getString("small_photo_url");
	            Double geoLat = Double.parseDouble(jsonObj.getString("geolat"));
	            Double geoLong = Double.parseDouble(jsonObj.getString("geolong"));
            	String mapWord = jsonObj.getString("map_word");
            	mapData.add(new MapData(new GeoPoint((int)(geoLat * 1e6), (int)(geoLong * 1e6)), mapWord, name, reviewCount));
            	
            	JSONArray reviews = jsonObj.getJSONArray("reviews");
	            ArrayList<String> individualReview = new ArrayList<String>();
	            individualReview.add(address);
	            individualReview.add(photoUrl);
	            individualReview.add(smallPhotoUrl);
	            
	            for (int j=0; j<reviews.length(); j++)
	            {
	            	JSONObject reviewJsonObj = reviews.getJSONObject(j);
	            	String rating = reviewJsonObj.getString("rating");
	            	String text = reviewJsonObj.getString("text");
	            	String username = reviewJsonObj.getString("username");
	            	individualReview.add("Rating: " + rating + "\n\n" + "Review: " + text + "\n\n" + "Username: " 	+ username);
	            }
	            individualReview.add(name);
	            rvw.add(individualReview);
	            String dist = jsonObj.getString("distance");
	            tst.add(name + "\n" +  address + "\n" + herenow + " people here\n" + dist + " meters");
	            System.out.println("name , dist, hereno" + name + herenow + dist); 

	        } 
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, tst));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        	ArrayList<String> reviews = rvw.get((int)id);
        	Intent reviewIntent = new Intent();
        	reviewIntent.putExtra("address", reviews.get(0));
        	reviewIntent.putExtra("photoUrl", reviews.get(1));
        	reviewIntent.putExtra("smallPhotoUrl", reviews.get(2));
        	reviewIntent.putExtra("r1", reviews.get(3));
        	reviewIntent.putExtra("r2", reviews.get(4));
        	reviewIntent.putExtra("r3", reviews.get(5));
        	reviewIntent.putExtra("venue", reviews.get(6));
        	reviewIntent.setClassName("com.hotspot", "com.hotspot.Reviews");
        	startActivity(reviewIntent); 
          }
        }); 
      }
}

class MapData{
	private GeoPoint p;
	private String collocation;
	private String title;
	private String reviewCount;
	
	MapData(GeoPoint p, String collocation, String title, String reviewCount){
		this.p = p;
		this.collocation = collocation;
		this.title = title;
		this.reviewCount = reviewCount;
	}
	
	public GeoPoint getPoint(){ return p; }
	public String getCollocation(){ return collocation; } 
	public String getTitle(){ return title; }
	public String getReviewCount() { return reviewCount; }
}
	