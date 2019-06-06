**** Работа со справочниками
**** nameForm - имя вызываемой формы
**** aliasForm - алиас основной таблицы
**** orderForm - рабочий ордер для основной таблицы

PARAMETERS nameForm, aliasForm, orderForm

m.TekAliasDo = ALIAS()
m.TekRecnoDo = IIF(EOF(),0,RECNO())
m.TekOrderDo = ORDER()

SELECT (aliasForm)
IF !EMPTY(orderForm)
   SET ORDER TO (orderForm)
ENDIF    
GO TOP 

DO FORM &nameForm

SELECT (m.TekAliasDo)
IF !EMPTY(m.TekOrderDo)
   SET ORDER TO (m.TekOrderDo)
ENDIF    
IF m.TekRecnoDo = 0
   GO TOP 
ELSE
   GO m.TekRecnoDo
ENDIF 
retu
