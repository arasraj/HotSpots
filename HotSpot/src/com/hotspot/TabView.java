package com.hotspot;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabView extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    Intent intent; 
	      
	    intent = new Intent().setClass(this, Home.class);

	    spec = tabHost.newTabSpec("home_screen").setIndicator("Home").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, Maps.class);
	    spec = tabHost.newTabSpec("map_screen").setIndicator("Map").setContent(intent);
	    tabHost.addTab(spec);
	}
}
