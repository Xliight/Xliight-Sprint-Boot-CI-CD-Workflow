package com.ouitrips.app.googlemapsservice.android;

import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Wrapper for the Android class android.content.pm.PackageInfo */
public class PackageInfo {
  private Class<?> piClass;
  private Object piInstance;
  private static final Logger LOGGER = LoggerFactory.getLogger(PackageInfo.class);


  public PackageInfo(Class<?> piClass, Object piInstance) {
    this.piClass = piClass;
    this.piInstance = piInstance;
  }

  /** @return the signing signature for the app */
  @Nullable
  public Object signingSignature() {
    try {
      Field signaturesField = piClass.getField("signatures");
      Object[] signatures = (Object[]) signaturesField.get(piInstance);
      if (signatures == null || signatures.length == 0 || signatures[0] == null) {
        return null;
      }
      return signatures[0];
    } catch (NoSuchFieldException | IllegalAccessException e) {
      LOGGER.error("Error while retrieving signing signature", e);
    }
    return null;
  }
}
