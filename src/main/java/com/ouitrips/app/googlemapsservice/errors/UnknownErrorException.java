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

package com.ouitrips.app.googlemapsservice.errors;

/**
 * Indicates that the server encountered an unknown error. In some cases these are safe to retry.
 */
public class UnknownErrorException extends ApiException {

  private static final long serialVersionUID = -4588344280364816431L;

  public UnknownErrorException(String errorMessage) {
    super(errorMessage);
  }
}
