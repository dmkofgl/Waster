***** процедура формирования временного файла подгрупп ткани для план-прогноза
***** на основании podgrpeo.dbf
FUNCTION Vrempodgrokrb


m.OpenSkNaimF = SELECT("SkNaim")
IF m.OpenSkNaimF = 0 
   IF NOT opentable("sknaim")
      RETURN .F.
   ENDIF 
ENDIF 

USE IN SELECT("TmppodgrOkrb")
lcNameFile = PathTMP + "TmppodgrOkrb"
SELE 0
CREATE DBF (lcNameFile) ;      
       (number     N(10),;
        kod        C(8),;
        kodnaim    C(100))
INDEX on kod TAG Tmppodgr

SELECT sknaim
GO top
SCAN
   m.KolObGrupp = OCCURS(',', kod)   && кол-во запятых
   m.kod = ALLTRIM(kod)
   IF m.KolObGrupp >  0
      FOR jj=1 TO m.KolObGrupp 
          IF jj = 1
             m.Nachalo = RAT(",",m.kod,jj)+1
             m.Skolko = LEN(m.kod) - (m.Nachalo-1)
          ELSE
             m.Nachalo = RAT(",",m.kod,jj)+1
             m.Skolko = RAT(",",m.kod,jj-1) - RAT(",",m.kod,jj)-1
          ENDIF 
          m.AddKod = ALLTRIM(SUBSTR(m.kod,m.Nachalo,m.Skolko))
          SELECT TmppodgrOkrb
          APPEND BLANK 
          REPLACE number  WITH sknaim.number,;
                  kodnaim WITH sknaim.kodnaim,;
                  kod     WITH m.AddKod

      NEXT 
      m.AddKod = ALLTRIM(SUBSTR(m.kod,1,AT(",",m.kod)-1))                
      SELECT TmppodgrOkrb
      APPEND BLANK 
      REPLACE number  WITH sknaim.number,;
              kodnaim WITH sknaim.kodnaim,;
              kod     WITH m.AddKod
   ELSE 
      m.AddKod = ALLTRIM(m.kod)
      SELECT TmppodgrOkrb
      APPEND BLANK 
      REPLACE number  WITH sknaim.number
      REPLACE kodnaim WITH sknaim.kodnaim
      REPLACE kod     WITH m.AddKod
              
   ENDIF       
   SELECT sknaim
ENDSCAN 
IF m.OpenSkNaimF = 0
   USE IN SELECT("sknaim")
ENDIF 
RETURN .T.   