package com.main;

//package com.main;n
/* import���class */
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class CTimeDlg extends CommentOutDlg implements View.OnClickListener
{
	public 	int		iHour;
	public	int		iMinute;
	TimePicker		Tp		=	null;
	TextView	 	m_Time	=	null;
	Byte			m_bType	=	-1;
	
	public CTimeDlg(Context context )
	{
		super(context);
		m_context	=	context;
		iHour		=	-1;
		iMinute		=	-1;

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
        setTitle("ʱ���趨");
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();						//�õ��Ի���Ĵ��ڣ�
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x	=	iPosX;											//�����������˶Ի����λ�ã�
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
    		iHour	=	Tp.getCurrentHour();
    		iMinute	=	Tp.getCurrentMinute();
        	saveData( iHour, iMinute );
        	cancel();
            break;
        default:
        }
    }
	
	public	void	saveData( int iHour, int iMinute )
	{
		if ( m_Time != null )
		{
			m_Time.setText(Integer.toString(iHour) + "Сʱ"+ Integer.toString(iMinute)+"����" );
		}
	}

}