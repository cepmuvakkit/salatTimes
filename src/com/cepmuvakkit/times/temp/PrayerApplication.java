package com.cepmuvakkit.times.temp;

import android.app.Application;
import android.content.Context;

public class PrayerApplication extends Application
{
  private static Application instance;

  public PrayerApplication()
  {
    instance = this;
  }

  public static Context getContext()
  {
    return instance;
  }
}

