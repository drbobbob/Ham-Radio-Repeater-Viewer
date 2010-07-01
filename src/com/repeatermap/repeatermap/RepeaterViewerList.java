package com.repeatermap.repeatermap;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import java.text.DecimalFormat;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.repeatermap.repeatermap.R;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;

public class RepeaterViewerList extends Activity {

	LocationManager lm;
	Location here;
	GeoPoint p;
	
	ArrayList<String> repeaterText;
	ArrayAdapter<String> aa;
	
	ArrayList<RepeaterPoint> repeaters;
	
	int LatSpan = 600000;
	int LongSpan = 600000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        repeaters = new ArrayList<RepeaterPoint>();
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        
        here = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
        
        if(here != null) {
        	p = new GeoPoint( (int) (here.getLatitude() * 1E6), (int) (here.getLongitude() * 1E6));
        } else {
        	p = new GeoPoint((int)(37.730385 * 1E6),(int)(-122.497790 * 1E6));
        	here = new Location("GPS");
        	here.setLatitude(37.730385);
        	here.setLongitude(-122.497790);
        }
	
        
        repeaterText = new ArrayList<String>();
  	  ListView lv = (ListView)findViewById(R.id.myListView);
  	  aa = new ArrayAdapter<String>(getApplicationContext(), 
  	                                android.R.layout.simple_list_item_1,
  	                                repeaterText);
  	  lv.setAdapter(aa);
       
  	  refreshRepeaterLocations();

  	  Button find_repeaters = (Button) findViewById(R.id.find_repeaters_text);
  	  find_repeaters.setOnClickListener(new OnClickListener() {
    	
  		  @Override
  		  public void onClick(View arg0) {
    		refreshRepeaterLocations();
  		  }
  	  });

  	  Button open_map = (Button) findViewById(R.id.open_map);
  	  open_map.setOnClickListener(new OnClickListener() {
    	
  		  @Override
  		  public void onClick(View arg0) {
    		Intent myIntent = new Intent();
    		myIntent.setClassName("com.repeatermap.repeatermap", "com.repeatermap.repeatermap.RepeaterViewerMap");
    		startActivity(myIntent);
  		  }
  	  });
  	  
	}

public void refreshRepeaterLocations() {
    here = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
    
    if(here != null) {
    	p = new GeoPoint( (int) (here.getLatitude() * 1E6), (int) (here.getLongitude() * 1E6));
    } else {
    	p = new GeoPoint((int)(37.730385 * 1E6),(int)(-122.497790 * 1E6));
    	here = new Location("GPS");
    	here.setLatitude(37.730385);
    	here.setLongitude(-122.497790);
    }

    String latLongString = "Lat: " + String.valueOf(here.getLatitude()) + "Long: " + String.valueOf(here.getLongitude());
    
    TextView myLocationText = (TextView)findViewById(R.id.myLocationText);
    myLocationText.setText("Your Current Position is:\n" + latLongString);
    
	URL url;

		try {
			//grab url data and output it
			
			Double minLat, minLong, maxLat, maxLong;
			minLat = (p.getLatitudeE6() - (LatSpan / 2)) / ((double)1E6);
			maxLat = (p.getLatitudeE6() + (LatSpan / 2)) / ((double)1E6);
			minLong = (p.getLongitudeE6() - (LongSpan / 2)) / ((double)1E6);
			maxLong = (p.getLongitudeE6() + (LongSpan / 2)) / ((double)1E6);
			
			String urlString = "http://k5ehx.net/repeaters/kmldynamic.php?BBOX=" + minLong.toString() + "," + minLat.toString() + "," + maxLong.toString() + "," + maxLat.toString();
			
			url = new URL(urlString);
			//url = new URL("http://k5ehx.net/repeaters/kmldynamic.php?BBOX=-122.497790,37.730385,-122.380087,37.812331");

		
		//get parser
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		
		//get XMLReader of parser
		XMLReader xr = sp.getXMLReader();
		
		//create content handler and apply to xml reader
		RepeaterXMLHandler rxh = new RepeaterXMLHandler();
		xr.setContentHandler(rxh);
		
		//parse xml-data from url-call
		xr.parse(new InputSource(url.openStream()));
		
		repeaters = rxh.getRepeaterPoints();
		
		refreshListView();
		
		} catch (Exception e) {
			
			e.toString();
		}

	}

	public void refreshListView() {
		repeaterText.clear();
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("##0.00");
		if(repeaters.size()>0) {
			Iterator<RepeaterPoint> e = repeaters.iterator();
		      do {
		        // Get the name and location
		        RepeaterPoint rp = e.next();          
		        Location location = new Location("GPS"); 
		        location.setLatitude(rp.getLatitude());
		        location.setLongitude(rp.getLongitude());
		        
		        // Find their distance from you
		        double distance = (here.distanceTo(location) * 0.000621371192);

		        String str = rp.getName() + "\n" + df.format(distance) + " miles";

		        // Update the ArrayList
		        repeaterText.add(str);
		      } while (e.hasNext());	
		      aa.notifyDataSetChanged();

		}
	
	}
}