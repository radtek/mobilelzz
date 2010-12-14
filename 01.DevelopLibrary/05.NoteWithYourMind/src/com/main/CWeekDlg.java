package com.main;

//package com.main;n
/* import相关class */
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CWeekDlg extends Dialog implements View.OnClickListener
{
	private	Context	m_context;
	byte		Week[]	=	new byte[7];	//从周日到周六
	
	
	public CWeekDlg(Context context)
	{
		super(context);
		m_context	=	context;
		int	length	=	Week.length;
		for ( int i = 0; i < length; ++i )
		{
			Week[ i ]	=	-1;
		}
	}
	
	public void setDisplay()
	{
        setContentView(R.layout.week);
        setProperty();
        setTitle("时间设定");
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();						//得到对话框的窗口．
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x	=	1000;											//这两句设置了对话框的位置．
        wl.y	=	1000;
        wl.alpha=	0.6f;											//这句设置了对话框的透明度
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