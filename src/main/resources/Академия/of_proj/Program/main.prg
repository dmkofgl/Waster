SET TALK OFF
SET HELP OFF
SET DELETE ON
SET EXCL OFF
SET MULTI ON
SET STAT BAR OFF
SET DATE GERMAN 
SET CENT ON
SET CONS OFF
SET MEMOWIDTH TO 100
SET NOTIF OFF
SET ODOMETER TO 999999999
SET TALK NOWIND
SET SYSMENU Off
SET ESCAPE OFF 
SET TABLEVALIDATE TO 0
**************************
        SET BELL OFF
        SET INTENSITY OFF
        SET CONFIRM On
        SET HOURS TO 24
        SET SECONDS OFF
        SET FIXED OFF
        SET REPROCESS TO -2
        SET DECIMALS TO 3
        SET SAFETY OFF
**************************
CLEAR  MACROS
CLOSE  ALL
CLEAR 
SET AUTOINCERROR OFF
SET REPROC TO 1  &&SECOND


DECLARE INTEGER LoadKeyboardLayout IN win32api STRING cp1, INTEGER np2
=LoadKeyboardLayout("00000419", 1)  && Русская раскладка
*=LoadKeyboardLayout("00000409", 1)  && Английская
*----------------------------------------------------------------------------------------
USE nastroj1 SHARED IN 0
SELECT 0
USE nastroj2 SHARED
SET ORDER TO NASTROJ2   && STR(ID_1)+STR(PN) 

PathTMP = 'D:\NabivProject_new_i\tmp\'

*--------------------------------
IF !DIRECTORY(PathTMP)&& проверка наличия каталога для месяца
   mkdir(PathTMP)&& создание каталога для месяца
ENDIF

**************** для NewViev() при выводе на экран (внешний просмотрщик)
IF vers(5) = 700
   m.tmpSet = 'SET ENGINEBEHAVIOR 70' 
   &tmpSet
ELSE
   IF vers(5) = 800

       m.tmpSet = 'SET ENGINEBEHAVIOR 70' 
      &tmpSet

      m.tmpSet = 'SET TABLEVALIDATE TO 0' 
      &tmpSet

   ELSE 
      IF vers(5) = 900
          m.tmpSet = 'SET ENGINEBEHAVIOR 70' 
          &tmpSet

         m.tmpSet = 'SET TABLEVALIDATE TO 0' 
         &tmpSet
      ENDIF
   ENDIF
ENDIF


*******************************************************
* Для установки 866-й таблицы для клиперовских файлов !!!
*!*	decl integer SetCP866 in cpTOOLS.DLL STRING FileName
*******************************************************

****-------------открытие таблиц   

SELECT nastroj1
GO top

SCAN
   IF oneOpen
      FileDostup = ALLTRIM(dostup)
      FileName = ALLTRIM(TablePath)+ALLTRIM(tablename)
      SELECT 0
      use (FileName) &FileDostup
*!*	      IF nastroj1.codepage = 866 && AND CPDBF()=0
*!*	         =SetCP866(DBF()) && Для установки 866 кодовой таблицы
*!*	      ENDIF    

      IF nastroj1.tableOrder
         TekAliasOpen = ALIAS()
         SELECT nastroj2
         SET FILTER TO id_1=nastroj1.id
         GO top
         FileOrderName = ALLTRIM(nameorder)
         SELECT (TekAliasOpen)
         SET ORDER TO &FileOrderName
      ENDIF    
      SELECT nastroj1
   ENDIF    
ENDSCAN


*-------------------------------------------------------
ON SHUTDOWN DO SHT IN MAIN

m.Vxod = .F.
TekIdUser = 0
TekidCategory = 0
TekSeansData = ''



m.parol = ''
_SCREEN.WindowState = 2   && Окно развернуто до границ полного экрана
_screen.backcolor = rgb(150,150,150)
_SCREEN.Caption = 'Учет расхода химикатов и красителей (участок набивки ОП-1) '

****Test_17_08_2011 *********
*DO MENU1.MPR
****************************

DO FORM fvxod
initstart(.T.)  
READ EVENTS
SELECT user
SET FILTER TO 
*DO obnovskart.prg

TekIdUser = user.id    && идентификатор текущего пользователя
TekidCategory = user.idcategory
**---- m.idCategory = 1 - admin
**---- m.idCategory = 2 - плановик

TekSeansData = ''
m.VxodData = .F.
m.god = ''
m.TekMonth = ''

IF m.Vxod 
      DO MENU1.MPR
      READ EVENTS
ENDIF 

RELEASE ALL
CLEAR ALL
SET HELP ON
SET SYSMENU TO DEFA
CLOSE ALL
initstart(.F.)


FUNCTION initstart
LPARAMETERS lkey
IF VERSION(2) = 0
        RETURN
ENDIF
LOCAL i
IF lkey
        _screen.AddProperty("awin[11,2]")
        _screen.awin[ 1,1]="COLOR PALETTE"
        _screen.awin[ 2,1]="DATABASE DESIGNER"
        _screen.awin[ 3,1]="FORM CONTROLS"
        _screen.awin[ 4,1]="FORM DESIGNER"
        _screen.awin[ 5,1]="LAYOUT"
        _screen.awin[ 6,1]="PRINT PREVIEW"
        _screen.awin[ 7,1]="QUERY DESIGNER"
        _screen.awin[ 8,1]="REPORT CONTROLS"
        _screen.awin[ 9,1]="REPORT DESIGNER"
        _screen.awin[10,1]="STANDARD"
        _screen.awin[11,1]="VIEW DESIGNER"
        FOR i=1 TO 11
                _screen.awin[i,2]=WEXIST(_screen.awin[i,1])
                DEACTIVATE WINDOW(_screen.awin[i,1])            
        ENDFOR
ELSE
        FOR i=1 TO 11
                IF _screen.awin[i,2] AND WEXIST(_screen.awin[i,1])
                        ACTIVATE WINDOW(_screen.awin[i,1])
        ENDIF
        ENDFOR
ENDIF
*
PROCEDURE SHT
        CLEAR EVENTS
        CLEAR MEMORY
        CLEAR DLLS
        QUIT
        RETURN
*