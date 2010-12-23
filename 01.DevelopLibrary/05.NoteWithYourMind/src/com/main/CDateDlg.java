package com.main;

//package com.main;n
/* import���class */
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
		m_iYear		=	CommonDefine.g_int_Invalid_ID;
		m_iMonth	=	CommonDefine.g_int_Invalid_ID;
		m_iDay		=	CommonDefine.g_int_Invalid_ID;
		
//		Calendar clCalendar	=	Calendar.getInstance();
//		clCalendar.setTimeInMillis(System.currentTimeMillis());
//		m_iYear		=	clCalendar.get(Calendar.YEAR);
//		m_iMonth	=	clCalendar.get(Calendar.MONTH);
//		m_iDay		=	clCalendar.get(Calendar.DAY_OF_MONTH);
//		
//		saveData( m_iYear, m_iMonth, m_iDay );
	}
	
	public void setDisplay( )
	{
        setContentView( R.layout.date );
        
        m_dp	=	(DatePicker)findViewById(R.id.DatePicker01);
        setProperty();
        setTitle("�����趨");
        
        if( CommonDefine.g_int_Invalid_ID != m_iYear 
        &&  CommonDefine.g_int_Invalid_ID != m_iMonth 
        &&  CommonDefine.g_int_Invalid_ID != m_iDay )
        {
        	m_dp.updateDate( m_iYear, m_iMonth, m_iDay );
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.DateCancel);
        btCancel.setOnClickListener(this);
        
        Button	btOK		=	(Button)findViewById(R.id.DateOK);
        btOK.setOnClickListener(this);
        
        show(); 
    }
    private void setProperty()
    {
        Window						window	=	getWindow();
        WindowManager.LayoutParams	wl		=	window.getAttributes();
        
        wl.x	=	m_iPosX;											
        wl.y	=	m_iPosY;
       
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch( view.getId() )
        {
	        case R.id.DateCancel:
	        	cancel();
	            break;
	        case R.id.DateOK:
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
	
	public	void	saveData( int iYear, int iMonth, int iDay )
	{
		m_iYear		=	iYear;
		m_iMonth	=	iMonth;
		m_iDay		=	iDay;
		
		TextView	DateTxt				=	(TextView)m_context.findViewById(R.id.OnceText);

		DateTxt.setText(Integer.toString(iYear) + "��" + Integer.toString( iMonth+1 ) + "��" + Integer.toString(iDay) + "��" );
		
		RemindActivity.m_iType	=	3;
	}
}