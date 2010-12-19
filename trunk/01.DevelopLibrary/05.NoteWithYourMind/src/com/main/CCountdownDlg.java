package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.content.Context;
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
	TimePicker		Tp		=	null;
	
	public CCountdownDlg(Activity context )
	{
		super(context);
		m_context	=	context;
		m_iHour		=	-1;
		m_iMinute		=	-1;
	}
	
	public void setDisplay()
	{
        setContentView(R.layout.time);
        Tp	=	(TimePicker)findViewById(R.id.TimePicker01);
        Tp.setIs24HourView( true );
        if( m_iHour != -1 && m_iMinute != -1 )
        {
        	Tp.setCurrentHour((int)m_iHour);
        	Tp.setCurrentMinute((int)m_iMinute);
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.TimeCancel);
        btCancel.setOnClickListener(this);
        Button	btOK		=	(Button)findViewById(R.id.TimeOK);
        btOK.setOnClickListener(this);
        setProperty();
        setTitle("����ʱ�趨");
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
    		int	iHour	=	Tp.getCurrentHour();
    		int	iMinute	=	Tp.getCurrentMinute();
        	saveData( iHour, iMinute );
        	cancel();
            break;
        default:
        }
    }
	
	public	void	saveData( int iHour, int iMinute )
	{
		m_iHour		=	iHour;
		m_iMinute	=	iMinute;
		
		TextView	CountDownTxt		=	(TextView)m_context.findViewById(R.id.daojishiTxt);

		CountDownTxt.setText(Integer.toString(iHour) + "Сʱ"+ Integer.toString(iMinute)+"���Ӻ�����" );
		
		RemindActivity.m_bType	=	1;
	}

}