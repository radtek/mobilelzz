package com.main;

//package com.main;n
/* import���class */
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CDateDlg extends Dialog implements View.OnClickListener
{
	private	Context	m_context;
	public	int		iYear;
	public	byte	bMonth;
	public	byte	bDay;
	
	
	public CDateDlg(Context context)
	{
		super(context);
		m_context	=	context;
		iYear		=	-1;
		bMonth		=	-1;
		bDay		=	-1;
	}
	
	public void setDisplay()
	{
        setContentView(R.layout.date);
        setProperty();
        setTitle("�����趨");
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();						//�õ��Ի���Ĵ��ڣ�
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x	=	1000;											//�����������˶Ի����λ�ã�
        wl.y	=	1000;
        wl.alpha=	0.6f;											//��������˶Ի����͸����
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