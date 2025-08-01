/*
 * Copyright 2018 Google Inc. All rights reserved.
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

import com.ouitrips.app.googlemapsservice.model.Size;

public class StaticMapsApi {

  private StaticMapsApi() {}

  /**
   * Create a new {@code StaticMapRequest}.
   *
   * @param context The {@code GeoApiContext} to make this request through.
   * @param size The size of the static map.
   * @return Returns a new {@code StaticMapRequest} with configured size.
   */
  public static StaticMapsRequest newRequest(GeoApiContext context, Size size) {
    return new StaticMapsRequest(context).size(size);
  }
}
