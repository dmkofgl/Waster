*** Обновление данных из \\DEEPASS\APPL\CEN\BAZA1\sk_art.dbf, \\DEEPASS\APPL\USL\BAZA\sk_artu.dbf
***

USE IN SELECT("tmpArtAll")
m.ThisAlias = ALIAS()
lcNameFile1 = PathTMP + 'TmpArtALL.dbf'
lcNameFile2 = PathTMP + 'TmpArtALL.cdx'
DELETE FILE &lcNameFile1
DELETE FILE &lcNameFile2


USE IN SELECT("sk_art")
USE IN SELECT("sk_artu")
USE IN SELECT("k_grup")
USE IN SELECT("STAT1")
USE IN SELECT("PRILO1")
USE IN SELECT("sk_naim")

**** из-за потребности artdobav.dbf. открываем на прямую, т.к. там есть свой artdobav.dbf
IF SELECT("artdobav") <> 0
   USE IN SELECT("artdobav")
ENDIF

m.OpenKlient = SELECT("klient")
IF m.OpenKlient = 0
   =openTable("klient")
ENDIF    

SELECT 0
*USE \\deepass\APPL\ximikat\baza\artdobav
USE D:\NabivProject_new_i\1\artdobav
SET ORDER TO ARTDOB1   && ZNACH1+STR(LENZNACH1)

m.OpenArtVrem = SELECT("ArtVrem")
IF m.OpenArtVrem = 0
   =OPENTABLE("artvrem")
ENDIF
m.OpenPodgrpeo = SELECT("podgrpeo")
IF m.OpenPodgrpeo = 0
   =OPENTABLE("podgrpeo")
ENDIF
*---------------------------------------------------------------------------------------------------------------------------
m.OnError = ON('ERROR')                     && Сохраняем текущий обработчик ошибок
ON ERROR DO NewErr WITH ERROR(), MESS()     && Устанавливаем новый обработчик ошибок
***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA1\sk_art.dbf
PathSkArt = ''
m.OpenSkArt = .F.
m.CpDbfSkArtDOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR ALLTRIM(tablename)=="sk_art"

IF FOUND()
   PathSkArt = ALLTRIM(tablepath)+'sk_art.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA1\sk_art.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathSkArt) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник артикулов SK_ART.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenSkArt = m.OpenThisTable
      m.CpDbfSkArtDOS = CPDBF()

   ENDIF
ENDIF
***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA1\k_grup.dbf
PathKGrup = ''
m.OpenKGrup = .F.
m.CpDbfKGrupDOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR ALLTRIM(tablename)=="k_grup"
IF FOUND()
   PathKGrup = ALLTRIM(tablepath)+'k_grup.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA1\k_grup.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathKGrup) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник подгрупп артикулов K_GRUP.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenKGrup = m.OpenThisTable
      m.CpDbfKGrupDOS = CPDBF()
   ENDIF
ENDIF
*ON ERROR &OnError                           && Восстанавливаем обработчик ошибок

***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA\stat1.dbf
PathStat1 = ''
m.OpenStat1 = .F.
m.CpDbfStat1DOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR ALLTRIM(tablename)=="stat1"
IF FOUND()
   PathStat1 = ALLTRIM(tablepath)+'stat1.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA\stat1.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathStat1) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник подгрупп артикулов STAT1.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenStat1 = m.OpenThisTable
      m.CpDbfStat1DOS = CPDBF()
   ENDIF
ENDIF
***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA\prilo1.dbf
PathPrilo1 = ''
m.OpenPrilo1 = .F.
m.CpDbfPrilo1DOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR ALLTRIM(tablename)=="prilo1"
IF FOUND()
   PathPrilo1 = ALLTRIM(tablepath)+'prilo1.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA\prilo1.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathPrilo1) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Таблица <Прейскурант> PRILO1.DBF (ПЭО) занята монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenPrilo1 = m.OpenThisTable
      m.CpDbfPrilo1DOS = CPDBF()
   ENDIF
ENDIF

***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA1\sk_naim.dbf
PathSkNaim = ''
m.OpenSkNaim = .F.
m.CpDbfSkNaimDOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR ALLTRIM(tablename)=="sk_naim"
IF FOUND()
   PathSkNaim = ALLTRIM(tablepath)+'sk_naim.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA1\sk_naim.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathSkNaim) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник подгрупп ОКРБ SK_NAIM.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenSkNaim = m.OpenThisTable
      m.CpDbfSkNaimDOS = CPDBF()
   ENDIF
ENDIF

***** открытие share таблицы \\DEEPASS\APPL\CEN\BAZA1\vidotd.dbf
PathVidotd = ''
m.OpenVidotd = .F.
m.CpDbfVidotdDOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR UPPER(ALLTRIM(tablename))=="VIDOTD"
IF FOUND()
   PathVidotd = ALLTRIM(tablepath)+'vidotd.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\CEN\BAZA1\vidotd.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathVidotd) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник видов отделки VIDOTD.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.CpDbfVidotdDOS = CPDBF()      
      COPY TO D:\NabivProject_new_i\tmp\vremvidotd.dbf
      USE IN SELECT("vidotd")
      USE D:\NabivProject_new_i\tmp\vremvidotd.dbf EXCLUSIVE
      IF m.CpDbfVidotdDOS = 0
         SCAN 
            REPLACE vid_otd WITH CPCONVERT(866,1251,vid_otd),;
                    naimotd WITH CPCONVERT(866,1251,naimotd)
         ENDSCAN 
      ENDIF     
      INDEX on vid_otd TAG vidotd
   ENDIF
ENDIF

ON ERROR      && &OnError                           && Восстанавливаем обработчик ошибок - по умолчанию

*---------------------------------------------------------------------------------------------------------------------------
*---------------------------------------------------------------------------------------------------------------------------
*---------------------------------------------------------------------------------------------------------------------------
***** открытие share таблицы \\DEEPASS\APPL\USL\BAZA\sk_artu.dbf
PathSkArtU = ''
m.OpenSkArtU = .F.
m.CpDbfSkArtUDOS = 0  && кодовая таблица: по умолчанию считаем =0
SELECT nastroj1
LOCATE FOR UPPER(ALLTRIM(tablename))=="SK_ARTU"

IF FOUND()
   PathSkArtU = ALLTRIM(tablepath)+'sk_artu.dbf'
   *****----- попытка открыть share таблицу \\DEEPASS\APPL\USL\BAZA\sk_artu.dbf
   m.OpenThisTable = .T.                 && По умолчанию - успешное открытие
   SELECT 0
   USE (PathSkArtU) SHARED                && Попытка открыть указанную таблицу
   IF NOT m.OpenThisTable  && не смогли открыть
      =MESSAGEBOX("Справочник артикулов SK_ARTU.DBF (ПЭО) занят монопольно! Повторите попытку позже...", 48, 'Предупреждение')
   ELSE
      m.OpenSkArtU = m.OpenThisTable
      m.CpDbfSkArtUDOS = CPDBF()

   ENDIF
ENDIF

***---------------------------------------------------------------------------------
*** справочник сокращенных наименований ОКРБ для печати итогов (в плане по готовым)
m.OpenSkNaimW = .F.
m.NowOpenSkNaimW = SELECT("sknaim")
IF m.NowOpenSkNaimW = 0
   SELECT nastroj1
   LOCATE FOR ALLTRIM(tablename)=="sknaim"
   IF FOUND()
      PathSkNaimW = ALLTRIM(tablepath)+'sknaim.dbf'
      IF NOT FILE(PathSkNaimW)
         SELE 0
         CREATE DBF (PathSkNaimW) ;
            (datatim    DT,;
            NUMBER     N(10),;
            kod        C(200),;
            kodnaim    C(100),;
            vibor      L )
         INDEX ON kod TAG sknaim
         INDEX ON STR(NUMBER) TAG sknaim1
         m.OpenSkNaimW = .T.
      ELSE
         IF OPENTABLE("sknaim")
            m.OpenSkNaimW = .T.
         ENDIF
      ENDIF
   ENDIF
ENDIF
SELECT sknaim
SET ORDER TO sknaim
=vrempodgrokrb()
*---------------------------------------------------------------------------------------------------------------------------
IF m.OpenSkArt AND m.OpenSkArtU AND m.OpenKGrup AND m.OpenStat1 AND m.OpenPrilo1 AND m.OpenSkNaim AND m.OpenSkNaimW

   WAIT "Ждите........ Идет обновление справочника артикулов...... "  WIND NOWA

   lcNameFile = PathTMP + 'TmpSkart'
   
   SELECT s.DATA, IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.art),s.art) AS art,;
      IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.art_obr),s.art_obr) AS artobr,;
      s.kpt_ob, IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.naim2),s.naim2) AS naim2,;
      IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.naim3),s.naim3) AS naim3,;
      IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.p_sod),s.p_sod) AS p_sod,;
      s.kpt_ob AS group_code, s.p_vl, s.p_ys, IIF(ISNULL(s.k_st1),0,s.k_st1) AS k_st1,IIF(ISNULL(s.k_st1),0,s.k_st2) AS k_st2,;
      IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.gn),s.gn) AS gn, s.pr_akt, s.shirg, s.vesg, s.kod, s.vespm, ;
      IIF(m.CpDbfSkNaimDOS = 0,CPCONVERT(866,1251,sn.naim),sn.naim) AS kodnaim, ;
      IIF(m.CpDbfSkArtDOS = 0, CPCONVERT(866,1251,s.vid_otd),s.vid_otd) AS vid_otd ;
      FROM sk_art s, sk_naim sn ;
      WHERE s.kod==sn.kod ;
      INTO TABLE (lcNameFile)
   INDEX ON art TAG TmpSkart
   SET ORDER TO TmpSkart

   USE IN SELECT("sk_art")
   *--------------------------------------------------------------------------------------------------------------------------
   SELECT IIF(m.CpDbfSkArtUDOS = 0, CPCONVERT(866,1251,s.art),s.art) AS art,;
          IIF(m.CpDbfSkArtUDOS = 0, CPCONVERT(866,1251,s.art_obr),s.art_obr) AS artobr,;
          s.kpt_ob, s.kpt_ob AS group_code, s.shirg, s.vesg, s.kod, s.vespm, s.kod_gr, k_name AS klient, ;
          IIF(m.CpDbfSkArtUDOS = 0, CPCONVERT(866,1251,s.naim2),s.naim2) AS naim2,;
          IIF(m.CpDbfSkNaimDOS = 0,CPCONVERT(866,1251,sn.naim),sn.naim) AS kodnaim ;
   FROM sk_artu s, sk_naim sn WHERE kpt_ob<>0 AND s.kod==sn.kod INTO CURSOR TmpSkartU

   USE IN SELECT("sk_artu")
   USE IN SELECT("sk_naim")
   *--------------------------------------------------------------------------------------------------------------------------   

   lcNameFile = PathTMP + 'TmpPrilo1'
   SELECT IIF(m.CpDbfPrilo1DOS = 0,CPCONVERT(866,1251,art),art) AS art,ocen,ocen_rus,IIF(m.CpDbfPrilo1DOS = 0,CPCONVERT(866,1251,pr_izm),pr_izm) AS pr_izm ;
      FROM prilo1 ;
      WHERE pr_izm='v' ;
      INTO TABLE (lcNameFile)
   SELECT TmpPrilo1
   INDEX ON art TAG TmpPrilo
   USE IN SELECT("PRILO1")
   **** обновление podgrpeo.dbf только для admin bkb плановик
   IF TekidCategory=1 OR TekidCategory=3
      =vrempodgrpeo()
      SELECT podgrpeo
      SET ORDER TO PODPEO   && STR(NUMBER)
      GO TOP
      IF EOF()
         m.FirstTimePodgrPeo = .T.
      ELSE
         m.FirstTimePodgrPeo = .F.
      ENDIF
   ENDIF
   ****-----------------------------------------------
   lcNameFile = PathTMP + 'TmpArtALL'
   SELE 0
   CREATE DBF (lcNameFile) ;
      (DATA       D(8),;
      obnov      T(8),;
      art        C(20),;
      artcp      C(20),;
      artcp1     C(20),;
      artobr     C(20),;
      prart      N(1),;        && 0-sk_ar, 1-sk_artu, 9 - artvrem
      kpt_ob     N(2),;
      kptob1     N(2),;
      nkptob1    C(60),;
      n_ob       C(15),;
      naim2      C(20),;
      naim3      C(25),;
      p_sod      C(30),;
      name_group C(60),;
      group_code N(2),;
      p_vl       N(5,2),;
      p_ys       N(6,2),;
      shirg      N(5,1),;
      vesg       N(6,1),;
      plan       N(6,1),;
      metrp      N(10,1),;
      metrd      N(6,1),;
      prcor      N(1),;
      pnartprice N(7),;
      k_st1      N(2),;
      name_st1   C(30),;
      namest1    C(10),;
      k_st2      N(2),;
      gn         C(1),;
      ocen       N(8),;
      ocen_rus   N(8,2),;
      pris       L(1),;
      got        L(1),;
      syr        L(1),;
      vespm      N(6,1),;
      kod        C(12),;
      kodnaim    M,;
      pr_akt     N(1),;
      otd        C(24),; 
      naimotd    M ,;
      klient     C(6),;
      kod1c      C(11),;
      naimkl     C(50),;
      knaimkl    C(25),;
      kvs        N(1) )


   INDEX ON name_group+art TAG tmpArtAll
   INDEX ON art TAG seekart
   INDEX ON artcp TAG artcp
   INDEX ON artcp1 TAG artcp1
   INDEX ON STR(kvs)+art TAG artusl
   SET ORDER TO tmpArtAll

   ** артикула Моготекс + "-д"
   SELECT TmpSkart
   GO TOP
   m.KolZapSkArt = 0
   m.datTimObnov = DATETIME()
   SCAN
      m.obnov = DATETIME()
      SELECT tmpArtAll

      APPEND BLANK
      REPLACE DATA       WITH TmpSkart.DATA,;
         obnov      WITH m.obnov,;
         art        WITH TmpSkart.art,;
         artobr     WITH TmpSkart.artobr,;
         prart      WITH 1,;
         kpt_ob     WITH TmpSkart.kpt_ob,;
         naim2      WITH TmpSkart.naim2,;
         naim3      WITH TmpSkart.naim3,;
         p_sod      WITH TmpSkart.p_sod,;
         group_code WITH TmpSkart.kpt_ob,;
         p_vl       WITH TmpSkart.p_vl,;
         p_ys       WITH TmpSkart.p_ys,;
         shirg      WITH TmpSkart.shirg,;
         vespm      WITH TmpSkart.vespm,;
         vesg       WITH TmpSkart.vesg,;
         k_st1      WITH TmpSkart.k_st1,;
         k_st2      WITH TmpSkart.k_st2,;
         gn         WITH TmpSkart.gn,;
         kod        WITH TmpSkart.kod,;
         kodnaim    WITH TmpSkart.kodnaim,;
         pr_akt     WITH TmpSkart.pr_akt,;
         otd        WITH TmpSkart.vid_otd
         
      IF SEEK(TmpSkart.vid_otd, "vremvidotd")
         REPLACE naimotd WITH vremvidotd.naimotd
      ENDIF    
      IF "-д" $ art
         REPLACE kvs WITH 2
      ENDIF    
         
      m.pozicKBorDP = 0
      DO CASE
      CASE "95-09" $ art
         m.pozicKBorDP = 5
      CASE "КВК" $ art
         m.pozicKBorDP = AT("КВ",art)+2
      CASE "КВ" $ art
         m.pozicKBorDP = AT("КВ",art)+1
      CASE "дп" $ art
         m.pozicKBorDP = AT("дп",art)+1
      ENDCASE
      m.artcp = art
      IF m.pozicKBorDP <> 0
         m.artcp = LEFT(art,m.pozicKBorDP)
      ENDIF
      REPLACE artcp WITH m.artcp
      *******************
      m.TAA = ALIAS()
      m.artcp1 = ALLTRIM(art)
      m.artcp1 = IIF(LEFT(art,5)=="95-09","95-09",m.artcp1)
      m.artforartcp1 = ALLTRIM(art)
      SELECT artdobav
      GO TOP
      SCAN
         m.pozic = AT(ALLTRIM(znach), ALLTRIM(m.artforartcp1))
         m.lenznach = lenznach
         IF m.pozic > 0
            m.artcp1 = PADR(LEFT(ALLTRIM(m.artforartcp1),m.pozic+m.lenznach-1),20)
            EXIT
         ENDIF
      ENDSCAN
      m.artcp1 = PADR(ALLTRIM(m.artcp1),20)
      SELECT (m.TAA)
      REPLACE artcp1 WITH m.artcp1
      *******************
      m.KolZapSkArt = m.KolZapSkArt + 1
      SELECT k_grup
      LOCATE FOR kpt_ob=tmpArtAll.kpt_ob
      IF FOUND()
         SELECT tmpArtAll
         REPLACE name_group WITH IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.NAME),k_grup.NAME),;
            n_ob WITH IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.n_ob),k_grup.n_ob)
      ELSE
         SELECT tmpArtAll
         REPLACE name_group WITH ""
      ENDIF

      SELECT stat1
      LOCATE FOR k_st1=tmpArtAll.k_st1
      IF FOUND()
         SELECT tmpArtAll
         REPLACE name_st1 WITH IIF(m.CpDbfStat1DOS = 0,CPCONVERT(866,1251,ALLTRIM(stat1.n_st1)+" "+ALLTRIM(stat1.n_podgr)),;
            ALLTRIM(stat1.n_st1)+" "+ALLTRIM(stat1.n_podgr)),;
            namest1 WITH IIF(m.CpDbfStat1DOS = 0,CPCONVERT(866,1251,ALLTRIM(stat1.n_podgr)),ALLTRIM(stat1.n_podgr))
      ELSE
         SELECT tmpArtAll
         REPLACE name_st1 WITH "", namest1 WITH ""
      ENDIF

      SELECT tmpArtAll
      IF SEEK(tmpArtAll.art,'TmpPrilo1')
         REPLACE ocen WITH TmpPrilo1.ocen, ocen_rus WITH TmpPrilo1.ocen_rus, pris WITH .T.
      ELSE
         REPLACE pris WITH .F.
      ENDIF
      IF NOT EMPTY(gn)
         REPLACE got WITH .T., syr WITH .F.
      ELSE
         REPLACE got WITH .F., syr WITH .T.
      ENDIF

      m.kod = LEFT(kod,8)
      m.kodnaim = kodnaim
      IF NOT SEEK(m.kod,"TmppodgrOkrb","Tmppodgr")
         SELECT TmppodgrOkrb
         APPEND BLANK
         REPLACE kod WITH m.kod
         SELECT sknaim
         APPEND BLANK
         REPLACE kod WITH m.kod, datatim WITH m.datTimObnov, kodnaim WITH m.kodnaim
      ENDIF
      SELECT TmpSkart
   ENDSCAN
   
   ** Услуги 
   SELECT TmpSkartU
   GO TOP
   SCAN
      m.obnov = DATETIME()
      SELECT tmpArtAll

      APPEND BLANK
      REPLACE DATA       WITH TmpSkart.DATA,;
         obnov      WITH m.obnov,;
         art        WITH TmpSkartU.art,;
         artobr     WITH TmpSkartU.artobr,;
         prart      WITH 1,;
         kpt_ob     WITH TmpSkartU.kpt_ob,;
         naim2      WITH TmpSkartU.naim2,;
         group_code WITH TmpSkartU.kpt_ob,;
         shirg      WITH TmpSkartU.shirg,;
         vespm      WITH TmpSkartU.vespm,;
         vesg       WITH TmpSkartU.vesg,;
         kod        WITH TmpSkartU.kod,;
         kodnaim    WITH TmpSkartU.kodnaim,;
         klient     WITH ALLTRIM(TmpSkartU.klient),;
         kvs        WITH 1,;
         got        WITH .T. 
         
      IF SEEK(PADR(ALLTRIM(TmpSkartU.klient),6), "klient", "KLIENT")
         REPLACE kod1c   WITH klient.kod1c,;
                 naimkl  WITH klient.naimkl,;
                 knaimkl WITH klient.knaimkl
      ENDIF    
      SELECT k_grup
      LOCATE FOR kpt_ob=tmpArtAll.kpt_ob
      IF FOUND()
         SELECT tmpArtAll
         REPLACE name_group WITH IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.NAME),k_grup.NAME),;
            n_ob WITH IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.n_ob),k_grup.n_ob)
      ELSE
         SELECT tmpArtAll
         REPLACE name_group WITH ""
      ENDIF

      m.kod = LEFT(kod,8)
      m.kodnaim = kodnaim
      IF NOT SEEK(m.kod,"TmppodgrOkrb","Tmppodgr")
         SELECT TmppodgrOkrb
         APPEND BLANK
         REPLACE kod WITH m.kod
         SELECT sknaim
         APPEND BLANK
         REPLACE kod WITH m.kod, datatim WITH m.datTimObnov, kodnaim WITH m.kodnaim
      ENDIF
      SELECT TmpSkartU
   ENDSCAN
   *-------------------------------------------------------------------------------------------
   **** заполнение специальных подгрупп для печати план-прогноза в ПЭО
   SELECT tmpArtAll
   SET ORDER TO artcp
   GO TOP
   DO WHILE NOT EOF()
      m.artcp = artcp
      m.kptob1 = 0
      m.nkptob1 = ""
      IF SEEK(m.artcp,"TmpSkArt") AND TmpSkart.art==m.artcp
         m.kptob1 = TmpSkart.kpt_ob
      ELSE
         m.kptob1 = tmpArtAll.kpt_ob
      ENDIF
      SELECT k_grup
      LOCATE FOR kpt_ob=m.kptob1
      IF FOUND()
         m.nkptob1 = IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.NAME),k_grup.NAME)
      ENDIF
      SELECT tmpArtAll
      DO WHILE NOT EOF() AND artcp==m.artcp
         REPLACE kptob1 WITH m.kptob1, nkptob1 WITH m.nkptob1
         SKIP
      ENDDO

      **** обновление podgrpeo.dbf только для admin bkb плановик
      IF TekidCategory=1 OR TekidCategory=3
         IF NOT SEEK(STR(m.kptob1),"TmpPodgrPeo") AND m.kptob1<>0
            SELECT Tmppodgrpeo
            APPEND BLANK
            REPLACE kptob1 WITH m.kptob1, nkptob1 WITH m.nkptob1
            SELECT podgrpeo
            GO BOTT
            IF BOF()
               m.number = 1
            ELSE
               m.number = NUMBER + 1
            ENDIF
            APPEND BLANK
            =RLOCK()
            REPLACE NUMBER  WITH m.number,;
               nkptob1 WITH m.nkptob1,;
               kptob1s WITH ALLTRIM(STR(m.kptob1,2)),;
               vibor   WITH .F.
            UNLOCK
         ENDIF
      ENDIF
      ****-------------------------------------------------------------
      SELECT tmpArtAll
   ENDDO
   **** обновление podgrpeo.dbf только для admin bkb плановик
   IF TekidCategory=1 OR TekidCategory=3
      **** если podgrpeo.dbf формируется впервые, то переопределяем number по nkptob1
      SELECT podgrpeo
      IF m.FirstTimePodgrPeo
         SET ORDER TO PODPEO1   && UPPER(NKPTOB1)
         m.number = 0
         GO TOP
         SCAN
            m.number = m.number + 1
            =RLOCK()
            REPLACE NUMBER WITH m.number
            UNLOCK
         ENDSCAN
      ENDIF
      SET ORDER TO PODPEO   && STR(NUMBER)
      GO TOP
   ENDIF
   ****--------------------------------------------------------------------
   **** переопределение нумерации для новых kod-ов в sknaim
   SELECT sknaim
   SET ORDER TO sknaim1
   GO BOTT
   m.number = NUMBER
   SET ORDER TO sknaim
   GO TOP
   SCAN
      IF NUMBER = 0
         m.number = m.number + 1
         =RLOCK()
         REPLACE NUMBER WITH m.number
         UNLOCK
      ENDIF
   ENDSCAN
   SET ORDER TO sknaim
   ****--------------------------------------------------------------------
   **** добавление артикулов из artvrem
   SELECT artvrem
   GO TOP
   SCAN
      IF NOT SEEK(artvrem.art,"tmpartall","seekart")
         m.pozicKBorDP = 0
         m.artcp = ""
         m.artcp1 = ""
         IF kvs <> 1
            DO CASE
            CASE "95-09" $ art
               m.pozicKBorDP = 5
            CASE "КВК" $ art
               m.pozicKBorDP = AT("КВ",art)+2
            CASE "КВ" $ art
               m.pozicKBorDP = AT("КВ",art)+1
            CASE "дп" $ art
               m.pozicKBorDP = AT("дп",art)+1
            ENDCASE
            m.artcp = art
            IF m.pozicKBorDP <> 0
               m.artcp = LEFT(art,m.pozicKBorDP)
            ENDIF
            REPLACE artcp WITH m.artcp
            *******************
            m.artcp1 = ALLTRIM(art)
            m.artcp1 = IIF(LEFT(art,5)=="95-09","95-09",m.artcp1)
            m.artforartcp1 = ALLTRIM(art)
            SELECT artdobav
            GO TOP
            SCAN
               m.pozic = AT(ALLTRIM(znach), ALLTRIM(m.artforartcp1))
               m.lenznach = lenznach
               IF m.pozic > 0
                  m.artcp1 = PADR(LEFT(ALLTRIM(m.artforartcp1),m.pozic+m.lenznach-1),20)
                  EXIT
               ENDIF
            ENDSCAN
            m.artcp1 = PADR(ALLTRIM(m.artcp1),20)
            SELECT artvrem
            REPLACE artcp1 WITH m.artcp1
         ENDIF    
         *******************
         SELECT artvrem
         SCATTER MEMVAR
         SELECT tmpArtAll
         APPEND BLANK
         GATHER MEMVAR
         REPLACE prart WITH 9

         SELECT k_grup
         LOCATE FOR kpt_ob=tmpArtAll.kpt_ob
         IF FOUND()
            SELECT tmpArtAll
            REPLACE n_ob WITH IIF(m.CpDbfKGrupDOS = 0,CPCONVERT(866,1251,k_grup.n_ob),k_grup.n_ob)
         ENDIF
         SELECT artvrem
         REPLACE artskart WITH .F.
      ELSE 
         REPLACE artskart WITH .T.   
      ENDIF
      SELECT artvrem
   ENDSCAN
   ****--------------------------------------------------------------------

   WAIT CLEAR

   SELECT tmpArtAll
   IF RECCOUNT() > 0      && считаем, что обновление прошло успешно
      =MESSAGEBOX("Обновление справочника артикулов ЗАВЕРШЕНО УСПЕШНО !!! " + CHR(13) + " " + "Количество записей = "+ALLTRIM(STR(m.KolZapSkArt)),0+64)
   ELSE
      =MESSAGEBOX("ВНИМАНИЕ !!!" + CHR(13) + "Обновление справочника артикулов ЗАВЕРШЕНО АВАРИЙНО !!! " + CHR(13) + "Повторите попытку",0+16)
   ENDIF

ENDIF

USE IN SELECT("sk_art")
USE IN SELECT("k_grup")
USE IN SELECT("STAT1")
USE IN SELECT("PRILO1")
USE IN SELECT("podgrartcp")
USE IN SELECT("sk_naim")
IF m.OpenArtVrem = 0
   USE IN SELECT("artvrem")
ENDIF
IF m.NowOpenSkNaimW = 0
   USE IN SELECT("sknaim")
ENDIF
IF m.OpenPodgrpeo = 0
   USE IN SELECT("podgrpeo")
ENDIF
USE IN SELECT("artdobav")
USE IN SELECT("TmpArtALL")
USE IN SELECT("VremVidOtd")
IF m.OpenKlient = 0
   USE IN SELECT("klient")
ENDIF    
IF NOT EMPTY(m.ThisAlias)
   SELECT (m.ThisAlias)
ENDIF

RETURN
