package com.hotspot;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.MapView.LayoutParams;

public class Maps extends MapActivity {
   
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.getController().setCenter(getPoint(Home.latitude,Home.longitude));
        mapView.getController().setZoom(15);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        SpotItemizedOverlay itemizedoverlay = new SpotItemizedOverlay(drawable, mapView.getContext());
        
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
        int size=0;
        for (MapData md : Home.mapData)
        {
           	OverlayItem overlayitem = new OverlayItem(md.getPoint(),  md.getTitle(),  md.getCollocation());
           	
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, md.getPoint(), LayoutParams.MODE_VIEW);

            LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.balloon, null);
            mapView.addView(ll, lp);

            TextView tv = new TextView(getApplicationContext());
            tv.setText( md.getCollocation());
            //tv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.marker));
            tv.setShadowLayer(2.3f, 1.7f, 1.7f, Color.rgb(71, 71, 71));
            tv.setTextColor(Color.WHITE);
            
            int reviewCount = Integer.parseInt(md.getReviewCount());
            if (reviewCount < 20)
            	size = 14;
            else if(reviewCount < 50)
            	size = 20;
            else if(reviewCount < 75)
            	size = 30;
            else
            	size = 40;

            tv.setTextSize(size);
            tv.setTypeface(null, Typeface.BOLD);
            ll.addView(tv);
        	itemizedoverlay.addOverlay(overlayitem);
        }
        mapOverlays.add(itemizedoverlay);
	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
}
