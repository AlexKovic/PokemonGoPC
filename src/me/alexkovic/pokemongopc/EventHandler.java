package me.alexkovic.pokemongopc;

import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.object.LatLong;
import netscape.javascript.JSObject;

public class EventHandler implements UIEventHandler {
	
	@Override
	public void handle(JSObject jso){
		LatLong ll = new LatLong((JSObject) jso.getMember("latLng"));
		double lat = ll.getLatitude();
		double lng = ll.getLongitude();
		Double[] to = {lat, lng};
		System.out.println("Clicked on: " + lat + ", " + lng);
		//MapHandler.pointWay(to);
		RouteHandler.startNewRoute(RouteHandler.getCurrentLocation(System.currentTimeMillis()), to, 100);
	}
}
