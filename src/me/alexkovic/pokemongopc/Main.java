package me.alexkovic.pokemongopc;

import java.util.HashMap;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application implements MapComponentInitializedListener {
	public static GoogleMapView mapView;
	public static GoogleMap map;
	public static HashMap<String, Marker> markers = new HashMap<String, Marker>();
	public static HashMap<Marker, Long[]> pk = new HashMap<Marker, Long[]>();
	public static ImageView mapProgress;
	public static int cMapProgress;
	public static Marker marker;
	
	@Override
	public void start(Stage stage) throws Exception {
	    mapView = new GoogleMapView();
	    mapView.setPrefSize(1280, 720);
	    mapView.addMapInializedListener(this);

	    cMapProgress = 10;
	    mapProgress = new ImageView("/p10.png");
	    mapProgress.setLayoutX(1220);
	    mapProgress.setLayoutY(10);
	    mapView.getChildren().add(mapProgress);
	    Scene scene = new Scene(mapView);
	    
	    stage.setTitle("PokemonGoPC Client");
	    stage.setScene(scene);
	    stage.show();
	    
	    Thread user = new Thread(new User());
	    user.start();
	}
	
	@Override
	public void mapInitialized() {
	    MapOptions mapOptions = new MapOptions();
	    mapOptions.center(new LatLong(User.startLoc[0], User.startLoc[1]))
	            .mapType(MapTypeIdEnum.ROADMAP)
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(false)
	            .zoom(15);
	    mapOptions.getJSObject().setMember("disableDoubleClickZoom", true);
	    map = mapView.createMap(mapOptions);
	    map.addUIEventHandler(UIEventType.dblclick, new EventHandler());
	    
	    MarkerOptions markerOptions = new MarkerOptions();
	    markerOptions.position(new LatLong(51.2997870,4.4302240))
	                .visible(Boolean.TRUE)
	                .title("You");
	    markerOptions.getJSObject().call("draggable", true);
	    marker = new Marker(markerOptions);
	    map.addMarker(marker);
	    
	    new AnimationTimer() {
            @Override public void handle(long currentNanoTime){
            	Double[] loc = RouteHandler.getCurrentLocation(System.currentTimeMillis());
            	marker.setPosition(new LatLong(loc[0], loc[1]));
            }
        }.start();
	}
	
	public static void main(String[] args) {
	    launch(args);
	}
}