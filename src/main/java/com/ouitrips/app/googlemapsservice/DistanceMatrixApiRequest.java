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

import static com.google.maps.internal.StringJoin.join;

import com.ouitrips.app.googlemapsservice.DirectionsApi.RouteRestriction;
import com.ouitrips.app.googlemapsservice.DistanceMatrixApi.Response;
import com.ouitrips.app.googlemapsservice.model.DistanceMatrix;
import com.ouitrips.app.googlemapsservice.model.LatLng;
import com.ouitrips.app.googlemapsservice.model.TrafficModel;
import com.ouitrips.app.googlemapsservice.model.TransitMode;
import com.ouitrips.app.googlemapsservice.model.TransitRoutingPreference;
import com.ouitrips.app.googlemapsservice.model.TravelMode;
import com.ouitrips.app.googlemapsservice.model.Unit;
import java.time.Instant;

/** A request to the Distance Matrix API. */
public class DistanceMatrixApiRequest
    extends PendingResultBase<DistanceMatrix, DistanceMatrixApiRequest, Response> {

  private static final String ORIGINS_PARAM = "origins";
  private static final String DESTINATIONS_PARAM = "destinations";
  private static final String ARRIVAL_TIME_PARAM = "arrival_time";
  private static final String DEPARTURE_TIME_PARAM = "departure_time";
  private static final String TRAFFIC_MODEL_PARAM = "traffic_model";

  public DistanceMatrixApiRequest(GeoApiContext context) {
    super(context, DistanceMatrixApi.API_CONFIG, Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey(ORIGINS_PARAM)) {
      throw new IllegalArgumentException("Request must contain 'origins'");
    }
    if (!params().containsKey(DESTINATIONS_PARAM)) {
      throw new IllegalArgumentException("Request must contain 'destinations'");
    }
    if (params().containsKey(ARRIVAL_TIME_PARAM) && params().containsKey(DEPARTURE_TIME_PARAM)) {
      throw new IllegalArgumentException(
              "Transit request must not contain both a departureTime and an arrivalTime");
    }
  }

  /**
   * One or more addresses from which to calculate distance and time. The service will geocode the
   * strings and convert them to latitude/longitude coordinates to calculate directions.
   *
   * @param origins Strings to geocode and use as an origin point (e.g. "New York, NY")
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest origins(String... origins) {
    return param(ORIGINS_PARAM, join('|', origins));
  }

  /**
   * One or more latitude/longitude values from which to calculate distance and time.
   *
   * @param points The origin points.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest origins(LatLng... points) {
    return param(ORIGINS_PARAM, join('|', points));
  }

  /**
   * One or more addresses to which to calculate distance and time. The service will geocode the
   * strings and convert them to latitude/longitude coordinates to calculate directions.
   *
   * @param destinations Strings to geocode and use as a destination point (e.g. "Jersey City, NJ")
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest destinations(String... destinations) {
    return param(DESTINATIONS_PARAM, join('|', destinations));
  }


  /**
   * One or more latitude/longitude values to which to calculate distance and time.
   *
   * @param points The destination points.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest destinations(LatLng... points) {
    return param(DESTINATIONS_PARAM, join('|', points));
  }

  /**
   * Specifies the mode of transport to use when calculating directions.
   *
   * <p>Note that Distance Matrix requests only support {@link TravelMode#DRIVING}, {@link
   * TravelMode#WALKING}, {@link TravelMode#BICYCLING} and {@link TravelMode#TRANSIT}.
   *
   * @param mode One of the travel modes supported by the Distance Matrix API.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest mode(TravelMode mode) {
    if (TravelMode.DRIVING.equals(mode)
        || TravelMode.WALKING.equals(mode)
        || TravelMode.BICYCLING.equals(mode)
        || TravelMode.TRANSIT.equals(mode)) {
      return param("mode", mode);
    }
    throw new IllegalArgumentException(
        "Distance Matrix API travel modes must be Driving, Transit, Walking or Bicycling");
  }

  /**
   * Introduces restrictions to the route. Only one restriction can be specified.
   *
   * @param restriction A {@link RouteRestriction} object.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest avoid(RouteRestriction restriction) {
    return param("avoid", restriction);
  }

  /**
   * Specifies the unit system to use when expressing distance as text. Distance Matrix results
   * contain text within distance fields to indicate the distance of the calculated route.
   *
   * @param unit One of {@link Unit#METRIC} or {@link Unit#IMPERIAL}.
   * @see <a
   *     href="https://developers.google.com/maps/documentation/distance-matrix/intro#unit_systems">
   *     Unit systems in the Distance Matrix API</a>
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest units(Unit unit) {
    return param("units", unit);
  }

  /**
   * Specifies the desired time of departure.
   *
   * <p>The departure time may be specified in two cases:
   *
   * <ul>
   *   <li>For requests where the travel mode is transit: You can optionally specify one of
   *       departure_time or arrival_time. If neither time is specified, the departure_time defaults
   *       to now. (That is, the departure time defaults to the current time.)
   *   <li>For requests where the travel mode is driving: Google Maps API for Work customers can
   *       specify the departure_time to receive trip duration considering current traffic
   *       conditions. The departure_time must be set to within a few minutes of the current time.
   * </ul>
   *
   * <p>Setting the parameter to null will remove it from the API request.
   *
   * @param departureTime The time of departure.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest departureTime(Instant departureTime) {
    return param(DEPARTURE_TIME_PARAM, Long.toString(departureTime.toEpochMilli() / 1000L));
  }

  /**
   * Specifies the assumptions to use when calculating time in traffic. This parameter may only be
   * specified when the travel mode is driving and the request includes a departure_time.
   *
   * @param trafficModel The traffic model to use in estimating time in traffic.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest trafficModel(TrafficModel trafficModel) {
    return param(TRAFFIC_MODEL_PARAM, trafficModel);
  }

  /**
   * Specifies the desired time of arrival for transit requests. You can specify either
   * departure_time or arrival_time, but not both.
   *
   * @param arrivalTime The preferred arrival time.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest arrivalTime(Instant arrivalTime) {
    return param(ARRIVAL_TIME_PARAM, Long.toString(arrivalTime.toEpochMilli() / 1000L));
  }

  /**
   * Specifies one or more preferred modes of transit. This parameter may only be specified for
   * requests where the mode is transit.
   *
   * @param transitModes The preferred transit modes.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest transitModes(TransitMode... transitModes) {
    return param("transit_mode", join('|', transitModes));
  }

  /**
   * Specifies preferences for transit requests. Using this parameter, you can bias the options
   * returned, rather than accepting the default best route chosen by the API.
   *
   * @param pref The transit routing preference for this distance matrix.
   * @return Returns this {@code DistanceMatrixApiRequest} for call chaining.
   */
  public DistanceMatrixApiRequest transitRoutingPreference(TransitRoutingPreference pref) {
    return param("transit_routing_preference", pref);
  }
}
