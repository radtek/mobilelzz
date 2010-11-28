package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/* ʵ����������Dialog��Activity */
public class AlarmAlert extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    /* ���������徯ʾ  */
    new AlertDialog.Builder(AlarmAlert.this)
        .setIcon(R.drawable.clock)
        .setTitle("��������!!")
        .setMessage("�Ͽ��𴲰�!!!")
        .setPositiveButton("�ص���",
         new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            /* �ر�Activity */
            AlarmAlert.this.finish();
          }
        })
        .show();
  } 
}