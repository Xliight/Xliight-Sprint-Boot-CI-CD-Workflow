/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.ouitrips.app.googlemapsservice;


import com.ouitrips.app.googlemapsservice.errors.ApiException;
import com.ouitrips.app.googlemapsservice.internal.ApiConfig;
import com.ouitrips.app.googlemapsservice.internal.ApiResponse;
import com.ouitrips.app.googlemapsservice.internal.StringJoin.UrlValue;
import com.ouitrips.app.googlemapsservice.model.DirectionsResult;
import com.ouitrips.app.googlemapsservice.model.DirectionsRoute;
import com.ouitrips.app.googlemapsservice.model.GeocodedWaypoint;

/**
 * The Google Directions API is a service that calculates directions between locations using an HTTP
 * request. You can search for directions for several modes of transportation, include transit,
 * driving, walking, or cycling. Directions may specify origins, destinations, and waypoints, either
 * as text strings (e.g. "Chicago, IL" or "Darwin, NT, Australia") or as latitude/longitude
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/intro">the Directions
 * API Developer's Guide</a> for more information.
 */
public class DirectionsApi {
  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/directions/json");

  private DirectionsApi() {}

  /**
   * Creates a new DirectionsApiRequest using the given context, with all attributes at their
   * default values.
   *
   * @param context Context that the DirectionsApiRequest will be executed against
   * @return A newly constructed DirectionsApiRequest between the given points.
   */
  public static DirectionsApiRequest newRequest(GeoApiContext context) {
    return new DirectionsApiRequest(context);
  }

  /**
   * Creates a new DirectionsApiRequest between the given origin and destination, using the defaults
   * for all other options.
   *
   * @param context Context that the DirectionsApiRequest will be executed against
   * @param origin Origin address as text
   * @param destination Destination address as text
   * @return A newly constructed DirectionsApiRequest between the given points.
   */
  public static DirectionsApiRequest getDirections(
      GeoApiContext context, String origin, String destination) {
    return new DirectionsApiRequest(context).origin(origin).destination(destination);
  }

  public static class Response implements ApiResponse<DirectionsResult> {
    private String status;
    private String errorMessage;
    private GeocodedWaypoint[] geocodedWaypoints;
    private DirectionsRoute[] routes;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
    }

    public GeocodedWaypoint[] getGeocodedWaypoints() {
      return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(GeocodedWaypoint[] geocodedWaypoints) {
      this.geocodedWaypoints = geocodedWaypoints;
    }

    public DirectionsRoute[] getRoutes() {
      return routes;
    }

    public void setRoutes(DirectionsRoute[] routes) {
      this.routes = routes;
    }

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public DirectionsResult getResult() {
      DirectionsResult result = new DirectionsResult();
      result.geocodedWaypoints = geocodedWaypoints;
      result.routes = routes;
      return result;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }

  /**
   * Directions may be calculated that adhere to certain restrictions. This is configured by calling
   * {@link com.google.maps.DirectionsApiRequest#avoid} or {@link
   * com.google.maps.DistanceMatrixApiRequest#avoid}.
   *
   * @see <a href="https://developers.google.com/maps/documentation/directions/intro#Restrictions">
   *     Restrictions in the Directions API</a>
   * @see <a
   *     href="https://developers.google.com/maps/documentation/distance-matrix/intro#RequestParameters">
   *     Distance Matrix API Request Parameters</a>
   */
  public enum RouteRestriction implements UrlValue {

    /** Indicates that the calculated route should avoid toll roads/bridges. */
    TOLLS("tolls"),

    /** Indicates that the calculated route should avoid highways. */
    HIGHWAYS("highways"),

    /** Indicates that the calculated route should avoid ferries. */
    FERRIES("ferries"),

    /** Indicates that the calculated route should avoid indoor areas. */
    INDOOR("indoor");

    private final String restriction;

    RouteRestriction(String restriction) {
      this.restriction = restriction;
    }

    @Override
    public String toString() {
      return restriction;
    }

    @Override
    public String toUrlValue() {
      return restriction;
    }
  }
}
