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

package com.ouitrips.app.googlemapsservice.model;

import java.io.Serializable;

/**
 * An Elevation API result.
 *
 * <p>Units are in meters, per https://developers.google.com/maps/documentation/elevation/start.
 */
public class ElevationResult implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Elevation in meters. */
  public double elevation;
  /** Location of the elevation data. */
  public LatLng location;
  /** Maximum distance between data points from which the elevation was interpolated, in meters. */
  public double resolution;

  @Override
  public String toString() {
    return String.format("(%s, %f m, resolution=%f m)", location, elevation, resolution);
  }
}
