*** ������� �����-���
FUNCTION PropisMesGod(ParamMesGod)

m.StrPropis = ''
IF !EMPTY(ParamMesGod)
   m.ParamMes = LEFT(ALLTRIM(ParamMesGod),2)
   m.ParamGod = RIGHT(ALLTRIM(ParamMesGod),4)
   DO CASE
      CASE m.ParamMes = '01'
           m.StrPropis = '������'
      CASE m.ParamMes = '02'
           m.StrPropis = '�������'
      CASE m.ParamMes = '03'
           m.StrPropis = '����'
      CASE m.ParamMes = '04'
           m.StrPropis = '������'
      CASE m.ParamMes = '05'
           m.StrPropis = '���'
      CASE m.ParamMes = '06'
           m.StrPropis = '����'
      CASE m.ParamMes = '07'
           m.StrPropis = '����'
      CASE m.ParamMes = '08'
           m.StrPropis = '������'
      CASE m.ParamMes = '09'
           m.StrPropis = '��������'
      CASE m.ParamMes = '10'
           m.StrPropis = '�������'
      CASE m.ParamMes = '11'
           m.StrPropis = '������'
      CASE m.ParamMes = '12'
           m.StrPropis = '�������'
   ENDCASE
   m.StrPropis = m.StrPropis + '  ' + m.ParamGod + '�.'
ENDIF    
RETURN m.StrPropis

