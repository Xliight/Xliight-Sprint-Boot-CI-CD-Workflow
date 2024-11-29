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
 * Map matching matches/snaps given GPS points to the road network in the most plausible way using the Open Source Routing Machine HTTP API.
 * Similar to Nearest except that multiple input points are permitted.
 * @author An Tran
 * @see //http://project-osrm.org/docs/v5.7.0/api/#match-service
 * 
 */
public class Match {

	private static final Logger logger = LoggerFactory.getLogger(Match.class);

	/**
	 * Snaps the supplied coordinates to the road network, and returns the most likely route given the points.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @return A JSON object containing the response code, an array of waypoint objects, and an array of route objects.
	 */
	public JSONObject matchPoints(String coordinates) {
		String url = String.format("http://router.project-osrm.org/match/v1/car/%s?geometries=geojson&overview=full", coordinates);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(request);
			// Using EntityUtils to convert the response entity to a string
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			logger.error("Error while matching points: {}", e.getMessage(), e);
		}

		return result;
	}

	/**
	 * Snaps the supplied coordinates to the road network, and returns the most likely route given the points.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @param profile The mode of travel. Valid values are 'car', 'bike' and 'foot'.
	 * @return A JSON object containing the response code, an array of waypoint objects, and an array of route objects.
	 */
	public JSONObject matchPoints(String coordinates, String profile) {
		String url = String.format("http://router.project-osrm.org/match/v1/%s/%s?geometries=geojson&overview=full", profile, coordinates);
		JSONObject result = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(request);
			// Using EntityUtils to convert the response entity to a string
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			logger.error("Error while matching points with profile: {}", e.getMessage(), e);
		}

		return result;
	}
}
