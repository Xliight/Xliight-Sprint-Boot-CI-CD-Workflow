package com.ouitrips.app.osrmservice;

import java.net.URI;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Roadway finding task that the nearest roadway to a given location using the Open Source Routing HTTP API.
 * @author An Tran
 * @see //http://project-osrm.org/docs/v5.7.0/api/#nearest-service
 */
public class Nearest {

	private static final Logger logger = LoggerFactory.getLogger(Nearest.class);
	private static final String HEADER_ACCEPT = "accept";
	private static final String HEADER_VALUE = "application/json";


	/**
	 * Finds the nearest route to the given location.
	 * @param coordinates The string containing a single comma separated lon/lat pair. Only one coordinate pair is permitted.
	 * @return A JSON object containing the response code, and an array of waypoint objects.
	 * 
	 */
	public JSONObject findNearestRoute(String coordinates) {
		String url = String.format("http://router.project-osrm.org/nearest/v1/car/%s", coordinates);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader(HEADER_ACCEPT, HEADER_VALUE);
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding nearest route: {}", e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Finds the nearest route to the given location, limiting the number of results to a specified number.
	 * @param coordinates The string containing a single comma separated lon/lat pair. Only one coordinate pair is permitted.
	 * @param maxReturns The maximum number of matches to return.
	 * @return A JSON object containing the response code, and an array of waypoint objects.
	 */
	public JSONObject findNearestRoute(String coordinates, int maxReturns) {
		String url = String.format("http://router.project-osrm.org/nearest/v1/car/%s?number=%d", coordinates, maxReturns);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader(HEADER_ACCEPT, HEADER_VALUE);
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding nearest route with max returns: {}", e.getMessage(), e);
		}
		return result;
	}	
	
	/**
	 * Finds the nearest route to the given location, accounting for mode of travel.
	 * @param coordinates The string containing a single comma separated lon/lat pair. Only one coordinate pair is permitted.
	 * @param profile The mode of travel. Valid values are 'car', 'bike' and 'foot'.
	 * @return A JSON object containing the response code, and an array of waypoint objects.
	 */
	public JSONObject findNearestRoute(String coordinates, String profile) {
		String url = String.format("http://router.project-osrm.org/nearest/v1/%s/%s", profile, coordinates);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader(HEADER_ACCEPT, HEADER_VALUE);
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding nearest route with profile: {}", e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * Finds the nearest route to the given location, accounting for mode of travel and limiting the number of results to a specified number.
	 * @param coordinates The string containing a single comma separated lon/lat pair. Only one coordinate pair is permitted.
	 * @param profile The mode of travel. Valid values are 'car', 'bike' and 'foot'.
	 * @return A JSON object containing the response code, and an array of waypoint objects.
	 */
	public JSONObject findNearestRoute(String coordinates, String profile, int maxReturns) {
		String url = String.format("http://router.project-osrm.org/nearest/v1/%s/%s?number=%d", profile, coordinates, maxReturns);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader(HEADER_ACCEPT, HEADER_VALUE);
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			logger.error("Error finding nearest route with profile and max returns: {}", e.getMessage(), e);
		}
		return result;
	}
}
