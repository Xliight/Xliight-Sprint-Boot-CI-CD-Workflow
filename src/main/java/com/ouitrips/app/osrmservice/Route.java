package com.ouitrips.app.osrmservice;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Routing and direction tasks using the Open Source Routing Machine HTTP API.
 * @author An Tran
 * @see //http://project-osrm.org/docs/v5.7.0/api/#route-service
 * 
 */
public class Route {

	private static final Logger logger = LoggerFactory.getLogger(Route.class);

	/**
	 * Finds the fastest route between coordinates in the supplied order.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @return A JSON object containing the response code, an array of waypoint objects, and an array of route objects
	 * 
	 */
	public JSONObject getFastestRoute(String coordinates) {
		String url = String.format("http://router.project-osrm.org/route/v1/car/%s?geometries=geojson&overview=full", coordinates);

		JSONObject result = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()){
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding route route: {}", e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Finds the fastest route between coordinates in the supplied order, accounting for mode of travel.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @param profile The string specifying mode of travel. Valid values are 'car', 'bike' and 'foot'.
	 * @return A JSON object containing the response code, an array of waypoint objects, and an array of route objects.
	 */
	public JSONObject getFastestRoute(String coordinates, String profile) {
		String url = String.format("http://router.project-osrm.org/route/v1/%s/%s?geometries=geojson&overview=full", profile, coordinates);

		JSONObject result = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()){
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding route route: {}", e.getMessage(), e);
		}
		return result;
	}

}
