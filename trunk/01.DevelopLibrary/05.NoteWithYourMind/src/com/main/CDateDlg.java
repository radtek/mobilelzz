package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
	
	private	DatePicker	dp	=	null;
	
	
	public CDateDlg(Activity context)
	{
		super(context);
		m_context	=	context;
		m_iYear		=	-1;
		m_iMonth		=	-1;
		m_iDay		=	-1;
	}
	
	public void setDisplay( )
	{
        setContentView(R.layout.date);
        dp	=	(DatePicker)findViewById(R.id.DatePicker01);
        setProperty();
        setTitle("�����趨");
        
        if( -1 != m_iYear && -1 != m_iMonth && -1 != m_iDay )
        {
        	dp.updateDate( m_iYear, m_iMonth, m_iDay );
        }
        
        Button	btCancel	=	(Button)findViewById(R.id.DateCancel);
        btCancel.setOnClickListener(this);
        Button	btOK		=	(Button)findViewById(R.id.DateOK);
        btOK.setOnClickListener(this);
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();						//�õ��Ի���Ĵ��ڣ�
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x	=	iPosX;											//�����������˶Ի����λ�ã�
        wl.y	=	iPosY;
//        wl.alpha=	0.6f;											//��������˶Ի����͸����
        //wl.gravity=Gravity.BOTTOM;         
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch(view.getId()){
        case R.id.DateCancel:
        	cancel();
            break;
        case R.id.DateOK:
        	m_iYear	=	dp.getYear();
        	m_iDay	=	dp.getMonth();
        	m_iDay	=	dp.getDayOfMonth();
        	saveData( m_iYear, m_iMonth, m_iDay );
        	cancel();
            break;
        default:
        }
    }
	
	public	void	saveData( int iYear, int iMonth, int iDay )
	{
		m_iYear		=	iYear;
		m_iMonth	=	iMonth;
		m_iDay		=	iDay;
		
		TextView	DateTxt				=	(TextView)m_context.findViewById(R.id.OnceText);

		DateTxt.setText(Integer.toString(iYear) + "��" + Integer.toString( iMonth+1 ) + "��" + Integer.toString(iDay) + "��" );
		
		RemindActivity.m_bType	=	3;
	}
}