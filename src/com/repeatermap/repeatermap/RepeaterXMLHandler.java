package com.repeatermap.repeatermap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class RepeaterXMLHandler extends DefaultHandler {

	private ArrayList<RepeaterPoint> repeaterPoints = null;
	private int numPoints = 0;
	private boolean in_name = false;
	private boolean in_Style = false;
	private boolean in_IconStyle = false;
	private boolean in_Icon = false;
	private boolean in_href = false;
	private boolean in_LineStyle = false;
	private boolean in_color = false;
	private boolean in_width = false;
	private boolean in_PolyStyle = false;
	private boolean in_fill = false;
	private boolean in_Folder = false;
	private boolean in_Placemark = false;
	private boolean in_styleURL = false;
	private boolean in_description = false;
	private boolean in_Point = false;
	private boolean in_coordinates = false;
	
	public ArrayList<RepeaterPoint> getRepeaterPoints() {
		return this.repeaterPoints;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.repeaterPoints  = new ArrayList<RepeaterPoint>();
		numPoints = 0;
	}
	
	@Override
	public void endDocument() throws SAXException {
		//nothing
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if(localName.equals("name")) {
			this.in_name = true;
		} else if (localName.equals("Style")) {
			this.in_Style = true;
		} else if (localName.equals("IconStyle")) {
			this.in_IconStyle = true;
		} else if (localName.equals("Icon")) {
			this.in_Icon = true;
		} else if (localName.equals("href")) {
			this.in_href = true;
		} else if (localName.equals("LineStyle")){
			this.in_LineStyle = true;
		} else if (localName.equals("color")) {
			this.in_color = true;
		} else if (localName.equals("width")){
			this.in_width = true;
		} else if (localName.equals("PolyStyle")){
			this.in_PolyStyle = true;
		} else if (localName.equals("fill")) {
			this.in_fill = true;
		} else if (localName.equals("Folder")) {
			this.repeaterPoints.add(new RepeaterPoint());
			this.numPoints++;
			this.in_Folder = true;
		} else if (localName.equals("Placemark")) {
			this.in_Placemark = true;
		} else if (localName.equals("styleURL")) {
			this.in_styleURL = true;
		} else if (localName.equals("description")){
			this.in_description = true;
		} else if (localName.equals("Point")){
			this.in_Point = true;
		} else if (localName.equals("coordinates")) {
			this.in_coordinates = true;
		}
		
		
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if(localName.equals("name")) {
			this.in_name = false;
		} else if (localName.equals("Style")) {
			this.in_Style = false;
		} else if (localName.equals("IconStyle")) {
			this.in_IconStyle = false;
		} else if (localName.equals("Icon")) {
			this.in_Icon = false;
		} else if (localName.equals("href")) {
			this.in_href = false;
		} else if (localName.equals("LineStyle")){
			this.in_LineStyle = false;
		} else if (localName.equals("color")) {
			this.in_color = false;
		} else if (localName.equals("width")){
			this.in_width = false;
		} else if (localName.equals("PolyStyle")){
			this.in_PolyStyle = false;
		} else if (localName.equals("fill")) {
			this.in_fill = false;
		} else if (localName.equals("Folder")) {
			this.in_Folder = false;
		} else if (localName.equals("Placemark")) {
			this.in_Placemark = false;
		} else if (localName.equals("styleURL")) {
			this.in_styleURL = false;
		} else if (localName.equals("description")){
			this.in_description = false;
		} else if (localName.equals("Point")){
			this.in_Point = false;
		} else if (localName.equals("coordinates")) {
			this.in_coordinates = false;
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_Folder) {
			if(this.in_Placemark) { 
				if(this.in_name) {
					String name = "";
					for(int i = start; i <length; i++){
						name = name + ch[i];
					}
					this.repeaterPoints.get(this.numPoints - 1).setName(name);
				}
				if(this.in_description) {
					String desc = "";
					for(int i = start; i< length; i++) {
						desc = desc + ch[i];
					}
					this.repeaterPoints.get(this.numPoints - 1).setDescription(desc);
				}
				if(this.in_Point) {
					if(this.in_coordinates) {
						String coords = "";
						for(int i = start; i < length; i++) {
							coords = coords + ch[i];
						}
						
						String[] splitCoords = coords.split(",");
						this.repeaterPoints.get(this.numPoints - 1).setLatitude(Double.parseDouble(splitCoords[1]));
						this.repeaterPoints.get(this.numPoints - 1).setLongitude(Double.parseDouble(splitCoords[0]));
					}
				}
			}
			if(this.in_name) {
				//name of folder
			}
		}
		
	}
}
