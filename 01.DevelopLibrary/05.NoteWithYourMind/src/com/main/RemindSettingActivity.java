package com.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class RemindSettingActivity extends Activity {
    /** Called when the activity is first created. */
   
    static	private		boolean		bIsSelect	=	false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting);
        
        ImageButton clSelect = (ImageButton) findViewById(R.id.remind_select);
        clSelect.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( bIsSelect )
				{
					/**ทดัก**/
					bIsSelect	=	false;					
				}
				else
				{
					/**ักิ๑**/
					bIsSelect	=	true;
				}
				
				setCheckBoxFlg( bIsSelect );
			}
        });
    }
    
    void	setCheckBoxFlg( boolean bFlg )
    {
    	CheckBox	checkBox1	=	( CheckBox )findViewById( R.id.C1);
    	CheckBox	checkBox2	=	( CheckBox )findViewById( R.id.C2);
    	CheckBox	checkBox3	=	( CheckBox )findViewById( R.id.C3);
    	CheckBox	checkBox4	=	( CheckBox )findViewById( R.id.C4);
    	CheckBox	checkBox5	=	( CheckBox )findViewById( R.id.C5);
    	CheckBox	checkBox6	=	( CheckBox )findViewById( R.id.C6);
    	CheckBox	checkBox7	=	( CheckBox )findViewById( R.id.C7);
    	
    	checkBox1.setChecked( bFlg );
    	checkBox2.setChecked( bFlg );
    	checkBox3.setChecked( bFlg );
    	checkBox4.setChecked( bFlg );
    	checkBox5.setChecked( bFlg );
    	checkBox6.setChecked( bFlg );
    	checkBox7.setChecked( bFlg );
    }
}
