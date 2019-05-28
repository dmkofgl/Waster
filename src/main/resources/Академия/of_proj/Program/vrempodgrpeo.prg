***** процедура формирования временного файла подгрупп ткани для план-прогноза
***** на основании podgrpeo.dbf
FUNCTION Vrempodgrpeo

USE IN SELECT("Tmppodgrpeo")
lcNameFile = PathTMP + "Tmppodgrpeo"
SELE 0
CREATE DBF (lcNameFile) ;      
       (number     N(4),;
        kptob1     N(2),;
        nkptob1    C(60),;
        vibor      L)
INDEX on STR(kptob1) TAG Tmppodgr


SELECT podgrpeo
GO top
SCAN
   m.KolObGrupp = OCCURS(',', kptob1s)   && кол-во запятых
   m.kptob1s = ALLTRIM(kptob1s)
   IF m.KolObGrupp >  0
      FOR jj=1 TO m.KolObGrupp 
          IF jj = 1
             m.Nachalo = RAT(",",m.kptob1s,jj)+1
             m.Skolko = LEN(m.kptob1s) - (m.Nachalo-1)
          ELSE
             m.Nachalo = RAT(",",m.kptob1s,jj)+1
             m.Skolko = RAT(",",m.kptob1s,jj-1) - RAT(",",m.kptob1s,jj)-1
          ENDIF 
          m.AddKptob1 = INT(VAL(SUBSTR(m.kptob1s,m.Nachalo,m.Skolko)))
          SELECT Tmppodgrpeo
          APPEND BLANK 
          REPLACE number WITH podgrpeo.number,;
                  nkptob1 WITH podgrpeo.nkptob1,;
                  kptob1 WITH m.AddKptob1,;
                  vibor  WITH .F.
                  
      NEXT 
      m.AddKptob1 = INT(VAL(SUBSTR(m.kptob1s,1,AT(",",m.kptob1s)-1)))                
      SELECT Tmppodgrpeo
      APPEND BLANK 
      REPLACE number WITH podgrpeo.number,;
              nkptob1 WITH podgrpeo.nkptob1,;
              kptob1 WITH m.AddKptob1,;
              vibor  with .F.
   ELSE 
      m.AddKptob1 = INT(VAL(m.kptob1s))
      SELECT Tmppodgrpeo
      APPEND BLANK 
      REPLACE number WITH podgrpeo.number,;
              nkptob1 WITH podgrpeo.nkptob1,;
              kptob1 WITH m.AddKptob1,;
              vibor  WITH .F.             
   ENDIF     
   
   SELECT podgrpeo            
ENDSCAN 

RETURN .T.   