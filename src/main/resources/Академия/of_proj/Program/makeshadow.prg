*==========================================================================*
*PROCEDURE MakeShadow
*
* Процедура создания эффекта тени на Родительском объекте-Контейнере...
*
*--------------------------------------------------------------------------*
PARAMETERS pWhatMethod, pShow, pParentForm, pShadowLevel, pShadowColor, ;
   pYesLockScreen, pNameArrayForProperty, pOffThemes
*
* pWhatMethod = 1 - наложение на объект п/прозрачной картинки 
*                   (при таком исполнении есть эффект медленной прорисовки сверху-вниз,
*                    но все выглядит оч-ч-чень стильно)
*                   Наложение картинки, похоже на метод 1, но только картинка создается 
*                   программно через GDI+
*
*             = 2,3 - переопределение свойств, связанных с цветом
*                   (при таком исполнении все очень быстро, т.е. практически незаметно,
*                    но ПОКА выглядит НЕ ТАК стильно КАК В 1-ОМ СЛУЧАЕ)
*
*             = 4 - Используется метод создания на форме объекта SHAPE
*
*
* pShow          - Показать/Удалить тень (.T./.F.)
*
* pParentForm    - Объект родитель(контейнер), для которого необходимо выполнить затемнение...
*
* pShadowLevel   - Уровень прозрачности. Любое число от 0 до 255
*
* pShadowColor   - Основной Цвет заливки в формате RGB(x,y,z)
*
* pYesLockScreen - выполнить LockScreen формы, на которой вызывается эта процедура
*
* pNameArrayForProperty - имя массива для сохранения значений свойств цвета
*                         объектов до применения черно-белой схемы....
*                         (ДЛЯ pWhatMethod = 2) 
*
* pOffThemes = .T. - отключать темы (ДЛЯ pWhatMethod = 2) 
*            = .F. - НЕ отключать темы, оставить ее как есть (ДЛЯ pWhatMethod = 2) 
*

IF pShow  && Показать тень   

   DO CASE
   CASE INLIST(pWhatMethod, 1)

       * Создаем на Родителе-Контейнере объект Картинка-Тень
      pParentForm.ADDOBJECT('imgShadowImage','Image')
      * ... сначала сделаем объект невидимым ...
      pParentForm.imgShadowImage.VISIBLE = .F.
      * Позиционируем объект на форме в левый верхний угол относительно активного Объекта(контейнера)
      pParentForm.imgShadowImage.TOP     = 0
      pParentForm.imgShadowImage.LEFT    = 0
      * Растягиваем объект, накрывая весь Объект(контейнер)
      pParentForm.imgShadowImage.WIDTH   = pParentForm.WIDTH
      pParentForm.imgShadowImage.HEIGHT  = pParentForm.HEIGHT
      * Чтобы растянуть картинку в объекте ставим свойство...
      pParentForm.imgShadowImage.STRETCH = 2
      *pParentForm.imgShadowImage.ZORDER
      DO CASE
      CASE pWhatMethod = 1
           
           pParentForm.ADDPROPERTY('oGP')

           SET CLASSLIB TO VfpGdiPlus ADDITIVE
           pParentForm.oGP = CREATEOBJECT("GdipImages")
           pParentForm.oGP.CreateBitmap(pParentForm.Width, pParentForm.Height , pParentForm.oGP.ARGB(pShadowColor, pShadowLevel))
*!*                IF FILE(pParentForm.Name + "_Shadow.png")
*!*                   DELETE FILE pParentForm.Name + "_Shadow.png"
*!*                ENDIF 
           pParentForm.oGP.SaveToFile(pParentForm.Name + "_Shadow.png")
           pParentForm.imgShadowImage.PICTURE = pParentForm.Name + "_Shadow.png"
           
      OTHERWISE
      ENDCASE

      * ... и сделаем объект видимым ...
      pParentForm.imgShadowImage.VISIBLE = .T.

   CASE INLIST(pWhatMethod, 2, 3)

      IF pOffThemes
         pParentForm.Themes = .F.
      ENDIF

      PUBLIC ARRAY &pNameArrayForProperty(1,3)
      m.NewIndexArray = 0
      pParentForm.LOCKSCREEN = .T.
      =ResetAllObjectsColors(pParentForm, @&pNameArrayForProperty)
      pParentForm.LOCKSCREEN = .F.

   CASE pWhatMethod = 4

      lcName = 'ShapeZoom'
      pParentForm.addobject(lcName,'Shape')

      WITH pParentForm.&lcName  
         .left   = 0
         .top    = 0
         .width  = pParentForm.width
         .height = pParentForm.height  
         .BackStyle   = 0  
         .BorderStyle = 0  
         .BorderWidth = 0  
      *   .Curvature   = 4  
         .DrawMode    = 9  
         .FillColor   = pShadowColor           && RGB(110,110,110)
         .FillStyle   = 0
         .SpecialEffect = 1
      *   .Anchor      = lcNameObject.Anchor    && 240
         .visible     = .t.  
         .ZOrder(0)
      ENDWITH
   
   ENDCASE


   TRY
     * Задержка, чтобы началась прорисовка объекта...
     * Если LockSсreen = .T., то объект не успевает нарисоваться и как бы 
     * тень не появляется...
     INKEY(0.03,'H')
   CATCH
   ENDTRY
   
   IF pYesLockScreen
      pParentForm.LOCKSCREEN = .T.
   ENDIF

ELSE   && Убрать тень

   DO CASE
   CASE INLIST(pWhatMethod, 1)

      * Удаляем созданный объект-картинка-тень на родительском объекте ...
      pParentForm.imgShadowImage.Picture = ''
      pParentForm.REMOVEOBJECT('imgShadowImage')
      IF FILE(pParentForm.Name + "_Shadow.png")
         DELETE FILE pParentForm.Name + "_Shadow.png"
      ENDIF 

   CASE INLIST(pWhatMethod, 2, 3)

      IF pOffThemes
         * ... дано указание на включение тем при снятии затемнения ...
         pParentForm.Themes = .T.
      ENDIF

      * Восстанавливаем цвета на родительском объекте ... 
      =RestoreAllObjectsColors(@&pNameArrayForProperty)
      RELEASE &pNameArrayForProperty

   CASE pWhatMethod = 4

      lcName = 'ShapeZoom'
      pParentForm.REMOVEOBJECT(lcName)

   ENDCASE

   IF pYesLockScreen
      pParentForm.LOCKSCREEN = .F.
   ENDIF

ENDIF

* End MakeShadow
*==========================================================================*

*==========================================================================*
PROCEDURE ResetAllObjectsColors
*--------------------------------------------------------------------------*
* Запоминает цветовые характеристики объектов и затем сбрасывает их 
* (эти характеристики) в черно-белую схему....
*
* ВАЖНО! Процедура реализована для реккурсивного вызова самой себя !!!!!
*
PARAMETERS pParentObject, pArrayName

* pParentObject - Имя родительского стартового объекта
*
* pArrayName    - Имя массива для хранения информации об объекте, свойстве и 
*                 значении свойства ...

PRIVATE pParent, aObjectsArray, aPropertiesArray, m.ObjectCNT, m.PropertiesCNT, m.Ind

* ... это текущий рассматриваемый объект ...
pParent = pParentObject

* Список объектов, содержащихся внутри объекта pParent
m.ObjectCNT = AMEMBERS(aObjectsArray, pParent, 2)

* Список свойств объекта pParent
m.PropertiesCNT = AMEMBERS(aPropertiesArray, pParent, 1, 'N')

FOR m.Ind = 1 TO m.PropertiesCNT        &&ALEN(aPropertiesArray, 1)
   * Перебираем свойства родительского объекта
   IF UPPER(aPropertiesArray(m.Ind, 2)) == 'PROPERTY'

      IF AT('COLOR', UPPER(aPropertiesArray(m.Ind, 1))) > 0 AND ;
         NOT INLIST(UPPER(aPropertiesArray(m.Ind, 1)), ;
           'DYNAMICFORECOLOR','DYNAMICBACKCOLOR','COLORSCHEME', 'COLORSOURCE')

         IF NOT pOffThemes
            * ... ТЕМЫ НЕ ОТКЛЮЧЕНЫ !!!!
            IF INLIST(UPPER(aPropertiesArray(m.Ind, 1)),'BORDERCOLOR')         
               * ...пропускаем это св-во, так как при темах оно READ ONLY !!!!!!!!
               LOOP
            ENDIF
         ENDIF
      

         m.NewIndexArray = m.NewIndexArray + 1
         DIMENSION pArrayName(m.NewIndexArray, 3)

         * Название объекта
         pArrayName(m.NewIndexArray, 1) = pParent
         * Название свойства
         pArrayName(m.NewIndexArray, 2) = aPropertiesArray(m.Ind, 1)
         * Старое значение Значение Свойства
         pArrayName(m.NewIndexArray, 3) = GETPEM(pParent, aPropertiesArray(m.Ind, 1))
         
         m.Color = DeColorizeColor(pArrayName(m.NewIndexArray, 1).&pArrayName(m.NewIndexArray, 2), (100 - pShadowLevel) / 100, pWhatMethod)
         pArrayName(m.NewIndexArray, 1).&pArrayName(m.NewIndexArray, 2) = ;
         IIF(pWhatMethod = 2, RGB(m.Color, m.Color, m.Color), m.Color)

      ENDIF
   ENDIF
ENDFOR

IF m.ObjectCNT > 0
   FOR m.Ind = 1 TO ALEN(aObjectsArray)
      =ResetAllObjectsColors( pParent.&aObjectsArray(m.Ind), @pArrayName)
   ENDFOR
ELSE
   RETURN
ENDIF

* End ResetAllObjectsColors
*==========================================================================*

*==========================================================================*
PROCEDURE RestoreAllObjectsColors
*--------------------------------------------------------------------------*
PARAMETERS pArrayName

FOR m.Ind = ALEN(pArrayName, 1) TO 1 STEP -1
     pArrayName(m.Ind, 1).&pArrayName(m.Ind, 2) = pArrayName(m.Ind, 3)
ENDFOR

* End RestoreAllObjectsColors
*==========================================================================*

*==========================================================================*
PROCEDURE DeColorizeColor
*
* Процедура обесцвечивания переданного цветного ЦВЕТА в оттенки серого
*
*--------------------------------------------------------------------------*
PARAMETERS pColor, pDarkNessLevel, pWhatMethod

PRIVATE m.CurrAlias, m.ColorB, m.ColorG, m.ColorR, m.ForReturn

* Выделим из цвета Голубую составляющую
m.ColorB = INT(pColor / 65536)
* Выделим из цвета Зелёную составляющую
m.ColorG = MOD(pColor, 65536)
* Выделим из цвета Красную составляющую
m.ColorR = MOD(m.ColorG, 256)
m.ColorG = INT(m.ColorG / 256)
DO CASE
CASE pWhatMethod = 2
     * Получим оттенок серого
     m.ForReturn = pDarkNessLevel * (0.3 * m.ColorR + 0.6 * m.ColorG + 0.1 * m.ColorB)
CASE pWhatMethod = 3
     m.ColorR = INT(m.ColorR * pDarkNessLevel)
     m.ColorG = INT(m.ColorG * pDarkNessLevel)
     m.ColorB = INT(m.ColorB * pDarkNessLevel)
     * Понизим цветовую насыщенность исходного цвета
     m.ForReturn = m.ColorR + 256 * m.ColorG + 65536 * m.ColorB
OTHERWISE
ENDCASE

RETURN m.ForReturn
* End DeColorizeColor
*==========================================================================*
   