// WidgetClock.cpp : ���� DLL Ӧ�ó������ڵ㡣
//

#include "ClockWidget.h"
#include <windows.h>

#include "WidgetTest.h"


UiWidget* CreateWidgetInstance( void* lpVoid )
{
    UiWidget* pWidget = new UiWidget_Clock;
    return pWidget;
}


BOOL APIENTRY DllMain( HANDLE hModule, 
                      DWORD  ul_reason_for_call, 
                      LPVOID lpReserved
                      )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}
