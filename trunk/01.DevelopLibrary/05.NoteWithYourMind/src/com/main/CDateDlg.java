package com.main;

//package com.main;n
/* import���class */
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

public class CDateDlg extends CommentOutDlg implements View.OnClickListener
{
	private	Context	m_context;
	public	int		iYear;
	public	int		iMonth;
	public	int		iDay;
	
	
	public CDateDlg(Context context)
	{
		super(context);
		m_context	=	context;
		iYear		=	-1;
		iMonth		=	-1;
		iDay		=	-1;
	}
	
	public void setDisplay()
	{
        setContentView(R.layout.date);
        DatePicker	dp	=	(DatePicker)findViewById(R.id.DatePicker01);
        setProperty();
        setTitle("�����趨");
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
//        switch(view.getId()){
//        case R.id.mouse_button1:
//            dismiss();
//            break;
//        case R.id.mouse_button2:
//        case R.id.mouse_button3:
//            dismiss();
//            new MyDialog(getContext()).setDisplay();
//            break;
//        default:
//        }
    }
}