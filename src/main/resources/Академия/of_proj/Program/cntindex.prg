*==========================================================================*
*procedure CntIndex
*--------------------------------------------------------------------------*
PARAM pNomerRecn, pAlias
*
* ��ࠬ����:  pNomerRecn - ����� �����, �� ������ �� ������
*                          ������樮��஢����� � ᯨ᪥ �� �ଥ....

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
