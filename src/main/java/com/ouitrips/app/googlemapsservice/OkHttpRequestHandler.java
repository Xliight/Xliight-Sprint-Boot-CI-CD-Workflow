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

import com.google.gson.FieldNamingPolicy;
import com.ouitrips.app.googlemapsservice.GeoApiContext.RequestHandler;
import com.ouitrips.app.googlemapsservice.android.AndroidAuthenticationConfig;
import com.ouitrips.app.googlemapsservice.android.AndroidAuthenticationConfigProvider;
import com.ouitrips.app.googlemapsservice.android.AndroidAuthenticationInterceptor;
import com.ouitrips.app.googlemapsservice.internal.ApiResponse;
import com.ouitrips.app.googlemapsservice.internal.ExceptionsAllowedToRetry;
import com.ouitrips.app.googlemapsservice.internal.OkHttpPendingResult;
import com.ouitrips.app.googlemapsservice.internal.RateLimitExecutorService;
import com.ouitrips.app.googlemapsservice.metrics.RequestMetrics;
import java.net.Proxy;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A strategy for handling URL requests using OkHttp.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class OkHttpRequestHandler implements GeoApiContext.RequestHandler {
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private final OkHttpClient client;
  private final ExecutorService executorService;

  /* package */ OkHttpRequestHandler(OkHttpClient client, ExecutorService executorService) {
    this.client = client;
    this.executorService = executorService;
  }

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
    Request.Builder builder = new Request.Builder().get();
    for (Entry<String, String> entry : headers.entrySet()) {
      builder = builder.header(entry.getKey(), entry.getValue());
    }
    Request req = builder.url(hostName + url).build();

    return new OkHttpPendingResult<>(
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
    RequestBody body = RequestBody.create(payload, JSON);
    Request.Builder builder = new Request.Builder().post(body);
    for (Entry<String, String> entry : headers.entrySet()) {
      builder = builder.header(entry.getKey(), entry.getValue());
    }
    Request req = builder.url(hostName + url).build();

    return new OkHttpPendingResult<>(
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
    executorService.shutdown();
    client.connectionPool().evictAll();
  }

  /** Builder strategy for constructing an {@code OkHTTPRequestHandler}. */
  public static class Builder implements GeoApiContext.RequestHandler.Builder {
    private final OkHttpClient.Builder okHttpClientBuilder; // Renamed for clarity
    private final RateLimitExecutorService rateLimitExecutorService;
    private final Dispatcher dispatcher;

    public Builder() {
      okHttpClientBuilder = new OkHttpClient.Builder(); // Update this line as well
      rateLimitExecutorService = new RateLimitExecutorService();
      dispatcher = new Dispatcher(rateLimitExecutorService);
      okHttpClientBuilder.dispatcher(dispatcher); // Update this line as well

      final AndroidAuthenticationConfigProvider provider =
              new AndroidAuthenticationConfigProvider();
      final AndroidAuthenticationConfig config = provider.provide();
      okHttpClientBuilder.addInterceptor(new AndroidAuthenticationInterceptor(config)); // Update this line as well
    }

    @Override
    public Builder connectTimeout(long timeout, TimeUnit unit) {
      okHttpClientBuilder.connectTimeout(timeout, unit);
      return this;
    }

    @Override
    public Builder readTimeout(long timeout, TimeUnit unit) {
      okHttpClientBuilder.readTimeout(timeout, unit);
      return this;
    }

    @Override
    public Builder writeTimeout(long timeout, TimeUnit unit) {
      okHttpClientBuilder.writeTimeout(timeout, unit);
      return this;
    }

    @Override
    public Builder queriesPerSecond(int maxQps) {
      dispatcher.setMaxRequests(maxQps);
      dispatcher.setMaxRequestsPerHost(maxQps);
      rateLimitExecutorService.setQueriesPerSecond(maxQps);
      return this;
    }

    @Override
    public Builder proxy(Proxy proxy) {
      okHttpClientBuilder.proxy(proxy);
      return this;
    }

    @Override
    public Builder proxyAuthentication(String proxyUserName, String proxyUserPassword) {
      final String userName = proxyUserName;
      final String password = proxyUserPassword;
      okHttpClientBuilder.proxyAuthenticator(
              (route, response) -> {
                String credential = Credentials.basic(userName, password);
                return response
                        .request()
                        .newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
              });
      return this;
    }

    /**
     * Gets a reference to the OkHttpClient.Builder used to build the OkHttpRequestHandler's
     * internal OkHttpClient. This allows you to fully customize the OkHttpClient that the resulting
     * OkHttpRequestHandler will make HTTP requests through.
     *
     * @return OkHttpClient.Builder that will produce the OkHttpClient used by the
     * OkHttpRequestHandler built by this.
     */
    public OkHttpClient.Builder okHttpClientBuilder() {
      return okHttpClientBuilder; // Update this line as well
    }

    @Override
    public RequestHandler build() {
      OkHttpClient client = okHttpClientBuilder.build(); // Update this line as well
      return new OkHttpRequestHandler(client, rateLimitExecutorService);
    }
  }
}
