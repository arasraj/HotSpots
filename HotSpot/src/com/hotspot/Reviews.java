package com.hotspot;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Reviews extends Activity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        
        Bundle extras = getIntent().getExtras();

        TextView text = (TextView) findViewById(R.id.Review1);
        text.setText(extras.getString("r1"));
        text = (TextView) findViewById(R.id.Review2);
        text.setText(extras.getString("r2"));
        text = (TextView) findViewById(R.id.Review3);
        text.setText(extras.getString("r3"));
        text = (TextView) findViewById(R.id.VenueName);
        text.setText(extras.getString("venue"));
        
        ImageView imgView =(ImageView)findViewById(R.id.YelpLarge);
        Drawable drawable2 = loadImage(extras.getString("photoUrl"));
        imgView.setImageDrawable(drawable2);

    }

   private Drawable loadImage(String url)
   {
		try
		{
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		}
		catch (Exception e) {e.printStackTrace(); return null;	}
	}

}
