package me.alexkovic.pokemongopc;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

public class User implements Runnable {
	public static PokemonGo go = null;
	Map map;
	long lastMapScan = -10000;
    public static Double[] startLoc = {51.3061039, 4.4339094};
    OkHttpClient http;
	
	@Override
	public void run(){
		while(true){
			try{
				if(go == null){
					System.out.println("Logging in...");
			        http = new OkHttpClient();
			        go = new PokemonGo(http);
			        go.login(new PtcCredentialProvider(http, "prulspeskstesk", "havfochwoft"));	
			        System.out.println("Logged in! Setting location...");
			        Thread.sleep(500);
			        go.setLocation(startLoc[0], startLoc[1], 1);
			        System.out.println("Set location!");
				}
		        if(lastMapScan + 10000 < System.currentTimeMillis()){
		        	Double[] loc = RouteHandler.getCurrentLocation(System.currentTimeMillis());
		        	go.setLocation(loc[0], loc[1], 1);
		        	MapHandler.updateMapProgress(10);
		        	System.out.println("Updating map...");
			        map = go.getMap();
			        MapHandler.updateMap(map.getMapObjects().getPokestops(), map.getGyms(), map.getCatchablePokemon());
		        	MapHandler.updateMapProgress(0);
		        	System.out.println("Updated map!");
			        lastMapScan = System.currentTimeMillis();
		        }else{
					long curr = System.currentTimeMillis();
		        	if(lastMapScan + 1000 < curr){
		        		if(lastMapScan + 2000 < curr){
		        			if(lastMapScan + 3000 < curr){
		        				if(lastMapScan + 4000 < curr){
		        					if(lastMapScan + 5000 < curr){
		        						if(lastMapScan + 6000 < curr){
		        							if(lastMapScan + 7000 < curr){
		        								if(lastMapScan + 8000 < curr){
		        									if(lastMapScan + 9000 < curr){
		        						        		MapHandler.updateMapProgress(9);
		        						        	}else{
		        						        		MapHandler.updateMapProgress(8);
		        						        	}
		        					        	}else{
		        					        		MapHandler.updateMapProgress(7);
		        					        	}
		        				        	}else{
		        				        		MapHandler.updateMapProgress(6);
		        				        	}
		        			        	}else{
		        			        		MapHandler.updateMapProgress(5);
		        			        	}
		        		        	}else{
		    			        		MapHandler.updateMapProgress(4);
		    			        	}
		    		        	}else{
					        		MapHandler.updateMapProgress(3);
					        	}
				        	}else{
				        		MapHandler.updateMapProgress(2);
				        	}
			        	}else{
			        		MapHandler.updateMapProgress(1);
			        	}
		        	}
		        }
			}catch(AsyncPokemonGoException | LoginFailedException | RemoteServerException e){
				System.out.println(e.toString() + " occured. Refreshing...");
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e1){
					e1.printStackTrace();
				}
				go = null;
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
