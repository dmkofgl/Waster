*** ѕропись мес€ц-год
FUNCTION PropisMesGod(ParamMesGod)

m.StrPropis = ''
IF !EMPTY(ParamMesGod)
   m.ParamMes = LEFT(ALLTRIM(ParamMesGod),2)
   m.ParamGod = RIGHT(ALLTRIM(ParamMesGod),4)
   DO CASE
      CASE m.ParamMes = '01'
           m.StrPropis = '€нварь'
      CASE m.ParamMes = '02'
           m.StrPropis = 'февраль'
      CASE m.ParamMes = '03'
           m.StrPropis = 'март'
      CASE m.ParamMes = '04'
           m.StrPropis = 'апрель'
      CASE m.ParamMes = '05'
           m.StrPropis = 'май'
      CASE m.ParamMes = '06'
           m.StrPropis = 'июнь'
      CASE m.ParamMes = '07'
           m.StrPropis = 'июль'
      CASE m.ParamMes = '08'
           m.StrPropis = 'август'
      CASE m.ParamMes = '09'
           m.StrPropis = 'сент€брь'
      CASE m.ParamMes = '10'
           m.StrPropis = 'окт€брь'
      CASE m.ParamMes = '11'
           m.StrPropis = 'но€брь'
      CASE m.ParamMes = '12'
           m.StrPropis = 'декабрь'
   ENDCASE
   m.StrPropis = m.StrPropis + '  ' + m.ParamGod + 'г.'
ENDIF    
RETURN m.StrPropis

