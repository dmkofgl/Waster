PROCEDURE UpdateChemicals

TableChemicals = "Chemicals.dbf"

SELECT 0
IF FILE(vTmpDirectory + TableChemicals)  
  USE (vTmpDirectory + TableChemicals) EXCLUSIVE ALIAS "Chemicals"
ELSE 
  CREATE DBF (vTmpDirectory + TableChemicals) ;      
          (KOD_O C(6), NAIM C(50))     
  INDEX ON KOD_O TAG IDX_KOD_O
  INDEX ON UPPER(NAIM) TAG IDX_NAIM
ENDIF

SQLConnection = SQLCONNECT('MogotexBase','chemical','werfh')

IF SQLConnection > 0
  qrySelect = 'SELECT * FROM sc47213'
  =SQLPREPARE(SQLConnection, qrySelect, 'TmpChemicals')
  =SQLEXEC(SQLConnection)
  =SQLDISCONNECT(SQLConnection)

  SELECT Chemicals
  ZAP
  
  SELECT TmpChemicals
  m.CountRecords = RECCOUNT()
  IF m.CountRecords > 0
  
    GO top
    SCAN
      SELECT Chemicals
      APPEND BLANK 
      REPLACE kod_o WITH TmpChemicals.code, naim WITH TmpChemicals.descr
      SELECT TmpChemicals
    ENDSCAN
    
    SELECT Chemicals
    REINDEX 
           
    =MESSAGEBOX("���������� �������������� ��������� ��������� ������� !!! " + CHR(13) + " " + "���������� ������� = "+ALLTRIM(STR(m.CountRecords)), 0 + 64)         
  
  ELSE
    =MESSAGEBOX("���������� �������������� ���������� �������� !!! " + CHR(13) + "��� ������ " + CHR(13) + "���������� � ���, ���. 32-18 (36-49, 31-00)", 0 + 16)         
  ENDIF  
ELSE
  =MESSAGEBOX("��� ����� � ����� 1� !!!" + CHR(13) + "��������� ������� ����� !", 0 + 16)   
ENDIF

SELECT Chemicals
USE
    
ENDPROC