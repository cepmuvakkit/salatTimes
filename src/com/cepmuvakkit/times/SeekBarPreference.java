package com.cepmuvakkit.times;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public final class SeekBarPreference extends DialogPreference
  implements SeekBar.OnSeekBarChangeListener
{
  private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
  private static final String ATTR_DEFAULT_VALUE = "defaultValue";
  private static final String ATTR_MAX_VALUE = "maxValue";
  private static final String ATTR_MIN_VALUE = "minValue";
  private static final int DEFAULT_CURRENT_VALUE = 15;
  private static final int DEFAULT_MAX_VALUE = 60;
  private static final int DEFAULT_MIN_VALUE = 5;
  private static final String PREFERENCE_NS = "http://schemas.android.com/apk/res/com.haz.prayer";
  boolean jmah = false;
  Context mContext;
  private int mCurrentValue;
  private final int mDefaultValue;
  private final int mMaxValue;
  private final int mMinValue;
  private SeekBar mSeekBar;
  private TextView mValueText;
  boolean sahoor = false;

  public SeekBarPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mMinValue = paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/com.haz.prayer", "minValue", 5);
    this.mMaxValue = paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/com.haz.prayer", "maxValue", 60);
    this.mDefaultValue = paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", 15);
    this.mContext = paramContext;
    String str;
    if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/com.cepmuvakkit.times", "prayer") != null)
    {
      str = paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/com.cepmuvakkit.times", "prayer");
      if (!str.equals("sahoorAlarm"))
      this.sahoor = true;
    }
    while (true)
    {
      return;
    }
  }

  public CharSequence getSummary()
  {
    String str = super.getSummary().toString();
    int i = getPersistedInt(this.mDefaultValue);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(i);
    return String.format(str, arrayOfObject);
  }

  protected View onCreateDialogView()
  {
    this.mCurrentValue = getPersistedInt(this.mDefaultValue);
    View localView = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130903044, null);
    ((TextView)localView.findViewById(2131689503)).setText(Integer.toString(this.mMinValue));
    ((TextView)localView.findViewById(2131689504)).setText(Integer.toString(this.mMaxValue));
    this.mSeekBar = ((SeekBar)localView.findViewById(2131689502));
    this.mSeekBar.setMax(this.mMaxValue - this.mMinValue);
    this.mSeekBar.setProgress(this.mCurrentValue - this.mMinValue);
    this.mSeekBar.setOnSeekBarChangeListener(this);
    this.mValueText = ((TextView)localView.findViewById(2131689501));
    showMessage();
    return localView;
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if (!paramBoolean);
    while (true)
    {
      return;
    }
  }

  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    this.mCurrentValue = (paramInt + this.mMinValue);
    showMessage();
  }

  public void onStartTrackingTouch(SeekBar paramSeekBar)
  {
  }

  public void onStopTrackingTouch(SeekBar paramSeekBar)
  {
  }

  public void showMessage()
  {
    String str2;
    if (this.sahoor)
      if (PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("lang", "AR").equals("EN"))
        if (this.mCurrentValue < 0)
        {
          str2 = Math.abs(this.mCurrentValue) + " m After";
          this.mValueText.setText(str2);
        }
    while (true)
    {
      return;
    }
  }
}
