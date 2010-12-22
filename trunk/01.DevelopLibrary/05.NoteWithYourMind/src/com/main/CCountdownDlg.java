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

public class CCountdownDlg extends CommentOutDlg implements View.OnClickListener
{
	public 	int		m_iHour;
	public	int		m_iMinute;
	TimePicker		m_Tp		=	null;
	
	public CCountdownDlg(Activity context )
	{
		super(context);
		m_iHour			=	CommonDefine.g_int_Invalid_ID;
		m_iMinute		=	CommonDefine.g_int_Invalid_ID;
	}
	
	public void setDisplay()
	{
        setContentView( R.layout.time );
        
        m_Tp	=	(TimePicker)findViewById(R.id.TimePicker01);
        m_Tp.setIs24HourView( true );
        
        if( ( m_iHour 	!= CommonDefine.g_int_Invalid_ID ) 
         && ( m_iMinute != CommonDefine.g_int_Invalid_ID ) )
        {
        	m_Tp.setCurrentHour(m_iHour);
        	m_Tp.setCurrentMinute(m_iMinute);
        }
        else
        {
        	m_Tp.setCurrentHour(0);
        	m_Tp.setCurrentMinute(0);        	
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.TimeCancel);
        btCancel.setOnClickListener(this);
        
        Button	btOK		=	(Button)findViewById(R.id.TimeOK);
        btOK.setOnClickListener(this);
        
        setProperty();
        setTitle("倒计时设定");
        show(); 
    }
    private void setProperty()
    {
        Window						window	=	getWindow();						//得到对话框的窗口．
        WindowManager.LayoutParams	wl		=	window.getAttributes();
        
        wl.x		=	m_iPosX;											
        wl.y		=	m_iPosX;		
        wl.width	=	300;
       
        window.setAttributes( wl );        
   }
    public void onClick(View view)
	{
        switch( view.getId() )
        {
	        case R.id.TimeCancel:
	        	cancel();
	            break;
	        case R.id.TimeOK:
	    		int	iHour	=	m_Tp.getCurrentHour();
	    		int	iMinute	=	m_Tp.getCurrentMinute();
	        	saveData( iHour, iMinute );
	        	cancel();
	            break;
	        default:
	        	break;
        }
    }
	
	public	void	saveData( int iHour, int iMinute )
	{
		m_iHour		=	iHour;
		m_iMinute	=	iMinute;
		
		TextView	CountDownTxt		=	(TextView)m_context.findViewById(R.id.daojishiTxt);
		
		if ( m_iHour < 0 || m_iMinute < 0 )
		{
			m_iHour		=	0;
			m_iMinute	=	0;
			
			CountDownTxt.setText("倒计时已过期!");
		}
		else
		{
			CountDownTxt.setText(Integer.toString(iHour) + "小时"+ Integer.toString(iMinute)+"分钟后提醒" );
		}
		
		RemindActivity.m_iType	=	1;
	}

}