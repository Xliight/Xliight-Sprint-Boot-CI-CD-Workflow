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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Calculates the travel time matrix of the fastest route between all supplied coordinates.
 * No geographic data is returned with this service.
 * @author An Tran
 * @see //http://project-osrm.org/docs/v5.7.0/api/#table-service
 * 
 */
@Service
public class Table {

	private static final Logger logger = LoggerFactory.getLogger(Table.class);
	@Value("${osrm.base_url}")
	private String osrmBaseUrl;
	public Table(){}

	/**
	 * Finds the fastest route between coordinates in the supplied order.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @return A JSON object containing the response code, a 2d array of durations, and arrays of waypoint objects representing sources, and an destinations.
	 */
	public JSONObject generateTravelTimeMatrix(String coordinates) {
		String url = String.format("%s/table/v1/driving/%s", osrmBaseUrl, coordinates);
		JSONObject result = null;

		// Using try-with-resources for resource management
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");

			// Execute the request and process the response
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			logger.error("Error finding table route: {}", e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Finds the fastest route between coordinates in the supplied order
	 * accounting for mode of travel.
	 * @param coordinates The string containing comma separated lon/lat. Multiple coordinate pairs are separated by a semicolon.
	 * @param profile The mode of travel. Valid values are 'car', 'bike' and 'foot'.
	 * @return A JSON object containing the response code, a 2d array of durations, and arrays of waypoint objects representing sources, and an destinations.
	 */
	public JSONObject generateTravelTimeMatrix(String coordinates, String profile, String annotations) {
		String url = String.format("%s/table/v1/%s/%s?annotations=%s", osrmBaseUrl, profile, coordinates, annotations);

		JSONObject result = null;

		// Using try-with-resources for resource management
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("accept", "application/json");

			// Execute the request and process the response
			HttpResponse response = httpClient.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			logger.error("Error finding table route: {}", e.getMessage(), e);
		}
		return result;
	}

}
