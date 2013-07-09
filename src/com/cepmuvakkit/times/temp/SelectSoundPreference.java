package com.cepmuvakkit.times.temp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

public class SelectSoundPreference extends ListPreference
{
  private int mClickedDialogEntryIndex;
  private CharSequence[] mEntries = getEntries();
  private CharSequence[] mEntryValues = getEntryValues();

  public SelectSoundPreference(Context paramContext)
  {
    this(paramContext, null);
  }

  public SelectSoundPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private int getValueIndex()
  {
    return findIndexOfValue(getValue());
  }

  public int findIndexOfValue(String paramString)
  {
    if ((paramString != null) && (this.mEntryValues != null));
    for (int i = -1 + this.mEntryValues.length; ; i--)
    {
      if (i < 0)
        i = -1;
      while (this.mEntryValues[i].equals(paramString))
        return i;
    }
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    PlaySound.stopSound();
    if ((paramBoolean) && (this.mClickedDialogEntryIndex >= 0) && (this.mEntryValues != null))
    {
      String str = this.mEntryValues[this.mClickedDialogEntryIndex].toString();
      if (callChangeListener(str))
        setValue(str);
    }
  }

  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return paramTypedArray.getString(paramInt);
  }

  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    super.onPrepareDialogBuilder(paramBuilder);
    if ((this.mEntries == null) || (this.mEntryValues == null))
      throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
    this.mClickedDialogEntryIndex = getValueIndex();
    paramBuilder.setSingleChoiceItems(this.mEntries, this.mClickedDialogEntryIndex, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        SelectSoundPreference.this.mClickedDialogEntryIndex = paramAnonymousInt;
        SelectSoundPreference.this.onClick(paramAnonymousDialogInterface, -1);
        PlaySound.playSound(paramAnonymousInt);
        Log.d("SelectSoundPreference", "Click .........");
      }
    });
    paramBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
      }
    });
  }

  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean);
    for (String str = getPersistedString(getValue()); ; str = (String)paramObject)
    {
      setValue(str);
      return;
    }
  }
}

/* Location:           C:\Users\193198\Desktop\APKDOWNLOADER\apks\classes-dex2jar.jar
 * Qualified Name:     com.muslimtoolbox.app.android.prayertimes.home.settings.SelectSoundPreference
 * JD-Core Version:    0.6.2
 */