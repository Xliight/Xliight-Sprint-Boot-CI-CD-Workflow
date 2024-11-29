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

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.FieldNamingPolicy;
import com.ouitrips.app.googlemapsservice.internal.ApiResponse;
import com.ouitrips.app.googlemapsservice.internal.ExceptionsAllowedToRetry;
import com.ouitrips.app.googlemapsservice.internal.GaePendingResult;
import com.ouitrips.app.googlemapsservice.metrics.RequestMetrics;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A strategy for handling URL requests using Google App Engine's URL Fetch API.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class GaeRequestHandler implements GeoApiContext.RequestHandler {
  private final URLFetchService client = URLFetchServiceFactory.getURLFetchService();

  /* package */ GaeRequestHandler() {}

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(
      String hostName,
      String url,
      Map<String, String> headers,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      RequestMetrics metrics) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    final HTTPRequest req;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
      headers.forEach((k, v) -> req.addHeader(new HTTPHeader(k, v)));
    } catch (MalformedURLException e) {
      throw new UnsupportedOperationException(e);
    }


    return new GaePendingResult<>(
        req,
        client,
        clazz,
        fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        metrics);
  }

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handlePost(
      String hostName,
      String url,
      String payload,
      Map<String, String> headers,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      RequestMetrics metrics) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    final HTTPRequest req;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
      req.setHeader(new HTTPHeader("Content-Type", "application/json; charset=utf-8"));
      headers.forEach((k, v) -> req.addHeader(new HTTPHeader(k, v)));
      req.setPayload(payload.getBytes(UTF_8));
    } catch (MalformedURLException e) {
      throw new UnsupportedOperationException(e);
    }


    return new GaePendingResult<>(
        req,
        client,
        clazz,
        fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        metrics);
  }

  @Override
  public void shutdown() {
    // do nothing
  }

  /** Builder strategy for constructing {@code GaeRequestHandler}. */
  public static class Builder implements GeoApiContext.RequestHandler.Builder {

    @Override
    public Builder connectTimeout(long timeout, TimeUnit unit) {
      throw new UnsupportedOperationException("connectTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder readTimeout(long timeout, TimeUnit unit) {
      throw new UnsupportedOperationException("readTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder writeTimeout(long timeout, TimeUnit unit) {
      throw new UnsupportedOperationException("writeTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder queriesPerSecond(int maxQps) {
      throw new UnsupportedOperationException("queriesPerSecond not implemented for Google App Engine");
    }

    @Override
    public Builder proxy(Proxy proxy) {
      throw new UnsupportedOperationException("setProxy not implemented for Google App Engine");
    }

    @Override
    public Builder proxyAuthentication(String proxyUserName, String proxyUserPassword) {
      throw new UnsupportedOperationException("setProxyAuthentication not implemented for Google App Engine");
    }

    @Override
    public GeoApiContext.RequestHandler build() {
      return new GaeRequestHandler();
    }
  }
}
