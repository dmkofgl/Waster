PROCEDURE AddLog

PARAMETERS vWorkArea, vTableName, vAction, vRecNo, vNotes

SELECT logs

RLOCK()
  APPEND BLANK
  REPLACE UserID WITH vUserID, ;
          Date WITH DATETIME(), ;
          TableName WITH vTableName, ;
          Action WITH vAction, ;
          RecordNum WITH vRecNo, ;
          Notes WITH vNotes
UNLOCK

SELECT &vWorkArea

ENDPROC