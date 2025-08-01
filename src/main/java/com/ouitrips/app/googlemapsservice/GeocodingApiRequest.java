/*
 * Copyright 2016 Google Inc. All rights reserved.
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

import com.ouitrips.app.googlemapsservice.internal.ApiConfig;
import com.ouitrips.app.googlemapsservice.model.AddressType;
import com.ouitrips.app.googlemapsservice.model.ComponentFilter;
import com.ouitrips.app.googlemapsservice.model.GeocodingResponse;
import com.ouitrips.app.googlemapsservice.model.LatLng;
import com.ouitrips.app.googlemapsservice.model.LocationType;

/** A request for the Geocoding API. */
public class GeocodingApiRequest
    extends PendingResultBase<GeocodingResponse, GeocodingApiRequest, GeocodingApi.Response> {

  private static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/geocode/json");

  private static final String KEY_LAT_LNG = "latlng";
  private static final String KEY_ADDRESS = "address";
  private static final String KEY_PLACE_ID = "place_id";

  public GeocodingApiRequest(GeoApiContext context) {
    super(context, API_CONFIG, GeocodingApi.Response.class);
  }

  @Override
  protected void validateRequest() {
    // Must not have both address and latlng.
    if (params().containsKey(KEY_LAT_LNG)
        && params().containsKey(KEY_ADDRESS)
        && params().containsKey(KEY_PLACE_ID)) {
      throw new IllegalArgumentException(
          "Request must contain only one of 'address', 'latlng' or 'place_id'.");
    }


    if (!params().containsKey(KEY_LAT_LNG)
        && !params().containsKey(KEY_ADDRESS)
        && !params().containsKey("components")
        && !params().containsKey(KEY_PLACE_ID)) {
      throw new IllegalArgumentException(
          "Request must contain at least one of 'address', 'latlng', 'place_id' and 'components'.");
    }
  }

  /**
   * Creates a forward geocode for {@code address}.
   *
   * @param address The address to geocode.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest address(String address) {
    return param(KEY_ADDRESS, address);
  }

  /**
   * Creates a forward geocode for {@code placeId}.
   *
   * @param placeId The Place ID to geocode.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest place(String placeId) {
    return param(KEY_PLACE_ID, placeId);
  }

  /**
   * Creates a reverse geocode for {@code latlng}.
   *
   * @param latlng The location to reverse geocode.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest latlng(LatLng latlng) {
    return param(KEY_LAT_LNG, latlng);
  }

  /**
   * Sets the bounding box of the viewport within which to bias geocode results more prominently.
   * This parameter will only influence, not fully restrict, results from the geocoder.
   *
   * <p>For more information see <a
   * href="https://developers.google.com/maps/documentation/geocoding/intro?hl=pl#Viewports">
   * Viewport Biasing</a>.
   *
   * @param southWestBound The South West bound of the bounding box.
   * @param northEastBound The North East bound of the bounding box.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest bounds(LatLng southWestBound, LatLng northEastBound) {
    return param("bounds", join('|', southWestBound, northEastBound));
  }

  /**
   * Sets the region code, specified as a ccTLD ("top-level domain") two-character value. This
   * parameter will only influence, not fully restrict, results from the geocoder.
   *
   * <p>For more information see <a
   * href="https://developers.google.com/maps/documentation/geocoding/intro?hl=pl#RegionCodes">Region
   * Biasing</a>.
   *
   * @param region The region code to influence results.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest region(String region) {
    return param("region", region);
  }

  /**
   * Sets the component filters. Each component filter consists of a component:value pair and will
   * fully restrict the results from the geocoder.
   *
   * <p>For more information see <a
   * href="https://developers.google.com/maps/documentation/geocoding/intro?hl=pl#ComponentFiltering">
   * Component Filtering</a>.
   *
   * @param filters Component filters to apply to the request.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest components(ComponentFilter... filters) {
    return param("components", join('|', filters));
  }

  /**
   * Sets the result type. Specifying a type will restrict the results to this type. If multiple
   * types are specified, the API will return all addresses that match any of the types.
   *
   * @param resultTypes The result types to restrict to.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest resultType(AddressType... resultTypes) {
    return param("result_type", join('|', resultTypes));
  }

  /**
   * Sets the location type. Specifying a type will restrict the results to this type. If multiple
   * types are specified, the API will return all addresses that match any of the types.
   *
   * @param locationTypes The location types to restrict to.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest locationType(LocationType... locationTypes) {
    return param("location_type", join('|', locationTypes));
  }

  /**
   * Determines whether the address descriptor is returned in the response.
   *
   * @param enableAddressDescriptor Flag to determine whether to return the address descriptor in
   *     the response.
   * @return Returns this {@code GeocodingApiRequest} for call chaining.
   */
  public GeocodingApiRequest enableAddressDescriptor(boolean enableAddressDescriptor) {
    return param("enable_address_descriptor", String.valueOf(enableAddressDescriptor));
  }
}
