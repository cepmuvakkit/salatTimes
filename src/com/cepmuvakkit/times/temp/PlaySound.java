package com.cepmuvakkit.times.temp;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import java.io.IOException;


public class PlaySound
{
  private static final int[] listAdhan = { 2131099648, 2131099649, 2131099650 };
  private static MediaPlayer mediaPlayer = null;

  public static void playSound(int paramInt)
  {
    Uri localUri = Uri.parse("android.resource://com.muslimtoolbox.app.android.prayertimes/" + listAdhan[paramInt]);
    stopSound();
    try
    {
      mediaPlayer = new MediaPlayer();
      mediaPlayer.reset();
      mediaPlayer.setDataSource(PrayerApplication.getContext(), localUri);
      mediaPlayer.prepare();
      MediaPlayer.OnPreparedListener local1 = new MediaPlayer.OnPreparedListener()
      {
        public void onPrepared(MediaPlayer paramAnonymousMediaPlayer)
        {
          PlaySound.mediaPlayer.start();
        }
      };
      mediaPlayer.setOnPreparedListener(local1);
      MediaPlayer.OnCompletionListener local2 = new MediaPlayer.OnCompletionListener()
      {
        public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
        {
          PlaySound.mediaPlayer.release();
          PlaySound.mediaPlayer = null;
        }
      };
      mediaPlayer.setOnCompletionListener(local2);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
        localIllegalArgumentException.printStackTrace();
    }
    catch (SecurityException localSecurityException)
    {
      while (true)
        localSecurityException.printStackTrace();
    }
    catch (IllegalStateException localIllegalStateException)
    {
      while (true)
        localIllegalStateException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public static void stopSound()
  {
    if (mediaPlayer != null)
    {
      mediaPlayer.stop();
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }
}

