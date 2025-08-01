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

package com.ouitrips.app.googlemapsservice.model;

import java.io.Serializable;

/**
 * The vehicle used on a line.
 *
 * <p>See <a
 * href="https://developers.google.com/maps/documentation/directions/intro#TransitDetails">Transit
 * details</a> for more detail.
 */
public class Vehicle implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The name of the vehicle on this line. E.g. {@code "Subway"}. */
  public String name;

  /**
   * The type of vehicle that runs on this line. See the {@link com.google.maps.model.VehicleType
   * VehicleType} documentation for a complete list of supported values.
   */
  public VehicleType type;

  /** The URL for an icon associated with this vehicle type. */
  public String icon;

  /** The URL for an icon based on the local transport signage. */
  public String localIcon;

  @Override
  public String toString() {
    return String.format("%s (%s)", name, type);
  }
}
