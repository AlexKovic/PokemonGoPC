package me.alexkovic.pokemongopc;

public class RouteHandler {

	public static Double[] p1;
	public static Double[] p2;
	public static Double dist;
	public static Double kph;
	public static Long startTime;
	
	public static void startNewRoute(Double[] startPoint, Double[] endPoint, double kmPerH){
		p1 = startPoint;
		p2 = endPoint;
		dist = DistanceCalc.distance(startPoint[0], startPoint[1], endPoint[0], endPoint[1], "K");
		kph = kmPerH;
		startTime = System.currentTimeMillis();
	}
	
	public static Double[] getCurrentLocation(long millis){
		if(p1 == null || p2 == null || dist == null || kph == null || startTime == null){
			return User.startLoc;
		}
		double addlat = ((double) distTraveled((long) millis - (long) startTime) * (double) Math.abs((double) p1[0] - (double) p2[0])) / (double) dist;
		double addlng = ((double) distTraveled((long) millis - (long) startTime) * (double) Math.abs((double) p1[1] - (double) p2[1])) / (double) dist;
		if(p1[0] > p2[0]){
			if(p1[1] > p2[1]){
				Double[] p = {p1[0] - addlat, p1[1] - addlng};
				return p;
			}else{
				Double[] p = {p1[0] - addlat, p1[1] + addlng};
				return p;
			}
		}else{
			if(p1[1] > p2[1]){
				Double[] p = {p1[0] + addlat, p1[1] - addlng};
				return p;
			}else{
				Double[] p = {p1[0] + addlat, p1[1] + addlng};
				return p;
			}
		}
	}
	
	public static double distTraveled(long timeElapsed){
		double distT = (double) ((double) kph * ((long) timeElapsed / (1000f * 60f * 60f)));
		if(distT > dist){
			distT = dist;
		}
		return distT;
	}
	
//	public static double distTraveledx1000(long timeElapsed){
//		return kph * (timeElapsed / (60 * 60));
//	}
}
