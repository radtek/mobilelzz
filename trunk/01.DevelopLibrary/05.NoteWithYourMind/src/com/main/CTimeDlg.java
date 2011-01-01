package com.main;

//package com.main;n
/* import相关class */
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class CTimeDlg extends CommentOutDlg implements View.OnClickListener
{
	public 	int		m_iHour;
	public	int		m_iMinute;
	TimePicker		m_Tp		=	null;
	
	public CTimeDlg(Activity context )
	{
		super(context);
		m_iHour		=	CommonDefine.g_int_Invalid_Time;
		m_iMinute	=	CommonDefine.g_int_Invalid_Time;

	}
	
	public void setDisplay( )
	{
        setContentView(R.layout.time);
        m_Tp	=	(TimePicker)findViewById(R.id.TimePicker01);
        m_Tp.setIs24HourView( true );
        if( m_iHour != CommonDefine.g_int_Invalid_Time && m_iMinute != CommonDefine.g_int_Invalid_Time )
        {
        	m_Tp.setCurrentHour(m_iHour);
        	m_Tp.setCurrentMinute(m_iMinute);
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.TimeCancel);
        btCancel.setOnClickListener(this);
        
        Button	btOK		=	(Button)findViewById(R.id.TimeOK);
        btOK.setOnClickListener(this);
        
        setProperty();
        setTitle("时间设定");
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x		=	m_iPosX;
        wl.y		=	m_iPosY;		
        wl.width	=	300;
        
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch(view.getId()){
        case R.id.TimeCancel:
        	saveCancel();
        	cancel();
            break;
        case R.id.TimeOK:
        	m_Tp.clearFocus();
        	m_iHour		=	m_Tp.getCurrentHour();
    		m_iMinute	=	m_Tp.getCurrentMinute();
        	saveData( m_iHour, m_iMinute );
        	cancel();
            break;
        default:
        	break;
       }
    }
	
	public	void	saveCancel()
	{
		if( m_iHour   == CommonDefine.g_int_Invalid_Time 
		 && m_iMinute == CommonDefine.g_int_Invalid_Time )
		{
			TextView	TimeTxt				=	(TextView)m_context.findViewById(R.id.Time_txt);
			TimeTxt.setText( "时间未设定");		
		}
	}
	
	public	void	saveData( int iHour, int iMinute )
	{
		m_iHour		=	iHour;
		m_iMinute	=	iMinute;
		
		TextView	TimeTxt				=	(TextView)m_context.findViewById(R.id.Time_txt);
		TimeTxt.setText(String.format("%02d:%02d", iHour, iMinute) );		
	}
}