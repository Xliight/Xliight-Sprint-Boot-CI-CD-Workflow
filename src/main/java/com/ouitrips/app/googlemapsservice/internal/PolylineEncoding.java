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

package com.ouitrips.app.googlemapsservice.internal;

import com.ouitrips.app.googlemapsservice.model.LatLng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class that encodes and decodes Polylines.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">
 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm</a> for detailed
 * description of this format.
 */
public class PolylineEncoding {
  /** Decodes an encoded path string into a sequence of LatLngs. */
  public static List<LatLng> decode(final String encodedPath) {

    int len = encodedPath.length();

    final List<LatLng> path = new ArrayList<>(len / 2);
    int index = 0;
    int lat = 0;
    int lng = 0;

    while (index < len) {
      int result = 1;
      int shift = 0;
      int b;
      do {
        b = encodedPath.charAt(index++) - 63 - 1;
        result += b << shift;
        shift += 5;
      } while (b >= 0x1f);
      lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      result = 1;
      shift = 0;
      do {
        b = encodedPath.charAt(index++) - 63 - 1;
        result += b << shift;
        shift += 5;
      } while (b >= 0x1f);
      lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      path.add(new LatLng(lat * 1e-5, lng * 1e-5));
    }

    return path;
  }

  /** Encodes a sequence of LatLngs into an encoded path string. */
  public static String encode(final List<LatLng> path) {
    long lastLat = 0;
    long lastLng = 0;

    final StringBuilder result = new StringBuilder();

    for (final LatLng point : path) {
      long lat = Math.round(point.lat * 1e5);
      long lng = Math.round(point.lng * 1e5);

      long dLat = lat - lastLat;
      long dLng = lng - lastLng;

      encode(dLat, result);
      encode(dLng, result);

      lastLat = lat;
      lastLng = lng;
    }
    return result.toString();
  }

  private static void encode(long v, StringBuilder result) {
    v = v < 0 ? ~(v << 1) : v << 1;
    while (v >= 0x20) {
      result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
      v >>= 5;
    }
    result.append(Character.toChars((int) (v + 63)));
  }

  /** Encodes an array of LatLngs into an encoded path string. */
  public static String encode(LatLng[] path) {
    return encode(Arrays.asList(path));
  }
}
