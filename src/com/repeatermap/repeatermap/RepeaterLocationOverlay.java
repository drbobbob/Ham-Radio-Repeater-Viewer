package com.repeatermap.repeatermap;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Overlay class used to draw circles on the locations
 * of each contact in the contact list, along with their 
 * name and a line connecting them to your current
 * position.
 * 
 * @author Reto Meier
 * Author of Professional Android Application Development
 * http://www.amazon.com/gp/product/0470344717?tag=interventione-20
 *
 */
public class RepeaterLocationOverlay extends Overlay {

  private Context context;
  private ArrayList<RepeaterPoint> repeaters;
  private Location location;
  private GeoPoint locationPoint;
  
  private Paint paint;
  private Paint backPaint;

	GeoPoint center;
	int LatSpan;
	int LongSpan;
  
//  private MapView mapView;

  
  private static int markerRadius = 7;

  /** Get your current location */
	public Location getLocation() {
		return location;
	}
  /** Set your current location */
	public void setLocation(Location location) {
	  this.location = location;
		
      Double latitude = location.getLatitude()*1E6;
      Double longitude = location.getLongitude()*1E6;
    
      locationPoint = new GeoPoint(latitude.intValue(),longitude.intValue());      
	}  
 
	public void setMapViewArea(MapView mapView) {
		center = mapView.getMapCenter();
		LatSpan = mapView.getLatitudeSpan();
		LongSpan = mapView.getLongitudeSpan();
		locationPoint = center;
	}
	

	
	/** Refresh the locations of each of the contacts */
  public void refreshRepeaterLocations() {
	  URL url;

		try {
			//grab url data and output it
			
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
  		
  		repeaters = rxh.getRepeaterPoints();
  		
		} catch (Exception e) {
			
			e.toString();
		}

  }
  
  /**
   * Create a new FriendLocationOverlay to show your contact's locations on a map
   * @param _context Parent application context
   */
  public RepeaterLocationOverlay(Context _context) {
    super();
    
    context = _context;
    repeaters = new ArrayList<RepeaterPoint>();
    //refreshFriendLocations();

    // Create the paint objects
    backPaint = new Paint();
    backPaint.setARGB(200, 200, 200, 200);
    backPaint.setAntiAlias(true);
    
    paint = new Paint();
    paint.setARGB(255, 10, 10, 255);
    paint.setAntiAlias(true);
    paint.setFakeBoldText(true);    
  }
  
  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow) {	  

    // Get the map projection to convert lat/long to screen coordinates
    Projection projection = mapView.getProjection();
    
    Point lPoint = new Point();
    projection.toPixels(locationPoint, lPoint);
    
    // Draw the overlay
    if (shadow == false) {
      if (repeaters.size() > 0) {
        Iterator<RepeaterPoint> e = repeaters.iterator();
        do {
          // Get the name and location of each contact
          RepeaterPoint repeater = e.next();          
          
          // Convert the lat / long to a Geopoint
          Double latitude = repeater.getLatitude()*1E6;
          Double longitude = repeater.getLongitude()*1E6;
          GeoPoint geopoint = new GeoPoint(latitude.intValue(),longitude.intValue());

            Point point = new Point();
            projection.toPixels(geopoint, point);
            
            // Draw a marker at the contact's location.
            RectF oval = new RectF(point.x-markerRadius,
                                   point.y-markerRadius,
                                   point.x+markerRadius,
                                   point.y+markerRadius);
            
            canvas.drawOval(oval, backPaint);
            oval.inset(2, 2);
            canvas.drawOval(oval, paint);
            
            // Draw the contact's name next to their position.
            float textWidth = paint.measureText(repeater.getName());
            float textHeight = paint.getTextSize();
            RectF textRect = new RectF(point.x+markerRadius, point.y-textHeight,
                                       point.x+markerRadius+8+textWidth, point.y+4);
            canvas.drawRoundRect(textRect, 3, 3, backPaint);
            canvas.drawText(repeater.getName(), point.x+markerRadius+4, point.y, paint);
          
        } while (e.hasNext());
      }
    }
    super.draw(canvas, mapView, shadow);
  }
	  
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
	  // Do not react to screen taps.
	  return false;
	}
}