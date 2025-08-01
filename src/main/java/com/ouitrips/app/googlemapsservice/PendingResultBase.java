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
import com.ouitrips.app.googlemapsservice.internal.HttpHeaders;
import com.ouitrips.app.googlemapsservice.internal.StringJoin;
import com.ouitrips.app.googlemapsservice.internal.StringJoin.UrlValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation for {@code PendingResult}.
 *
 * <p>{@code T} is the class of the result, {@code A} is the actual base class of this abstract
 * class, and R is the type of the request.
 */
abstract class PendingResultBase<T, A extends PendingResultBase<T, A, R>, R extends ApiResponse<T>>
    implements PendingResult<T> {
  private PendingResult<T> delegate;

  private final GeoApiContext context;
  private final ApiConfig config;
  private HashMap<String, List<String>> params = new HashMap<>();
  private Map<String, String> headers = new HashMap<>();
  private Class<? extends R> responseClass;

  protected PendingResultBase(GeoApiContext context, ApiConfig config, Class<? extends R> clazz) {
    this.context = context;
    this.config = config;
    this.responseClass = clazz;
  }

  @Override
  public final void setCallback(Callback<T> callback) {
    makeRequest().setCallback(callback);
  }

  @Override
  public final T await() throws ApiException, InterruptedException, IOException {
    PendingResult<T> request = makeRequest();
    return request.await();
  }

  @Override
  public final T awaitIgnoreError() {
    return makeRequest().awaitIgnoreError();
  }

  @Override
  public final void cancel() {
    if (delegate == null) {
      return;
    }
    delegate.cancel();
  }

    private PendingResult<T> makeRequest() {
        if (delegate != null) {
            throw new IllegalStateException(
                    "'await', 'awaitIgnoreError' or 'setCallback' was already called.");
        }
        validateRequest();
        PendingResult<T> result; // Declare the result variable
        switch (config.requestVerb) {
            case "GET":
                result = context.get(config, responseClass, headers, params);
                break;
            case "POST":
                result = context.post(config, responseClass, headers, params);
                break;
            default:
                throw new IllegalStateException(
                        String.format("Unexpected request method '%s'", config.requestVerb));
        }
        delegate = result;
        return delegate;
    }

  protected abstract void validateRequest();

  private A getInstance() {
    @SuppressWarnings("unchecked")
    A result = (A) this;
    return result;
  }

  /**
   * Sets the header named {@code key} to {@code value}. If this request already has any headers
   * with the same key, the value is replaced.
   *
   * @param key the header key
   * @param value the header value
   * @return this request
   */
  public A header(String key, String value) {
    headers.put(key, value);
    return getInstance();
  }

  /**
   * Sets the value for the HTTP header field name {@link HttpHeaders#X_GOOG_MAPS_EXPERIENCE_ID}.
   * Passing null to this method will unset the experienceId header field.
   *
   * @param experienceIds The experience IDs
   */
  public A experienceIds(String... experienceIds) {
    if (experienceIds == null || experienceIds.length == 0) {
      headers.remove(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID);
      return getInstance();
    }
    header(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID, StringJoin.join(",", experienceIds));
    return getInstance();
  }

  protected A param(String key, String val) {
    // Enforce singleton parameter semantics for most API surfaces
      params.put(key, new ArrayList<>());
      return paramAddToList(key, val);
  }

  protected A param(String key, int val) {
    return this.param(key, Integer.toString(val));
  }

  protected A param(String key, UrlValue val) {
    if (val != null) {
      return this.param(key, val.toUrlValue());
    }
    return getInstance();
  }

  protected A paramAddToList(String key, String val) {
    // Multiple parameter values required to support Static Maps API paths and markers.
      List<String> valueList = params.computeIfAbsent(key, k -> new ArrayList<>());
      valueList.add(val);
      return getInstance();
  }

  protected A paramAddToList(String key, UrlValue val) {
    if (val != null) {
      return this.paramAddToList(key, val.toUrlValue());
    }
    return getInstance();
  }

  protected Map<String, List<String>> params() {
    return Collections.unmodifiableMap(params);
  }

  /**
   * The language in which to return results. Note that we often update supported languages so this
   * list may not be exhaustive.
   *
   * @param language The language code, e.g. "en-AU" or "es".
   * @see <a href="https://developers.google.com/maps/faq#languagesupport">List of supported domain
   *     languages</a>
   * @return Returns the request for call chaining.
   */
  public final A language(String language) {
    return param("language", language);
  }

  /**
   * A channel to pass with the request. channel is used by Google Maps API for Work users to be
   * able to track usage across different applications with the same clientID. See <a
   * href="https://developers.google.com/maps/documentation/business/clientside/quota">Premium Plan
   * Usage Rates and Limits</a>.
   *
   * @param channel String to pass with the request for analytics.
   * @return Returns the request for call chaining.
   */
  public A channel(String channel) {
    return param("channel", channel);
  }

  /**
   * Custom parameter. For advanced usage only.
   *
   * @param parameter The name of the custom parameter.
   * @param value The value of the custom parameter.
   * @return Returns the request for call chaining.
   */
  public A custom(String parameter, String value) {
    return param(parameter, value);
  }
}
