package com.ouitrips.app.googlemapsservice.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Wrapper for the Android class android.content.pm.PackageManager. */
public class PackageManager {
  private Class<?> pmClass;
  private Object pmInstance;
  private static final Logger LOGGER = LoggerFactory.getLogger(PackageManager.class);


  public PackageManager(@NotNull Class<?> pmClass, @NotNull Object pm) {
    this.pmClass = pmClass;
    this.pmInstance = pm;
  }

  /**
   * Similar to <a
   * href="https://developer.android.com/reference/android/content/pm/PackageManager#getPackageInfo(java.lang.String,%20int)">PackageInfo#getPackageInfo</a>
   * but performed through reflection.
   *
   * @param packageName the package name
   * @param flags additional flags
   * @return the PackageInfo for the requested package name if found, otherwise, null
   */
  @Nullable
  public PackageInfo getPackageInfo(String packageName, int flags) {
    try {
      final Class<?> piClass = Class.forName("android.content.pm.PackageInfo");
      final Method method = pmClass.getMethod("getPackageInfo", String.class, int.class);
      return new PackageInfo(piClass, method.invoke(pmInstance, packageName, flags));
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | ClassNotFoundException e) {
      LOGGER.error("Error while retrieving PackageInfo", e);
    }
    return null;
  }
}
