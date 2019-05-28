PROCEDURE OpenSetTable
PARAMETERS v_AliasAgain

SELECT nastroj1

IF nastroj1.codepage <> 1251
  =SetCP866(DBF()) && Для установки 866 кодовой таблицы
ENDIF    

AliasName = ALLTRIM(tablename)
FileDostup = ALLTRIM(dostup)
FileName = ALLTRIM(TablePath) + ALLTRIM(tablename)

IF v_AliasAgain == ""
  IF NOT USED(AliasName)
    SELECT 0
    USE (FileName) ALIAS &AliasName &FileDostup
  ELSE 
    SELECT &AliasName
  ENDIF  
ELSE
  IF USED(AliasName)
    SELECT 0
    USE (FileName) AGAIN ALIAS &v_AliasAgain &FileDostup
  ELSE 
    SELECT 0
    USE (FileName) ALIAS &v_AliasAgain &FileDostup
  ENDIF    
ENDIF

IF nastroj1.tableOrder
  TekAliasOpen = ALIAS()
  
  SELECT nastroj2
  SET FILTER TO id_1 = nastroj1.id
  GO top
  FileOrderName = ALLTRIM(nameorder)
  SELECT (TekAliasOpen)
  SET ORDER TO &FileOrderName
  
ENDIF    
    
SELECT nastroj1

ENDPROC

***************************

PROCEDURE OpenTablesByStart()

SELECT nastroj1

GO TOP
SCAN
  IF oneOpen
    DO OpenSetTable WITH ""
  ENDIF    
ENDSCAN

ENDPROC

***************************

PROCEDURE OpenTableByName
PARAMETERS TN

SELECT nastroj1

GO TOP

LOCATE FOR ALLTRIM(UPPER(nastroj1.Tablename)) == ALLTRIM(UPPER(TN))

IF FOUND()
  DO OpenSetTable WITH ""
ENDIF

ENDPROC

***************************

PROCEDURE OpenTableByNameAgain
PARAMETERS TN, v_AliasAgain

SELECT nastroj1

GO TOP
LOCATE FOR ALLTRIM(UPPER(nastroj1.Tablename)) = UPPER(TN)

IF FOUND()
  DO OpenSetTable WITH v_AliasAgain
ENDIF

ENDPROC
