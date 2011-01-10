package com.main;

//package com.main;n
/* import���class */
import java.util.Calendar;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CDateDlg extends CommentOutDlg implements View.OnClickListener
{
	public	int		m_iYear;
	public	int		m_iMonth;
	public	int		m_iDay;
	
	private	DatePicker	m_dp	=	null;
	
	
	public CDateDlg(Activity context)
	{
		super(context);
		m_iYear		=	CommonDefine.g_int_Invalid_Time;
		m_iMonth	=	CommonDefine.g_int_Invalid_Time;
		m_iDay		=	CommonDefine.g_int_Invalid_Time;
		
		m_iType		=	CommonDefine.Remind_Type_Once;
		
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		m_iYear		=	clCalendar.get(Calendar.YEAR);
		m_iMonth	=	clCalendar.get(Calendar.MONTH);
		m_iDay		=	clCalendar.get(Calendar.DAY_OF_MONTH);
//		
//		saveData( m_iYear, m_iMonth, m_iDay );
	}
	
	public void setDisplay( )
	{
        setContentView( R.layout.date );
        
        m_dp	=	(DatePicker)findViewById(R.id.DatePicker01);
        setProperty();
        Calendar clCalendar     =     Calendar. getInstance();
        clCalendar.set(Calendar. YEAR, m_iYear );
        clCalendar.set(Calendar.MONTH, m_iMonth );
        clCalendar.set(Calendar. DAY_OF_MONTH, m_iDay );

        setTitle(getDayofWeek(clCalendar));
        
        if( CommonDefine.g_int_Invalid_Time != m_iYear 
        &&  CommonDefine.g_int_Invalid_Time != m_iMonth 
        &&  CommonDefine.g_int_Invalid_Time != m_iDay )
        {
        	m_dp.updateDate( m_iYear, m_iMonth, m_iDay );
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.DateCancel);
        btCancel.setOnClickListener(this);
        
        Button	btOK		=	(Button)findViewById(R.id.DateOK);
        btOK.setOnClickListener(this);
        
        m_dp.init(m_iYear, m_iMonth, m_iDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar clCalendar     =     Calendar. getInstance();
                clCalendar.set(Calendar. YEAR, year );
                clCalendar.set(Calendar.MONTH, monthOfYear );
                clCalendar.set(Calendar. DAY_OF_MONTH, dayOfMonth );

                setTitle(getDayofWeek(clCalendar));
            }
        });

        
        show(); 
    }
    private void setProperty()
    {
        Window						window	=	getWindow();
        WindowManager.LayoutParams	wl		=	window.getAttributes();
        
        wl.x	=	m_iPosX;											
        wl.y	=	m_iPosY;
        
//        wl.width	=	300;
       
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch( view.getId() )
        {
	        case R.id.DateCancel:
	        	saveCancel();
	        	cancel();
	            break;
	        case R.id.DateOK:
	        	m_dp.clearFocus();
	        	m_iYear		=	m_dp.getYear();
	        	m_iMonth	=	m_dp.getMonth();
	        	m_iDay		=	m_dp.getDayOfMonth();
	        	saveData( m_iYear, m_iMonth, m_iDay );
	        	cancel();
	            break;
	        default:
	        	break;
        }
    }
	
	public	void	saveCancel()
	{
		if( m_iYear  == CommonDefine.g_int_Invalid_Time 
		 && m_iMonth == CommonDefine.g_int_Invalid_Time
		 && m_iDay   == CommonDefine.g_int_Invalid_Time)
		{
			TextView	DateTxt				=	(TextView)m_context.findViewById(R.id.OnceText);
			DateTxt.setText( "����δ�趨");
			
			RemindActivity.m_iType	=	m_iType;
		}
	}
	
	public	void	saveData( int iYear, int iMonth, int iDay )
	{
		m_iYear		=	iYear;
		m_iMonth	=	iMonth;
		m_iDay		=	iDay;
		
        Calendar clCalendar     =     Calendar.getInstance();
        clCalendar.setTimeInMillis(System.currentTimeMillis());
        clCalendar.set(Calendar.YEAR, iYear);
        clCalendar.set(Calendar.MONTH, iMonth);
        clCalendar.set(Calendar.DAY_OF_MONTH, iDay);
		
		TextView	DateTxt				=	(TextView)m_context.findViewById(R.id.OnceText);

		DateTxt.setText(Integer.toString(iYear) + "��" + Integer.toString( iMonth+1 ) + "��" + Integer.toString(iDay) + "��" + "  " + getDayofWeek(clCalendar));
		
		RemindActivity.m_iType	=	m_iType;
	}
	
    static	String	getDayofWeek( Calendar	clCalendar )
    {
    	String	strTemp	=	null;
    	switch( clCalendar.get(Calendar.DAY_OF_WEEK) )
    	{
    	case	1:
    		strTemp	=	"������";
    		break;
    	case	2:
    		strTemp	=	"����һ";
    		break;
    	case	3:
    		strTemp	=	"���ڶ�";
    		break;
    	case	4:
    		strTemp	=	"������";
    		break;
    	case	5:
    		strTemp	=	"������";
    		break;
    	case	6:
    		strTemp	=	"������";
    		break;
    	case	7:
    		strTemp	=	"������";
    		break;
    	}
    	
    	return	strTemp;
    }
    
    static	String	getDayofWeek2( Calendar	clCalendar )
    {
    	String	strTemp	=	null;
    	switch( clCalendar.get(Calendar.DAY_OF_WEEK) )
    	{
    	case	1:
    		strTemp	=	"������";
    		break;
    	case	2:
    		strTemp	=	"����һ";
    		break;
    	case	3:
    		strTemp	=	"���ܶ�";
    		break;
    	case	4:
    		strTemp	=	"������";
    		break;
    	case	5:
    		strTemp	=	"������";
    		break;
    	case	6:
    		strTemp	=	"������";
    		break;
    	case	7:
    		strTemp	=	"������";
    		break;
    	}
    	
    	return	strTemp;
    }
}