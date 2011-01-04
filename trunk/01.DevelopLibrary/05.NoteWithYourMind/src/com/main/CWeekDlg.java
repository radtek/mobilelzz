package com.main;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CWeekDlg extends CommentOutDlg implements View.OnClickListener
{
	byte		m_bWeek[]	=	new byte[7];	//从周日到周六
	boolean		m_bIsSelect	=	false;

	public CWeekDlg( Activity context )
	{
		super(context);
		
		int	length	=	m_bWeek.length;
		for ( int i = 0; i < length; ++i )
		{
			m_bWeek[ i ]	=	(byte)CommonDefine.g_int_Invalid_Time;
		}
		
		m_iType	=	CommonDefine.Remind_Type_Week;
	}
	
	public void setDisplay()
	{
        setContentView(R.layout.week);
        setProperty();
        setTitle("星期设定");
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
        for( int i = 0; i < 7; ++i )
        {
        	if( 1 == m_bWeek[i] )
        	{
        		checkBox[i].setChecked(true);
        	}
        	else
        	{
        		checkBox[i].setChecked(false);
        	}
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.WeekCancel);
        btCancel.setOnClickListener(this);
        
        Button	btOK		=	(Button)findViewById(R.id.WeekOK);
        btOK.setOnClickListener(this);
        
        Button	btSelect	=	(Button)findViewById(R.id.remind_select);
        btSelect.setOnClickListener(this);
        
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();	
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x		=	m_iPosX;	
        wl.y		=	m_iPosY;	
//        wl.width	=	400;       
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch(view.getId()){
        case R.id.WeekOK:
        	saveData();
        	cancel();
            break;
        case R.id.WeekCancel:
        	saveCancel();
        	cancel();
        	break;
        case R.id.remind_select:
        	if ( m_bIsSelect )
			{
				/**反选**/
        		m_bIsSelect	=	false;					
			}
			else
			{
				/**选择**/
				m_bIsSelect	=	true;
			}
			
			setCheckBoxFlg( m_bIsSelect );
            break;
        default:
        }
    }
	
	public	void	saveCancel()
	{
		RemindActivity.m_iType	=	m_iType;
	}
	
	void	setCheckBoxFlg( boolean bFlg )
    { 	
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		checkBox[0].setChecked( bFlg );
		checkBox[1].setChecked( bFlg );
		checkBox[2].setChecked( bFlg );
		checkBox[3].setChecked( bFlg );
		checkBox[4].setChecked( bFlg );
		checkBox[5].setChecked( bFlg );
		checkBox[6].setChecked( bFlg );
    }
	
	void	setInputSatus( byte	week[] )
	{
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		TextView	WeekTxt[]	=	new	TextView[ 7 ];
    	WeekTxt[0]			=	(TextView)m_context.findViewById(R.id.MonTxt);
    	WeekTxt[1]			=	(TextView)m_context.findViewById(R.id.TusTxt);
    	WeekTxt[2]			=	(TextView)m_context.findViewById(R.id.WedTxt);
    	WeekTxt[3]			=	(TextView)m_context.findViewById(R.id.ThrTxt);
    	WeekTxt[4]			=	(TextView)m_context.findViewById(R.id.FriTxt);
    	WeekTxt[5]			=	(TextView)m_context.findViewById(R.id.SatTxt);
    	WeekTxt[6]			=	(TextView)m_context.findViewById(R.id.SunTxt);
		
		int iLength	=	week.length;
  		for( int i = 0; i < iLength; ++i )
  		{
  			m_bWeek[ i ]	=	week[ i ];
  			if( week[ i ] == 1 )
  			{
  				WeekTxt[i].setTextColor(Color.GREEN);
  			}
  			else
  			{
  				WeekTxt[i].setTextColor(Color.WHITE);
  			}
  		}
  		
  		RemindActivity.m_iType	=	m_iType;
	}
	
	void	saveData( )
	{
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		TextView	WeekTxt[]	=	new	TextView[ 7 ];
    	WeekTxt[0]			=	(TextView)m_context.findViewById(R.id.MonTxt);
    	WeekTxt[1]			=	(TextView)m_context.findViewById(R.id.TusTxt);
    	WeekTxt[2]			=	(TextView)m_context.findViewById(R.id.WedTxt);
    	WeekTxt[3]			=	(TextView)m_context.findViewById(R.id.ThrTxt);
    	WeekTxt[4]			=	(TextView)m_context.findViewById(R.id.FriTxt);
    	WeekTxt[5]			=	(TextView)m_context.findViewById(R.id.SatTxt);
    	WeekTxt[6]			=	(TextView)m_context.findViewById(R.id.SunTxt);
		
		if( checkBox[0].isChecked())
    	{
    		m_bWeek[0]		=	1;
    		WeekTxt[0].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[0]		=	0;
    		WeekTxt[0].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[1].isChecked())
    	{
    		m_bWeek[1]		=	1;
    		WeekTxt[1].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[1]	=	0;
    		WeekTxt[1].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[2].isChecked())
    	{
    		m_bWeek[2]		=	1;
    		WeekTxt[2].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[2]		=	0;
    		WeekTxt[2].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[3].isChecked())
    	{
    		m_bWeek[3]		=	1;
    		WeekTxt[3].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[3]		=	0;
    		WeekTxt[3].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[4].isChecked())
    	{
    		m_bWeek[4]	=	1;
    		WeekTxt[4].setTextColor(Color.GREEN);
    	}
    	else 
    	{
    		m_bWeek[4]		=	0;
    		WeekTxt[4].setTextColor(Color.WHITE);
    	}
    	if( checkBox[5].isChecked())
    	{
    		m_bWeek[5]		=	1;
    		WeekTxt[5].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[5]		=	0;
    		WeekTxt[5].setTextColor(Color.WHITE);
    	}
    	if( checkBox[6].isChecked())
    	{
    		m_bWeek[6]		=	1;
    		WeekTxt[6].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		m_bWeek[6]		=	0;
    		WeekTxt[6].setTextColor(Color.WHITE);
    	}
    	
    	RemindActivity.m_iType	=	m_iType;

	}
}