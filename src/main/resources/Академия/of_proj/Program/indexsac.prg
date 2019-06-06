**** Процедура индексации таблиц

CLOSE DATABASES

USE nastroj1 SHARED IN 0
SELECT 0
USE nastroj2 SHARED 
SET ORDER TO NASTROJ2   && STR(ID_1,10)+STR(PN,2) 

SELECT nastroj1
SET FILTER TO TableOrder
GO top
jj = 0

m.OpenThisTable = .T.       && возврат из newerr.prg
m.OpenThisTableOsn = .T.    && флаг открытия основной таблицы (которую индексируем)
m.OpenThisTableDele = .T.   && флаг открытия вспомогательной таблицы (где сохраняем помеченные на удаление записи)

ON ERROR DO NewErr WITH ERROR(), MESS()     && Устанавливаем новый обработчик ошибок

***
DIMENSION arrTalon[7]&& Массив наименований БД связанных с Talon.dbf
arrTalon[1]="talonsm"
arrTalon[2]="talontf"
arrTalon[3]="talontbc"
arrTalon[4]="talonpor"
arrTalon[5]="HstTalonSm"
arrTalon[6]="HstTalonPor"
arrTalon[7]="HstTalonTBC"

DIMENSION arrPathT[7]&& Массив путей к БД связанных с Talon.dbf

SCAN 
   jj = jj + 1
   m.id = id
   FileName = ALLTRIM(TablePath)+ALLTRIM(tablename)
   m.copydel = copydel
   WAIT "Идет индексация таблицы " + UPPER(ALLTRIM(tablename))+'.DBF' WINDOW NOWAIT    
    
   DIMENSION AlistIndex(jj,4)
   AlistIndex(jj,1) = ALLTRIM(tablename)
   AlistIndex(jj,2) = ALLTRIM(tablepath)
   AlistIndex(jj,3) = .F.   && открытие основной таблицы
   AlistIndex(jj,4) = .F.   && .T. - либо сохранять удаленные записи не надо, либо сохранили успешно
                            && .F. - сохранить удаленные записи надо, но не смогли
   m.OpenThisTable = .T.
   m.OnError = ON('ERROR')                     && Сохраняем текущий обработчик ошибок

   SELECT 0
   use (FileName) excl
   m.OpenThisTableOsn = m.OpenThisTable   && флаг открытия основной таблицы (которую индексируем)
 
   IF m.OpenThisTableOsn            && не смогли открыть excl (значит кто-то занял )
      TekIndexAlias = ALIAS()
      AlistIndex(jj,3) = .T.
      m.aliasCopyDel = "" 

      **** pack + index делаем, если смогли открыть excl основную таблицу и таблицу для сохранения помеченных на удаление записей
      ****(в случае если nastroj.copydel для основной таблицы = .T.) 
      IF m.copydel 
         FileNameDele = ALLTRIM(nastroj1.TablePath)+'DELE'+ALLTRIM(nastroj1.tablename)
         m.aliasCopyDel = "DELE" + ALLTRIM(nastroj1.tablename)   
         **** создание структуры таблицы для сохранения удаленных записей (в случае ее отсутствия)
         IF NOT FILE(FileNameDele+'.dbf')  
            copystrudel = FileNameDele+'.dbf'
            COPY STRUCTURE TO (copystrudel)
         ENDIF    
         ****
         SELECT 0
         USE (FileNameDele) EXCLUSIVE 
         m.OpenThisTableDele = m.OpenThisTable && флаг открытия вспомогательной таблицы (где сохраняем помеченные на удаление записи)
         IF m.OpenThisTableDele
            **** перенос помеченных на удаление записей
            SET DELETED OFF 
            SELECT (m.aliasCopyDel)
            commandAppendDele = 'APPEND FROM ' + FileName + '.DBF FOR DELETED()'
            &commandAppendDele

            SET DELETED ON 
            SELECT (TekIndexAlias)
         ENDIF    
         m.OpenThisTableDele = m.OpenThisTable
      ENDIF    
      AlistIndex(jj,4) = m.OpenThisTableDele
      **** Если перенос удаленных записей прошел успешно или сохранять удаленные записи не надо
      **** в случае, если сохранять удаленные записи не надо (т.е. если nastroj.copydel=.F., то m.OpenThisTableDele всегда .T.)
      IF m.OpenThisTableDele
         SELECT (TekIndexAlias)
         PACK    
      
         SELECT nastroj2
         SET FILTER TO id_1 = m.id
         GO top
         SCAN
           NKeyOrder = ALLTRIM(keyOrder)
           NOrderNaim = ALLTRIM(nameOrder)
           IF MinMaxSort  && сортировка по убыванию
              NOrderNaim = NOrderNaim + " DESCENDING"
           ENDIF    
           SELECT (TekIndexAlias)
           DELETE TAG &NOrderNaim
           INDEX on &NKeyOrder TAG &NOrderNaim
           SELECT nastroj2
         ENDSCAN
         SET FILTER TO 
         USE IN SELECT(TekIndexAlias) 
         IF m.copyDel  
            USE IN SELECT(m.aliasCopyDel)
         ENDIF    
      ENDIF    
   ENDIF    

   WAIT CLEAR 
   SELECT nastroj1

ENDSCAN               
ON ERROR       && &OnError                           && Восстанавливаем обработчик ошибок


m.CountNotIndex = 0
FOR jj=1 TO ALEN(AlistIndex,1)
    IF NOT AlistIndex(jj,3) OR NOT AlistIndex(jj,4)
       m.CountNotIndex = m.CountNotIndex + 1
       DIMENSION AlistIndexSpis(m.CountNotIndex)
       AlistIndexSpis(m.CountNotIndex) = PADR(UPPER(ALLTRIM(AlistIndex(jj,1)))+'.DBF',20)+'       '+ALLTRIM(AlistIndex(jj,2))
       IF AlistIndex(jj,3) AND NOT AlistIndex(jj,4)   && проблемы с переносом удаленных записей
          AlistIndexSpis(m.CountNotIndex) = AlistIndexSpis(m.CountNotIndex) + '  (проблемы с сохранением удаленных записей)'
       ENDIF 
    ENDIF    
NEXT
IF m.CountNotIndex > 0
   DO FORM rezultatindex.scx
ELSE
   MESSAGEBOX("Индексация завершена успешно !",0+64)   
ENDIF    

******* востанавливаем открытие необходимых таблиц   
CLOSE DATABASES

USE nastroj1 SHARED IN 0
SELECT 0
USE nastroj2 SHARED 
SET ORDER TO NASTROJ2   && STR(ID_1,10)+STR(PN,2) 

SELECT nastroj1
GO top
SCAN
   IF oneOpen
      FileDostup = ALLTRIM(dostup)
      FileName = ALLTRIM(TablePath)+ALLTRIM(tablename)
      SELECT 0
      use (FileName) &FileDostup
*!*	      IF nastroj1.codepage <> 1251 AND CPDBF()=0
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

ON ERROR