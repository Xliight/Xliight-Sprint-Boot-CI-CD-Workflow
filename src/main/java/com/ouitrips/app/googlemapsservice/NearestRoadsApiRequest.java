package com.ouitrips.app.googlemapsservice;

import com.google.gson.FieldNamingPolicy;
import com.ouitrips.app.googlemapsservice.RoadsApi.RoadsResponse;
import com.ouitrips.app.googlemapsservice.internal.ApiConfig;
import com.ouitrips.app.googlemapsservice.internal.StringJoin;
import com.ouitrips.app.googlemapsservice.model.LatLng;
import com.ouitrips.app.googlemapsservice.model.SnappedPoint;

/** A request to the snap to roads API (part of Roads API). */
public class NearestRoadsApiRequest
    extends PendingResultBase<SnappedPoint[], NearestRoadsApiRequest, RoadsResponse> {

  private static final ApiConfig NEAREST_ROADS_API_CONFIG =
      new ApiConfig("/v1/nearestRoads")
          .hostName(RoadsApi.API_BASE_URL)
          .supportsClientId(false)
          .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  public NearestRoadsApiRequest(GeoApiContext context) {
    super(context, NEAREST_ROADS_API_CONFIG, RoadsResponse.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("points")) {
      throw new IllegalArgumentException("Request must contain 'path");
    }
  }

  /**
   * The points from which to snap to roads.
   *
   * @param points the point to be snapped
   * @return returns this {@link NearestRoadsApiRequest} for call chaining.
   */
  public NearestRoadsApiRequest points(LatLng... points) {
    return param("points", StringJoin.join('|', points));
  }
}
