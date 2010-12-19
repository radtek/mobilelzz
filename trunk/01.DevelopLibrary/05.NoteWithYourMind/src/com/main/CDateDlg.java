package com.main;

//package com.main;n
/* import���class */
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
	public	int		iYear;
	public	int		iMonth;
	public	int		iDay;
	
	private	DatePicker	dp	=	null;
	
	TextView	m_Date		=	null;
	
	public CDateDlg(Context context)
	{
		super(context);
		m_context	=	context;
		iYear		=	-1;
		iMonth		=	-1;
		iDay		=	-1;
	}
	
	public void setDisplay( TextView TimeTxt )
	{
        setContentView(R.layout.date);
        dp	=	(DatePicker)findViewById(R.id.DatePicker01);
        setProperty();
        setTitle("�����趨");
        
        m_Date	=	TimeTxt;
        
        if( -1 != iYear && -1 != iMonth && -1 != iDay )
        {
        	dp.updateDate( iYear, iMonth, iDay );
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
    		iYear	=	dp.getYear();
    		iMonth	=	dp.getMonth();
    		iDay	=	dp.getDayOfMonth();
        	saveData( iYear, iMonth, iDay );
        	cancel();
            break;
        default:
        }
    }
	
	public	void	saveData( int iYear, int iMonth, int iDay )
	{

		if ( m_Date != null )
		{
			m_Date.setText(Integer.toString(iYear) + "��" + Integer.toString( iMonth+1 ) + "��" + Integer.toString(iDay) + "��" );
		}
		
		RemindActivity.m_bType	=	3;
	}
}