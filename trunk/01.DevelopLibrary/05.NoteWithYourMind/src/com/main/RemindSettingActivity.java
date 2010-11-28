package com.main;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class RemindSettingActivity extends ActivityGroup 
{
   
    static	private		boolean		bIsSelect	=	false;
    
    TabHost m_TabHost;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsettingnew);
        
        //����TabHost
        m_TabHost = (TabHost)findViewById(android.R.id.tabhost);
        m_TabHost.setup(this.getLocalActivityManager());
        
        TabSpec specLater = m_TabHost.newTabSpec( "1" );
        specLater.setIndicator("�������");
        specLater.setContent(R.id.remindinterval);
        m_TabHost.addTab(specLater);
        
        TabSpec specCycel = m_TabHost.newTabSpec("2");
        specCycel.setIndicator("ѭ������");
        specCycel.setContent(R.id.remindcycel);
        m_TabHost.addTab(specCycel);
        
        TabSpec specOnce = m_TabHost.newTabSpec("3");
        specOnce.setIndicator("��ʱ����");
        specOnce.setContent(R.id.remindonce);
        m_TabHost.addTab(specOnce);
     
        m_TabHost.setOnTabChangedListener(new OnTabChangeListener()
        {
			@Override
			public void onTabChanged(String tabId)
			{
				// TODO Auto-generated method stub
				if(tabId.equals(String.valueOf(1)))
				{
					
				}
				else if ( tabId.equals(String.valueOf(2)))	
				{
					
				}			
				else if ( tabId.equals(String.valueOf(3)))
				{
					
				}
			}
        	
        });
        //���ȷ��ʱǨ�ƻ�ҳ����NewNoteKindEnum����ΪRemindSetting_Kind����������ΪSingleTop
/////////////////////////////////////////////////////////////////////////////////////////        
        Button clSelect = (Button) findViewById(R.id.remind_select);
        clSelect.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( bIsSelect )
				{
					/**��ѡ**/
					bIsSelect	=	false;					
				}
				else
				{
					/**ѡ��**/
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
