package me.alexkovic.pokemongopc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.util.PokeDictionary;

import POGOProtos.Enums.TeamColorOuterClass.TeamColor;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import netscape.javascript.JSObject;

public class MapHandler {
	
	public static void updateMap(Collection<Pokestop> pokestops, List<Gym> gyms, List<CatchablePokemon> pokemon){
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	for(Pokestop ps : pokestops){
	        		String pos = ps.getLatitude() + ":" + ps.getLongitude();
	        		if(!Main.markers.containsKey(pos)){
			        	MarkerOptions psOptions = new MarkerOptions();
			    		psOptions.position(new LatLong(ps.getLatitude(), ps.getLongitude()))
			            	.visible(true)
			            	.title("PokeStop")
			            	.icon(createMarkerImage("/pokestop.png", "png"));
			    	    Marker marker = new Marker(psOptions);
			    	    Main.map.addMarker(marker);
			    	    Main.markers.put(pos, marker);
	        		}
	        	}
	        	for(Gym gym : gyms){
	        		String pos = gym.getLatitude() + ":" + gym.getLongitude();
	        		if(!Main.markers.containsKey(pos)){
	        			String icon = "";
	        			if(gym.getOwnedByTeam() == TeamColor.BLUE){
	        				icon = createMarkerImage("/bluegym.png", "png");
	        			}else if(gym.getOwnedByTeam() == TeamColor.RED){
	        				icon = createMarkerImage("/redgym.png", "png");	        				
	        			}else if(gym.getOwnedByTeam() == TeamColor.YELLOW){
	        				icon = createMarkerImage("/yellowgym.png", "png");	        				
	        			}else{
	        				icon = createMarkerImage("/whitegym.png", "png");	        				
	        			}
			        	MarkerOptions gymOptions = new MarkerOptions();
			    		gymOptions.position(new LatLong(gym.getLatitude(), gym.getLongitude()))
			            	.visible(true)
			            	.title("Gym")
			            	.icon(icon);
			    	    Marker marker = new Marker(gymOptions);
			    	    Main.map.addMarker(marker);
			    	    Main.markers.put(pos, marker);
	        		}
	        	}
	        	for(CatchablePokemon cp : pokemon){
	        		for(Marker m : Main.pk.keySet()){
	        			m.getJSObject().call("setMap", (Object) null);
	        		}
	        		MarkerOptions psOptions = new MarkerOptions();
//	        		int seconds = (int) (cp.getExpirationTimestampMs() / 1000) % 60 ;
//        			int minutes = (int) ((cp.getExpirationTimestampMs() / (1000*60)) % 60);
		    		psOptions.position(new LatLong(cp.getLatitude(), cp.getLongitude()))
		            	.visible(true)
		            	.title(PokeDictionary.getDisplayName(cp.getPokemonIdValue(), Locale.ENGLISH) + " (" + cp.getExpirationTimestampMs() + ")")
		            	.icon(createMarkerImage("/" + cp.getPokemonIdValue() + ".png", "png"));
		    	    Marker marker = new Marker(psOptions);
		    	    Main.map.addMarker(marker);
		    	    Long[] l = {cp.getExpirationTimestampMs(), System.currentTimeMillis()};
		    	    Main.pk.put(marker, l);
	        	}
//	        	Marker[] ms = {};
//	        	for(Marker m : Main.pk.keySet()){
//	        		if(Main.pk.get(m)[0] + Main.pk.get(m)[1] < System.currentTimeMillis()){
//	        			m.getJSObject().call("setMap", (Object) null);
//	        			ms[ms.length] = m;
//	        		}else{
//	        			String name = (String) m.getJSObject().call("getTitle");
//	        			System.out.println(name);
//	        			String pn = name.split("\\s+")[0];
//	        			long millis = Main.pk.get(m)[0] - (System.currentTimeMillis() - Main.pk.get(m)[1]);
//	        			
//	        			int seconds = (int) (millis / 1000) % 60 ;
//	        			int minutes = (int) ((millis / (1000*60)) % 60);
//	        			m.setTitle(pn + "(" + minutes + " min, " + seconds + " sec");
//	        		}
//	        	}
//	        	for(Marker m : ms){
//	        		Main.pk.remove(m);
//	        	}
	        }
	    });
	}
	
	public static void updateMapProgress(int stage){
		if(stage != Main.cMapProgress){
			Main.cMapProgress = stage;
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	Main.mapProgress.setImage(new Image("/p" + stage + ".png"));
		        }
		    });
		}
	}
	
	public static void pointWay(Double[] to){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	Double[] from = {User.go.getLatitude(), User.go.getLongitude()};
	    		System.out.println("Starting... from " + from[0] + ", " + from[1] + " to " + to[0] + ", " + to[1]);
	    		RouteHandler.startNewRoute(from, to, 10);
	    		System.currentTimeMillis();
	    		int ms = 0;
	    		for(long i = 0; i < (60000 * 20); i += 60000){
	    			ms++;
	    			Double[] loc = RouteHandler.getCurrentLocation(System.currentTimeMillis() + i);
	    			MarkerOptions psOptions = new MarkerOptions();
	        		psOptions.position(new LatLong(loc[0], loc[1]))
	                	.visible(true)
	                	.title("Location #" + ms);
	        	    Marker marker = new Marker(psOptions);
	        	    Main.map.addMarker(marker);
	        	    System.out.println("Put marker at " + loc[0] + ", " + loc[1]);
	    		}
	        }
	    });
	}
	
	public static String createMarkerImage(String uri, String type) {        
        String dataURI = null;
        
        URL myURL = MapHandler.class.getResource(uri);
        
        if (myURL != null) {
            String myURI = myURL.toExternalForm();
            Image img = new Image(myURI);
            
            String imageMimeType = "image/" + type;
            
            try {
                dataURI = "data:" + imageMimeType + ";base64,(" + 
                        javax.xml.bind.DatatypeConverter.printBase64Binary(getImageBytes(SwingFXUtils.fromFXImage(img, null), type)) + ")";
            } catch (IOException ioe) {
                System.out.println("Cannot create marker image: " + ioe);
                dataURI = null;
            }
        }
        
        return dataURI;
        
    }
	
	private static byte[] getImageBytes(BufferedImage image, String type) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ImageIO.write(image, type, bos);
        byte[] imageBytes = bos.toByteArray();
        bos.close();

        return imageBytes;
		
	}
}
