package com.main;

import java.util.HashMap;

import android.content.Intent;

public final class CRemindOperator
{
    private static final CRemindOperator _INSTANCE	=	new CRemindOperator();  
    
//    private	HashMap< Long, CRemindInfo >	m_hashMap;
    
    private CNoteDBCtrl m_clCNoteDBCtrl = RootViewList.m_clCNoteDBCtrl;
    private CRemindOperator()
    {
    	//��ȡ����Ҫ�����ѵļ�¼���浽hashMap�У�����ÿ���������õ�alarmManager��
    }  
      
    public synchronized static CRemindOperator getInstance()
    {  
        return _INSTANCE;  
    }  
    
    public	void	addRemind( long _id, CRemindInfo _clCRemindInfo )
    {
    	//����������Ϣ��ӵ�alarmManager�У�ͬʱ���浽Map��
    	//����ʹ��ʱ��Ҫ�Ƚ���������Insert��DB�У�Ȼ���ٵ��ø÷���
    	//Insertʱ��������ص����Կ�������Ϊ��Ч����������Update
    }
    
    public	void	alarmAlert( long _id )
    {
    	//һ�����ѵ�ʱ�̵���ʱ���ø÷���
    	//���ȸ���intent�б����ID��HashMap���ҵ�������¼���ж���������
    	//����ǵ��������򽫸�����Ϣ��HashMap��ɾ����������DB��Isable��ΪFalse
    	//�����ѭ��������������
    }
    
    public	void	editRemind( long _id, CRemindInfo _clCRemindInfo )
    {
    	//��һ�����ѽ��б༭ʱ���ø÷�����
    	//�뽫�����Ѵ�alarmManager��HashMap��ɾ����Ȼ�����addRemind����
    }
    
   
    public	void	disableRemind( long _id )
    {
    	//��һ����������Ϊ��Ч
    	//�����HashMap��alarmManager��ɾ��������DB�е�IsAble����Ϊ��Ч
    	//�������Ѳ���תΪMemo�����Ը÷�����ɾ��������ΪDisable������
    }
    
    public	void	getRemindInfo( long _id, CRemindInfo _clCRemindInfo )
    {
    	//����ID��DB��ȡ��������Ϣ
    }
    
}
