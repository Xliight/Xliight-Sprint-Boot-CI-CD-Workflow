/*
 * Copyright 2015 Google Inc. All rights reserved.
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

import com.ouitrips.app.googlemapsservice.errors.ApiError;
import com.ouitrips.app.googlemapsservice.errors.ApiException;
import com.ouitrips.app.googlemapsservice.internal.ApiResponse;
import com.ouitrips.app.googlemapsservice.model.LatLng;
import com.ouitrips.app.googlemapsservice.model.SnappedPoint;
import com.ouitrips.app.googlemapsservice.model.SnappedSpeedLimitResult;
import com.ouitrips.app.googlemapsservice.model.SpeedLimit;

/**
 * The Google Maps Roads API identifies the roads a vehicle was traveling along and provides
 * additional metadata about those roads, such as speed limits.
 *
 * <p>See also: <a href="https://developers.google.com/maps/documentation/roads">Roads API
 * documentation</a>.
 */
public class RoadsApi {
  static final String API_BASE_URL = "https://roads.googleapis.com";

  private RoadsApi() {}

  /**
   * Takes up to 100 GPS points collected along a route, and returns a similar set of data with the
   * points snapped to the most likely roads the vehicle was traveling along.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param path The collected GPS points as a path.
   * @return Returns the {@code SnapToRoadsApiRequest} for call chaining
   */
  public static SnapToRoadsApiRequest snapToRoads(GeoApiContext context, LatLng... path) {
    return snapToRoads(context, false, path);
  }

  /**
   * Takes up to 100 GPS points collected along a route, and returns a similar set of data with the
   * points snapped to the most likely roads the vehicle was traveling along. Additionally, you can
   * request that the points be interpolated, resulting in a path that smoothly follows the geometry
   * of the road.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param interpolate Whether to interpolate a path to include all points forming the full
   *     road-geometry. When true, additional interpolated points will also be returned, resulting
   *     in a path that smoothly follows the geometry of the road, even around corners and through
   *     tunnels.
   * @param path The path to be snapped.
   * @return Returns the {@code SnapToRoadsApiRequest} for call chaining
   */
  public static SnapToRoadsApiRequest snapToRoads(
      GeoApiContext context, boolean interpolate, LatLng... path) {
    return new SnapToRoadsApiRequest(context).path(path).interpolate(interpolate);
  }

  /**
   * Returns the posted speed limit for given road segments. The provided LatLngs will first be
   * snapped to the most likely roads the vehicle was traveling along.
   *
   * <p>Note: The accuracy of speed limit data returned by the Google Maps Roads API cannot be
   * guaranteed. Speed limit data provided is not real-time, and may be estimated, inaccurate,
   * incomplete, and/or outdated. Inaccuracies in our data may be reported through <a
   * href="https://www.localguidesconnect.com/t5/News-Updates/Exclusive-Edit-a-road-segment-in-Google-Maps/ba-p/149865">
   * Google Maps Feedback</a>.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param path The collected GPS points as a path.
   * @return a {@link SpeedLimitsApiRequest}
   */
  public static SpeedLimitsApiRequest speedLimits(GeoApiContext context, LatLng... path) {
    return new SpeedLimitsApiRequest(context).path(path);
  }

  /**
   * Returns the posted speed limit for given road segments.
   *
   * <p>Note: The accuracy of speed limit data returned by the Google Maps Roads API cannot be
   * guaranteed. Speed limit data provided is not real-time, and may be estimated, inaccurate,
   * incomplete, and/or outdated. Inaccuracies in our data may be reported through <a
   * href="https://www.localguidesconnect.com/t5/News-Updates/Exclusive-Edit-a-road-segment-in-Google-Maps/ba-p/149865">
   * Google Maps Feedback</a>.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param placeIds The Place ID of the road segment. Place IDs are returned by the {@link
   *     #snapToRoads(GeoApiContext, com.google.maps.model.LatLng...)} method. You can pass up to
   *     100 placeIds with each request.
   * @return a {@link SpeedLimitsApiRequest}
   */
  public static SpeedLimitsApiRequest speedLimits(GeoApiContext context, String... placeIds) {
    return new SpeedLimitsApiRequest(context).placeIds(placeIds);
  }

  /**
   * Takes up to 100 GPS points, and returns the closest road segment for each point. The points
   * passed do not need to be part of a continuous path.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param points The sequence of points to be aligned to nearest roads
   * @return a {@link NearestRoadsApiRequest}
   */
  public static NearestRoadsApiRequest nearestRoads(GeoApiContext context, LatLng... points) {
    return new NearestRoadsApiRequest(context).points(points);
  }

  public static class RoadsResponse implements ApiResponse<SnappedPoint[]> {
    private SnappedPoint[] snappedPoints;
    private ApiError error;

    @Override
    public boolean successful() {
      return error == null;
    }

    @Override
    public SnappedPoint[] getResult() {
      return snappedPoints;
    }

    @Override
    public ApiException getError() {
      return ApiException.from(error.status, error.message);
    }
  }

  public static class SpeedLimitsResponse implements ApiResponse<SnappedSpeedLimitResult> {
    private SnappedPoint[] snappedPoints;
    private SpeedLimit[] speedLimits;
    private ApiError error;

    @Override
    public boolean successful() {
      return error == null;
    }

    @Override
    public SnappedSpeedLimitResult getResult() {
      SnappedSpeedLimitResult response = new SnappedSpeedLimitResult();
      response.snappedPoints = snappedPoints;
      response.speedLimits = speedLimits;
      return response;
    }

    @Override
    public ApiException getError() {
      return ApiException.from(error.status, error.message);
    }
  }
}
