package com.main;

//package com.main;n
/* import相关class */
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;

public class CTimeDlg extends CommentOutDlg implements View.OnClickListener
{
	private	Context	m_context;
	public 	int		iHour;
	public	int		iMinute;
	TimePicker		Tp		=	null;
	RadioButton 	m_Time	=	null;
	
	
	public CTimeDlg(Context context)
	{
		super(context);
		m_context	=	context;
		iHour		=	-1;
		iMinute		=	-1;
	}
	
	public void setDisplay( RadioButton rbTime )
	{
        setContentView(R.layout.time);
        m_Time	=	rbTime;
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
        setTitle("时间设定");
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
			m_Time.setText(Integer.toString(iHour) + "小时"+ Integer.toString(iMinute)+"分钟" );
		}
	}
}