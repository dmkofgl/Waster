****�������� �������
FUNCTION openTable(PtableName)


SELECT nastroj1
GO TOP 
LOCATE FOR UPPER(ALLTRIM(tablename))==UPPER(ALLTRIM(PtableName))
IF FOUND()
   m.OpenThisTable = .T.
   m.OnError = ON('ERROR')                     && ��������� ������� ���������� ������
   ON ERROR DO NewErr WITH ERROR(), MESS()     && ������������� ����� ���������� ������

   FileDostup = ALLTRIM(dostup)
   *FileName = ALLTRIM(TablePath)+ALLTRIM(tablename)
   *-------------------------------------------------
   FileName = ALLTRIM(TablePath)+IIF(AT("XIM",ALLTRIM(tablename))>0,LEFT(ALLTRIM(tablename),AT("XIM",ALLTRIM(tablename))-1), ALLTRIM(tablename))  
   *-------------------------------------------------
     
   SELECT 0
   use (FileName) &FileDostup

   ON ERROR &OnError                           && ��������������� ���������� ������
   IF NOT m.OpenThisTable            && �� ������ ������� shared (������ ���-�� ����� EXCL)
      =MESSAGEBOX("�������� ������� " + FileName + " ������ <����������>!" +CHR(13)+ " " + CHR(13) + "��������� ������� �����...", 48, '��������������')   
      RETURN .F.
   ENDIF        

*!*	   IF nastroj1.codepage <> 1251 AND CPDBF()=0
*!*	  =SetCP866(DBF()) && ��� ��������� 866 ������� �������
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
   
*ON ERROR &OnError                           && ��������������� ���������� ������   
ON ERROR                            && ��������������� ���������� ������   
   RETURN .T.    
ELSE 
*!*	   =MESSAGEBOX("������ ������� ������� ( " + ALLTRIM(PtableName)+".DBF )!" + CHR(13) + " " + CHR(13) + ;
*!*	      "��� ������ � ������� <nastroj1.dbf> " + CHR(13) + " " + CHR(13) + "���������� � ���, ���. 32-18 (36-49, 31-00)",0+16)
ON ERROR                            && ��������������� ���������� ������   
   RETURN .F.
ENDIF    


*!*	*==========================================================================*
*!*	PROCEDURE NewErr
*!*	*--------------------------------------------------------------------------*
*!*	PARAM pNumError, pMessErr

*!*	m.OpenThisTable = .F.  && ������ ���������� ������... ������� ���� � "����"

*!*	RETURN
*!*	*==========================================================================*
