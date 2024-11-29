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

import com.ouitrips.app.googlemapsservice.errors.ApiException;
import com.ouitrips.app.googlemapsservice.internal.ApiConfig;
import com.ouitrips.app.googlemapsservice.internal.ApiResponse;
import com.ouitrips.app.googlemapsservice.internal.PolylineEncoding;
import com.ouitrips.app.googlemapsservice.model.ElevationResult;
import com.ouitrips.app.googlemapsservice.model.EncodedPolyline;
import com.ouitrips.app.googlemapsservice.model.LatLng;

/**
 * The Google Elevation API provides a simple interface to query locations on the earth for
 * elevation data. Additionally, you may request sampled elevation data along paths, allowing you to
 * calculate elevation changes along routes.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/elevation/start">the Google Maps
 * Elevation API documentation</a>.
 */
public class ElevationApi {
  private static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/elevation/json");

  private static final String LOCATIONS_PARAM = "locations";
  private static final String PATH_PARAM = "path";
  private static final String SAMPLES_PARAM = "samples";
  private static final String ENCODED_PREFIX = "enc:";

  private ElevationApi() {}

  /**
   * Gets a list of elevations for a list of points.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param points The points to retrieve elevations for.
   * @return The elevations as a {@link PendingResult}.
   */
  public static PendingResult<ElevationResult[]> getByPoints(
      GeoApiContext context, LatLng... points) {
    return context.get(API_CONFIG, MultiResponse.class, LOCATIONS_PARAM, shortestParam(points));
  }

  /**
   * See <a href="https://developers.google.com/maps/documentation/elevation/intro#Paths">
   * documentation</a>.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param samples The number of samples to retrieve heights along {@code path}.
   * @param path The path to sample.
   * @return The elevations as a {@link PendingResult}.
   */
  public static PendingResult<ElevationResult[]> getByPath(
      GeoApiContext context, int samples, LatLng... path) {
    return context.get(
        API_CONFIG,
        MultiResponse.class,
            SAMPLES_PARAM,
        String.valueOf(samples),
            PATH_PARAM,
        shortestParam(path));
  }

  /**
   * See <a href="https://developers.google.com/maps/documentation/elevation/intro#Paths">
   * documentation</a>.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param samples The number of samples to retrieve heights along {@code encodedPolyline}.
   * @param encodedPolyline The path to sample as an encoded polyline.
   * @return The elevations as a {@link PendingResult}.
   */
  public static PendingResult<ElevationResult[]> getByPath(
      GeoApiContext context, int samples, EncodedPolyline encodedPolyline) {
    return context.get(
        API_CONFIG,
        MultiResponse.class,
            SAMPLES_PARAM,
        String.valueOf(samples),
            PATH_PARAM,
            ENCODED_PREFIX + encodedPolyline.getEncodedPath());
  }

  /**
   * Chooses the shortest param (only a guess, since the length is different after URL encoding).
   */
  private static String shortestParam(LatLng[] points) {
    String joined = join('|', points);
    String encoded = ENCODED_PREFIX + PolylineEncoding.encode(points);
    return joined.length() < encoded.length() ? joined : encoded;
  }

  /**
   * Retrieves the elevation of a single location.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param location The location to retrieve the elevation for.
   * @return The elevation as a {@link PendingResult}.
   */
  public static PendingResult<ElevationResult> getByPoint(GeoApiContext context, LatLng location) {
    return context.get(API_CONFIG, SingularResponse.class, LOCATIONS_PARAM, location.toString());
  }

  private static class SingularResponse implements ApiResponse<ElevationResult> {
    private String status;
    private String errorMessage;
    private ElevationResult[] results;

    public String getStatus() {
      return status;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    private ElevationResult getFirstResult() {
      return (results != null && results.length > 0) ? results[0] : null;
    }

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public ElevationResult getResult() {
      return getFirstResult();
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
   * Retrieves the elevations of an encoded polyline path.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param encodedPolyline The encoded polyline to retrieve elevations for.
   * @return The elevations as a {@link PendingResult}.
   */
  public static PendingResult<ElevationResult[]> getByPoints(
      GeoApiContext context, EncodedPolyline encodedPolyline) {
    return context.get(
        API_CONFIG, MultiResponse.class, LOCATIONS_PARAM, "enc:" + encodedPolyline.getEncodedPath());
  }

  private static class MultiResponse implements ApiResponse<ElevationResult[]> {
    private String status;
    private String errorMessage;
    private ElevationResult[] results;

    public String getStatus() {
      return status;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    public ElevationResult[] getResults() {
      return results;
    }

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public ElevationResult[] getResult() {
      return getResults(); // Reuse the getResults method instead of duplicating logic
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }
}
