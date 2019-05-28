*==========================================================================*
*procedure CntIndex
*--------------------------------------------------------------------------*
PARAM pNomerRecn, pAlias
*
* Параметры:  pNomerRecn - номер записи, на которую мы должны
*                          запозиционироваться в списке на форме....

IF NOT EMPTY(pAlias)
   PRIVATE m.CurrAlias
   m.CurrAlias = ALIAS()
   SELECT (pAlias)
ENDIF

GO TOP
COUNT WHILE RECNO() <> pNomerRecn TO m.CntStep

IF NOT EMPTY(pAlias)
   IF NOT EMPTY(m.CurrAlias)
      SELECT (m.CurrAlias)
   ENDIF
ENDIF

RETU m.CntStep + 1

*End CntIndex
*==========================================================================*
