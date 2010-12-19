package com.main;

//package com.main;n
/* import相关class */
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CWeekDlg extends CommentOutDlg implements View.OnClickListener
{
	byte		Week[]	=	new byte[7];	//从周日到周六
	private		boolean		bIsSelect	=	false;
	TextView	WeekTxt[];

	public CWeekDlg(Context context )
	{
		super(context);
		m_context	=	context;
		
		int	length	=	Week.length;
		for ( int i = 0; i < length; ++i )
		{
			Week[ i ]	=	-1;
		}
	}
	
	public void setDisplay( TextView week[])
	{
        setContentView(R.layout.week);
        setProperty();
        setTitle("星期设定");
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
        for( int i = 0; i < 7; ++i )
        {
        	if( 1 == Week[i] )
        	{
        		checkBox[i].setChecked(true);
        	}
        	else
        	{
        		checkBox[i].setChecked(false);
        	}
        }
        
        WeekTxt	=	week;
        
        Button	btCancel	=	(Button)findViewById(R.id.WeekCancel);
        btCancel.setOnClickListener(this);
        Button	btOK		=	(Button)findViewById(R.id.WeekOK);
        btOK.setOnClickListener(this);
        Button	btSelect	=	(Button)findViewById(R.id.remind_select);
        btSelect.setOnClickListener(this);
        show(); 
    }
    private void setProperty()
    {
        Window		window	=	getWindow();						//得到对话框的窗口．
        WindowManager.LayoutParams	wl	=	window.getAttributes();
        wl.x		=	iPosX;											//这两句设置了对话框的位置．
        wl.y		=	iPosY;	
        wl.width	=	300;       
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch(view.getId()){
        case R.id.WeekOK:
        	saveData();
        	cancel();
            break;
        case R.id.WeekCancel:
        	cancel();
        	break;
        case R.id.remind_select:
        	if ( bIsSelect )
			{
				/**反选**/
				bIsSelect	=	false;					
			}
			else
			{
				/**选择**/
				bIsSelect	=	true;
			}
			
			setCheckBoxFlg( bIsSelect );
            break;
        default:
        }
    }
	
	void	setCheckBoxFlg( boolean bFlg )
    { 	
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		checkBox[0].setChecked( bFlg );
		checkBox[1].setChecked( bFlg );
		checkBox[2].setChecked( bFlg );
		checkBox[3].setChecked( bFlg );
		checkBox[4].setChecked( bFlg );
		checkBox[5].setChecked( bFlg );
		checkBox[6].setChecked( bFlg );
    }
	
	void	setInputSatus( byte	week[] )
	{
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		int iLength	=	week.length;
  		for( int i = 0; i < iLength; ++i )
  		{
  			Week[ i ]	=	week[ i ];
  			if( week[ i ] == 1 )
  			{
  				WeekTxt[i].setTextColor(Color.GREEN);
  			}
  			else
  			{
  				WeekTxt[i].setTextColor(Color.WHITE);
  			}
  		}
	}
	
	void	saveData( )
	{
        CheckBox	checkBox[]	=	new	CheckBox[7];
		
		checkBox[0]	=	( CheckBox )findViewById( R.id.C1);
		checkBox[1]	=	( CheckBox )findViewById( R.id.C2);
		checkBox[2]	=	( CheckBox )findViewById( R.id.C3);
		checkBox[3]	=	( CheckBox )findViewById( R.id.C4);
		checkBox[4]	=	( CheckBox )findViewById( R.id.C5);
		checkBox[5]	=	( CheckBox )findViewById( R.id.C6);
		checkBox[6]	=	( CheckBox )findViewById( R.id.C7);	
		
		byte	bCheckFlg		=	0;
    	
    	if( checkBox[0].isChecked())
    	{
    		Week[0]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[0].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[0]		=	0;
    		WeekTxt[0].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[1].isChecked())
    	{
    		Week[1]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[1].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[1]	=	0;
    		WeekTxt[1].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[2].isChecked())
    	{
    		Week[2]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[2].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[2]		=	0;
    		WeekTxt[2].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[3].isChecked())
    	{
    		Week[3]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[3].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[3]		=	0;
    		WeekTxt[3].setTextColor(Color.WHITE);
    	}
    	
    	if( checkBox[4].isChecked())
    	{
    		Week[4]	=	1;
    		bCheckFlg	=	1;
    		WeekTxt[4].setTextColor(Color.GREEN);
    	}
    	else 
    	{
    		Week[4]		=	0;
    		WeekTxt[4].setTextColor(Color.WHITE);
    	}
    	if( checkBox[5].isChecked())
    	{
    		Week[5]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[5].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[5]		=	0;
    		WeekTxt[5].setTextColor(Color.WHITE);
    	}
    	if( checkBox[6].isChecked())
    	{
    		Week[6]		=	1;
    		bCheckFlg	=	1;
    		WeekTxt[6].setTextColor(Color.GREEN);
    	}
    	else
    	{
    		Week[6]		=	0;
    		WeekTxt[6].setTextColor(Color.WHITE);
    	}
    	
    	RemindActivity.m_bType	=	2;
//    	if ( 0 == bCheckFlg )
//    	{
//    		Toast toast = Toast.makeText(m_context, "没有设置星期，请重新设置!", Toast.LENGTH_SHORT);
//    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
//    		toast.show();		
//    	}
	}
}