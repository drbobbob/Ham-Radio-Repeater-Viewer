package com.repeatermap.repeatermap;

import java.net.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import android.graphics.*;
import android.graphics.drawable.*;


import com.google.android.maps.*;

import android.location.*;

import java.util.ArrayList;
import java.util.List;

public class RepeaterViewerMap extends MapActivity {
	private MapView mapView;
	private MapController mc;
	private GeoPoint p;
	private LocationManager lm;
	private Location here;
	private RepeaterLocationOverlay repLocOverlay;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        mapView = (MapView) findViewById(R.id.mapview1);
        mc = mapView.getController();
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        
        here = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
        
        if(here != null) {
        	p = new GeoPoint( (int) (here.getLatitude() * 1E6), (int) (here.getLongitude() * 1E6));
        } else {
        	p = new GeoPoint((int)(37.730385 * 1E6),(int)(-122.497790 * 1E6));
        }
        mc.setCenter(p);
        mc.setZoom(12);
        mapView.invalidate();
        
        /*
        //add locaiton mark
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay("my location");
        List<Overlay> list = mapView.getOverlays();
        list.add(myLocationOverlay);
        */
        repLocOverlay = new RepeaterLocationOverlay(getApplicationContext());
        repLocOverlay.setMapViewArea(mapView);
        repLocOverlay.refreshRepeaterLocations();
        List<Overlay> list = mapView.getOverlays();
        list.add(repLocOverlay);
        
        
        Button find_repeaters = (Button) findViewById(R.id.find_repeaters);
        find_repeaters.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		repLocOverlay.setMapViewArea(mapView);
        		repLocOverlay.refreshRepeaterLocations();
        		
        		/*
        		URL url;
        		try {
        			//grab url data and output it
        			
        			GeoPoint center = mapView.getMapCenter();
        			int LatSpan = mapView.getLatitudeSpan();
        			int LongSpan = mapView.getLongitudeSpan();
        			
        			Double minLat, minLong, maxLat, maxLong;
        			minLat = (center.getLatitudeE6() - (LatSpan / 2)) / ((double)1E6);
        			maxLat = (center.getLatitudeE6() + (LatSpan / 2)) / ((double)1E6);
        			minLong = (center.getLongitudeE6() - (LongSpan / 2)) / ((double)1E6);
        			maxLong = (center.getLongitudeE6() + (LongSpan / 2)) / ((double)1E6);
        			
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
            		
            		ArrayList<RepeaterPoint> repeaterPoints = rxh.getRepeaterPoints();
            		
            		GeoPoint repeaterLoc;
            		RepeaterPoint rp;
            		MyLocationOverlay repeaterOverlay;
            		List<Overlay> overlayList = mapView.getOverlays();
            		//update some form of display
            		for(int i = 0; i < repeaterPoints.size(); i++) {
            	        rp = repeaterPoints.get(i);
            	        repeaterLoc = new GeoPoint((int)(rp.getLatitude() * 1E6),(int)(rp.getLongitude()*1E6));
            			mc.setCenter(repeaterLoc);
            			repeaterOverlay = new MyLocationOverlay(rp.getName());
            			overlayList.add(repeaterOverlay);
            		}
            		
            		mc.setCenter(center);
            		
        		} catch (Exception e) {
        			int i = 0;
        			//I broke it
        		}
        		*/
        	}
        });
    /*
        Button show_map = (Button) findViewById(R.id.show_map);
        show_map.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		try {
        		setContentView(R.layout.map);
        		} catch (Exception e) {
        			e.toString();
        		}
        	}
        	
        });
    */
    }
    
    @Override
    protected boolean isRouteDisplayed() {
    	
    	return false;
    }
    

    protected class MyLocationOverlay extends com.google.android.maps.Overlay {
    	
    	private String displayText;
    	
    	public MyLocationOverlay (String text) {
    		super();
    		displayText = text;
    		
    	}
    	
    	@Override
    	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			Paint paint = new Paint();
			
			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(p, myScreenCoords);
			
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);
			
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
			
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText(displayText, myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
    }
    

    
}