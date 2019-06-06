****Открытие таблицы
FUNCTION openTable(PtableName)


SELECT nastroj1
GO TOP 
LOCATE FOR UPPER(ALLTRIM(tablename))==UPPER(ALLTRIM(PtableName))
IF FOUND()
   m.OpenThisTable = .T.
   m.OnError = ON('ERROR')                     && Сохраняем текущий обработчик ошибок
   ON ERROR DO NewErr WITH ERROR(), MESS()     && Устанавливаем новый обработчик ошибок

   FileDostup = ALLTRIM(dostup)
   *FileName = ALLTRIM(TablePath)+ALLTRIM(tablename)
   *-------------------------------------------------
   FileName = ALLTRIM(TablePath)+IIF(AT("XIM",ALLTRIM(tablename))>0,LEFT(ALLTRIM(tablename),AT("XIM",ALLTRIM(tablename))-1), ALLTRIM(tablename))  
   *-------------------------------------------------
     
   SELECT 0
   use (FileName) &FileDostup

   ON ERROR &OnError                           && Восстанавливаем обработчик ошибок
   IF NOT m.OpenThisTable            && не смогли открыть shared (значит кто-то занял EXCL)
      =MESSAGEBOX("Исходная таблица " + FileName + " занята <МОНОПОЛЬНО>!" +CHR(13)+ " " + CHR(13) + "Повторите попытку позже...", 48, 'Предупреждение')   
      RETURN .F.
   ENDIF        

*!*	   IF nastroj1.codepage <> 1251 AND CPDBF()=0
*!*	  =SetCP866(DBF()) && Для установки 866 кодовой таблицы
*!*	   ENDIF    

   IF nastroj1.tableOrder
      TekAliasOpen = ALIAS()
      SELECT nastroj2
      SET FILTER TO id_1=nastroj1.id
      GO top
      FileOrderName = ALLTRIM(nameorder)
      SELECT (TekAliasOpen)
      SET ORDER TO &FileOrderName
   ENDIF
   
*ON ERROR &OnError                           && Восстанавливаем обработчик ошибок   
ON ERROR                            && Восстанавливаем обработчик ошибок   
   RETURN .T.    
ELSE 
*!*	   =MESSAGEBOX("Нельзя открыть таблицу ( " + ALLTRIM(PtableName)+".DBF )!" + CHR(13) + " " + CHR(13) + ;
*!*	      "Нет ссылки в таблице <nastroj1.dbf> " + CHR(13) + " " + CHR(13) + "Обратитесь в УИТ, тел. 32-18 (36-49, 31-00)",0+16)
ON ERROR                            && Восстанавливаем обработчик ошибок   
   RETURN .F.
ENDIF    


*!*	*==========================================================================*
*!*	PROCEDURE NewErr
*!*	*--------------------------------------------------------------------------*
*!*	PARAM pNumError, pMessErr

*!*	m.OpenThisTable = .F.  && Вызван обработчик ошибки... поэтому флаг в "ЛОЖЬ"

*!*	RETURN
*!*	*==========================================================================*
