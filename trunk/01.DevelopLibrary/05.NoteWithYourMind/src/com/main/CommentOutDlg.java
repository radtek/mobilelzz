package com.main;

//package com.main;n
/* import���class */
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/* ʵ����������Dialog��Activity */
public class CommentOutDlg extends Dialog implements View.OnClickListener
{
	private Context m_context;
	public CommentOutDlg(Context context){
		super(context);
		m_context = context;
	}
	public void setDisplay(){
        setContentView(R.layout.toolbar_more_dlg);
        setProperty();
        setTitle("mouse panel");
        View vdlgback  = findViewById(R.id.toolbar_more_dlg);
        Animation anim = AnimationUtils.loadAnimation(m_context, R.anim.commentout);
        vdlgback.startAnimation(anim);
        show();
        //getInstance();
        //setListener();    
    }
    private void setProperty(){
        Window window= getWindow();//�õ��Ի���Ĵ��ڣ�
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x =1000;//�����������˶Ի����λ�ã�
        wl.y =1000;
        wl.alpha=0.6f;//��������˶Ի����͸����
        //wl.gravity=Gravity.BOTTOM;         
        window.setAttributes(wl);        
   }
	public void onClick(View view){
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
