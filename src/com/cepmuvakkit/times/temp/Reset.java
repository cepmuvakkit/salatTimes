package com.cepmuvakkit.times.temp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Reset extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    String s;
    String s1 = null;
    String s2 = null;
    String s3 = null;
    if (PreferenceManager.getDefaultSharedPreferences(this).getString("lang", "AR").equals("EN"))
    {
    	 s = "Reset Settings";
         s1 = "Settings will be deleted, and reset back to defaults.";
         s2 = "OK";
         s3 = "Cancel";

    }
    for (String str4 = "Cancel"; ; str4 = "إلغاء")
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setTitle(s1);
      localBuilder.setMessage(s2);
      localBuilder.setPositiveButton(s3, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Reset.this.reset();
          Reset.this.finish();
        }
      });
      localBuilder.setNegativeButton(str4, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Reset.this.finish();
        }
      }).setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          Reset.this.finish();
        }
      });
      localBuilder.show();
       s = "\u0627\u0633\u062A\u0639\u0627\u062F\u0629 \u0627\u0644\u0627\u0639\u062F\u0627\u062F\u0627\u062A";
      s1 = "\u0633\u064A\u062A\u0645 \u062D\u0630\u0641 \u0627\u0644\u0625\u0639\u062F\u0627\u062F\u0627\u062A \u0648\u0627\u0633\u062A\u0639\u0627\u062F\u0629 \u0627\u0644\u0642\u064A\u0645 \u0627\u0644\u0627\u0641\u062A\u0631\u0627\u0636\u064A\u0629.";
      s2 = "\u0645\u0648\u0627\u0641\u0642";
      s3 = "\u0625\u0644\u063A\u0627\u0621";
    }
  }

  public void reset()
  {
      String s = PreferenceManager.getDefaultSharedPreferences(this).getString("lang", "AR");
      android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      editor.clear();
      editor.putString("lang", s);
      editor.putBoolean("backup", true);
      boolean flag = editor.commit();
      String s1;
      String s2;
      if(s.equals("EN"))
      {
          s1 = "Setting reset completed";
          s2 = "Error occurred while resetting, try again";
      } else
      {
          s1 = "\u062A\u0645 \u0627\u0633\u062A\u0639\u0627\u062F\u0629 \u0627\u0644\u0625\u0639\u062F\u0627\u062F\u0627\u062A \u0627\u0644\u0627\u0641\u062A\u0631\u0627\u0636\u064A\u0629";
          s2 = "\u062D\u062F\u062B \u062E\u0637\u0623 \u0623\u062B\u0646\u0627\u0621 \u0627\u0644\u0625\u0633\u062A\u0639\u0627\u062F\u0629\u060C \u062D\u0627\u0648\u0644 \u0645\u0631\u0629 \u0623\u062E\u0631\u0649";
      }
      if(flag)
          Toast.makeText(getApplicationContext(), s1, 0).show();
      else
          Toast.makeText(getApplicationContext(), s2, 0).show();
  }
}
