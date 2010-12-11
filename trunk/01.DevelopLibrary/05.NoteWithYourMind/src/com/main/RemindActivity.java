package com.main;

import java.util.Calendar;

import com.main.NoteWithYourMind.OperationNoteKindEnum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class RemindActivity extends Activity
{
	
	CRemindInfo	clCRemindInfo	=	new	CRemindInfo ( (byte)-1 );
	
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting2);

        Intent iExtraData = getIntent();
        CRemindInfo	clTemp	=	(CRemindInfo)iExtraData.getSerializableExtra(NoteWithYourMind.ExtraData_RemindSetting);
        if ( null != clTemp )
        {
        	clCRemindInfo	=	clTemp;
        }
        else
        {
        	
        }
        //�趨����
        settingWeek();
        //�趨ȷ��
        settingCheck();
        //�趨����
        settingDate();
        //ֹͣ����
        settingAble();
        
        EditText xiaoshi = (EditText) findViewById(R.id.xiaoshi); 
        xiaoshi.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME ); 
 //       xiaoshi.setText(String.valueOf(value));
        
        EditText fenzhong1 = (EditText) findViewById(R.id.fenzhong1); 
        fenzhong1.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME ); 
        
        EditText xiaoshi2 = (EditText) findViewById(R.id.xiaoshi2); 
        xiaoshi2.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME ); 
        
        EditText fenzhong2 = (EditText) findViewById(R.id.fenzhong2); 
        fenzhong2.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME ); 

    }
    
    void	settingAble()
    {
    	Button	BtAble	=	(Button) findViewById(R.id.RemindAble);
    	BtAble.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    }

    void	settingDate()
    {
    	ImageButton	clWeekSetting	=	(ImageButton) findViewById(R.id.OnceRemindImg);
    	clWeekSetting.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar	clCalendar	=	Calendar.getInstance();

				clCalendar.setTimeInMillis(clCRemindInfo.lTime);
				
				if ( 1970 == clCalendar.get(Calendar.YEAR))
				{
					clCalendar.setTimeInMillis(System.currentTimeMillis());
				}


				int	year	=	clCalendar.get(Calendar.YEAR); 
				int month	=	clCalendar.get(Calendar.MONTH); 
				int day		=	clCalendar.get(Calendar.DAY_OF_MONTH); 
				//��������е� year month day 
				DatePickerDialog dlg	=	new DatePickerDialog( RemindActivity.this,mDateSetListener,year,month,day); 
				dlg.show(); 
			}
    		
    	});
    	

    }
    private DatePickerDialog.OnDateSetListener mDateSetListener	=	new	DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			// TODO Auto-generated method stub
			clCRemindInfo.m_bType	=	3;
			Calendar	clCalendar	=	Calendar.getInstance();
			clCalendar.set(Calendar.YEAR, year);
			clCalendar.set(Calendar.MONTH, monthOfYear );
			clCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			clCalendar.set(Calendar.HOUR_OF_DAY, 0);
			clCalendar.set(Calendar.MINUTE, 0);
			clCalendar.set(Calendar.SECOND, 0);
			clCalendar.set(Calendar.MILLISECOND, 0);
			clCRemindInfo.lTime	=	clCalendar.getTimeInMillis();
			String	strWeek	=	getDayofWeek( clCalendar );
			TextView	clTextView	=	(TextView) findViewById(R.id.OnceText);
			clTextView.setText( String.valueOf(year)+ "��" + String.valueOf(monthOfYear+1) + "��" + String.valueOf(dayOfMonth) + "��" 
								+ "����" + strWeek );
		}
    	
    };
    
    String	getDayofWeek( Calendar	clCalendar )
    {
    	String	strTemp	=	null;
    	switch( clCalendar.get(Calendar.DAY_OF_WEEK) )
    	{
    	case	1:
    		strTemp	=	"��";
    		break;
    	case	2:
    		strTemp	=	"һ";
    		break;
    	case	3:
    		strTemp	=	"��";
    		break;
    	case	4:
    		strTemp	=	"��";
    		break;
    	case	5:
    		strTemp	=	"��";
    		break;
    	case	6:
    		strTemp	=	"��";
    		break;
    	case	7:
    		strTemp	=	"��";
    		break;
    	}
    	
    	return	strTemp;
    }
    
    void	settingCheck()
    {
    	Button	clCheck	=	(Button) findViewById(R.id.FixBtn);
    	clCheck.setOnClickListener( new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( 2 == clCRemindInfo.m_bType )		//ѭ������
				{
					EditText Xiaoshi	=	(EditText) findViewById(R.id.xiaoshi);
					EditText Fenzhong	=	(EditText) findViewById(R.id.fenzhong1);
					
					String	strhour		=		Xiaoshi.getText().toString();
					String	strminute	=		Fenzhong.getText().toString();
					
					clCRemindInfo.setTime( Long.valueOf(strhour) , Long.valueOf(strminute));		
				}
				else if( 1 == clCRemindInfo.m_bType )	//����ʱ����
				{
					Calendar clCalendar	=	Calendar.getInstance();
					
					EditText Xiaoshi	=	(EditText) findViewById(R.id.xiaoshi2);
					EditText Fenzhong	=	(EditText) findViewById(R.id.fenzhong2);
					
					long	lhour		=	Long.valueOf( Xiaoshi.getText().toString() );
					long	lminute		=	Long.valueOf( Fenzhong.getText().toString() );
					
					clCalendar.setTimeInMillis(System.currentTimeMillis());
					clCalendar.set(Calendar.SECOND, 0 );
					clCalendar.set(Calendar.MILLISECOND, 0 );
					
					long	lTime	=	clCalendar.getTimeInMillis();
					lTime	+=	( lhour * 60 + lminute ) *1000;
					
					clCRemindInfo.lTime		=	lTime;				
				}
				else if( 3 == clCRemindInfo.m_bType )
				{
					Calendar clCalendar	=	Calendar.getInstance();
					clCalendar.setTimeInMillis( clCRemindInfo.lTime );
					EditText Xiaoshi	=	(EditText) findViewById(R.id.xiaoshi);
					EditText Fenzhong	=	(EditText) findViewById(R.id.fenzhong1);
					
					int	lhour		=	Integer.valueOf( Xiaoshi.getText().toString() );
					int	lminute		=	Integer.valueOf( Fenzhong.getText().toString() );
					
					clCalendar.set(Calendar.HOUR_OF_DAY, lhour );
					clCalendar.set(Calendar.MINUTE, lminute );
					
					clCRemindInfo.lTime	=	clCalendar.getTimeInMillis();
				}
				
				Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);  
//				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
				intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, clCRemindInfo );
				intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
				
				startActivity(intent);	
			}
    		
    	});
    }
	
	void settingWeek()
	{
		ImageButton	clWeekSetting	=	(ImageButton) findViewById(R.id.EveryWeekImg);
        clWeekSetting.setOnClickListener(new Button.OnClickListener()
        {
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
	        	final Byte	bCheck[]	=	new Byte[ 7 ];
	        	for ( int i = 0; i < 7 ; ++i )
	        	{
	        		bCheck[ i ]	=	-1;
	        	}
				Dialog dialog	=	new AlertDialog.Builder( RemindActivity.this).setTitle("ѡ������").setMultiChoiceItems(
			     new String[] {
			    		 		"����һ", "���ڶ�",
			    		 		"������", "������",
			    		 		"������", "������",
			    		 		"������"
			    		 		}, null, new DialogInterface.OnMultiChoiceClickListener()
			     				{

									@Override
									public void onClick(DialogInterface dialog, int which, boolean isChecked)
									{
										// TODO Auto-generated method stub
										if( isChecked )
										{
											bCheck[ which ]	=	1;
										}
										else
										{
											bCheck[ which ]	=	-1;
										}
										
									}

								} )
			     .setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
			     {
			    	 public void onClick(DialogInterface dialog, int whichButton)
	                 {
	                        /* User clicked OK so do some stuff */
			    		 clCRemindInfo.m_bType	=	2;
			    		 clCRemindInfo.m_Week	=	bCheck;			    		 
	                 }
	             })
			     .setNegativeButton("ȡ��", null).create();

				dialog.show();
			}

        });
	}
}
