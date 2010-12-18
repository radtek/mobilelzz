package com.main;

//package com.main;n
/* import相关class */
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class CCountdownDlg extends CommentOutDlg implements View.OnClickListener
{
	private	Context	m_context;
	public 	int		iHour;
	public	int		iMinute;
	TimePicker		Tp		=	null;
	TextView	 	m_Time	=	null;
	
	public	void	Initialize( Byte type )
	{
		iHour		=	-1;
		iMinute		=	-1;
		m_bType		=	type;
	}
	
	public CCountdownDlg(Context context )
	{
		super(context);
		m_context	=	context;
	}
	
	public void setDisplay( TextView TimeTxt )
	{
        setContentView(R.layout.time);
        m_Time	=	TimeTxt;
        Tp	=	(TimePicker)findViewById(R.id.TimePicker01);
        Tp.setIs24HourView( true );
        if( iHour != -1 && iMinute != -1 )
        {
        	Tp.setCurrentHour((int)iHour);
        	Tp.setCurrentMinute((int)iMinute);
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
        Window		window	=	getWindow();						//得到对话框的窗口．
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x	=	iPosX;											//这两句设置了对话框的位置．
        wl.y	=	iPosY;		
        wl.width	=	250;
        //wl.gravity=Gravity.BOTTOM;         
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch(view.getId()){
        case R.id.TimeCancel:
        	cancel();
            break;
        case R.id.TimeOK:
        	saveData();
        	cancel();
            break;
        default:
        }
    }
	
	private	void	saveData()
	{
		iHour	=	Tp.getCurrentHour();
		iMinute	=	Tp.getCurrentMinute();
		if ( m_Time != null )
		{
			m_Time.setText(Integer.toString(iHour) + "小时"+ Integer.toString(iMinute)+"分钟后提醒" );
		}
		
		m_bType	=	1;
	}

}