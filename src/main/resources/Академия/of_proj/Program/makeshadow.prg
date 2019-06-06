*==========================================================================*
*PROCEDURE MakeShadow
*
* ��������� �������� ������� ���� �� ������������ �������-����������...
*
*--------------------------------------------------------------------------*
PARAMETERS pWhatMethod, pShow, pParentForm, pShadowLevel, pShadowColor, ;
   pYesLockScreen, pNameArrayForProperty, pOffThemes
*
* pWhatMethod = 1 - ��������� �� ������ �/���������� �������� 
*                   (��� ����� ���������� ���� ������ ��������� ���������� ������-����,
*                    �� ��� �������� ��-�-���� �������)
*                   ��������� ��������, ������ �� ����� 1, �� ������ �������� ��������� 
*                   ���������� ����� GDI+
*
*             = 2,3 - ��������������� �������, ��������� � ������
*                   (��� ����� ���������� ��� ����� ������, �.�. ����������� ���������,
*                    �� ���� �������� �� ��� ������� ��� � 1-�� ������)
*
*             = 4 - ������������ ����� �������� �� ����� ������� SHAPE
*
*
* pShow          - ��������/������� ���� (.T./.F.)
*
* pParentForm    - ������ ��������(���������), ��� �������� ���������� ��������� ����������...
*
* pShadowLevel   - ������� ������������. ����� ����� �� 0 �� 255
*
* pShadowColor   - �������� ���� ������� � ������� RGB(x,y,z)
*
* pYesLockScreen - ��������� LockScreen �����, �� ������� ���������� ��� ���������
*
* pNameArrayForProperty - ��� ������� ��� ���������� �������� ������� �����
*                         �������� �� ���������� �����-����� �����....
*                         (��� pWhatMethod = 2) 
*
* pOffThemes = .T. - ��������� ���� (��� pWhatMethod = 2) 
*            = .F. - �� ��������� ����, �������� �� ��� ���� (��� pWhatMethod = 2) 
*

IF pShow  && �������� ����   

   DO CASE
   CASE INLIST(pWhatMethod, 1)

       * ������� �� ��������-���������� ������ ��������-����
      pParentForm.ADDOBJECT('imgShadowImage','Image')
      * ... ������� ������� ������ ��������� ...
      pParentForm.imgShadowImage.VISIBLE = .F.
      * ������������� ������ �� ����� � ����� ������� ���� ������������ ��������� �������(����������)
      pParentForm.imgShadowImage.TOP     = 0
      pParentForm.imgShadowImage.LEFT    = 0
      * ����������� ������, �������� ���� ������(���������)
      pParentForm.imgShadowImage.WIDTH   = pParentForm.WIDTH
      pParentForm.imgShadowImage.HEIGHT  = pParentForm.HEIGHT
      * ����� ��������� �������� � ������� ������ ��������...
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

      * ... � ������� ������ ������� ...
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
     * ��������, ����� �������� ���������� �������...
     * ���� LockS�reen = .T., �� ������ �� �������� ������������ � ��� �� 
     * ���� �� ����������...
     INKEY(0.03,'H')
   CATCH
   ENDTRY
   
   IF pYesLockScreen
      pParentForm.LOCKSCREEN = .T.
   ENDIF

ELSE   && ������ ����

   DO CASE
   CASE INLIST(pWhatMethod, 1)

      * ������� ��������� ������-��������-���� �� ������������ ������� ...
      pParentForm.imgShadowImage.Picture = ''
      pParentForm.REMOVEOBJECT('imgShadowImage')
      IF FILE(pParentForm.Name + "_Shadow.png")
         DELETE FILE pParentForm.Name + "_Shadow.png"
      ENDIF 

   CASE INLIST(pWhatMethod, 2, 3)

      IF pOffThemes
         * ... ���� �������� �� ��������� ��� ��� ������ ���������� ...
         pParentForm.Themes = .T.
      ENDIF

      * ��������������� ����� �� ������������ ������� ... 
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
* ���������� �������� �������������� �������� � ����� ���������� �� 
* (��� ��������������) � �����-����� �����....
*
* �����! ��������� ����������� ��� ������������� ������ ����� ���� !!!!!
*
PARAMETERS pParentObject, pArrayName

* pParentObject - ��� ������������� ���������� �������
*
* pArrayName    - ��� ������� ��� �������� ���������� �� �������, �������� � 
*                 �������� �������� ...

PRIVATE pParent, aObjectsArray, aPropertiesArray, m.ObjectCNT, m.PropertiesCNT, m.Ind

* ... ��� ������� ��������������� ������ ...
pParent = pParentObject

* ������ ��������, ������������ ������ ������� pParent
m.ObjectCNT = AMEMBERS(aObjectsArray, pParent, 2)

* ������ ������� ������� pParent
m.PropertiesCNT = AMEMBERS(aPropertiesArray, pParent, 1, 'N')

FOR m.Ind = 1 TO m.PropertiesCNT        &&ALEN(aPropertiesArray, 1)
   * ���������� �������� ������������� �������
   IF UPPER(aPropertiesArray(m.Ind, 2)) == 'PROPERTY'

      IF AT('COLOR', UPPER(aPropertiesArray(m.Ind, 1))) > 0 AND ;
         NOT INLIST(UPPER(aPropertiesArray(m.Ind, 1)), ;
           'DYNAMICFORECOLOR','DYNAMICBACKCOLOR','COLORSCHEME', 'COLORSOURCE')

         IF NOT pOffThemes
            * ... ���� �� ��������� !!!!
            IF INLIST(UPPER(aPropertiesArray(m.Ind, 1)),'BORDERCOLOR')         
               * ...���������� ��� ��-��, ��� ��� ��� ����� ��� READ ONLY !!!!!!!!
               LOOP
            ENDIF
         ENDIF
      

         m.NewIndexArray = m.NewIndexArray + 1
         DIMENSION pArrayName(m.NewIndexArray, 3)

         * �������� �������
         pArrayName(m.NewIndexArray, 1) = pParent
         * �������� ��������
         pArrayName(m.NewIndexArray, 2) = aPropertiesArray(m.Ind, 1)
         * ������ �������� �������� ��������
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
* ��������� �������������� ����������� �������� ����� � ������� ������
*
*--------------------------------------------------------------------------*
PARAMETERS pColor, pDarkNessLevel, pWhatMethod

PRIVATE m.CurrAlias, m.ColorB, m.ColorG, m.ColorR, m.ForReturn

* ������� �� ����� ������� ������������
m.ColorB = INT(pColor / 65536)
* ������� �� ����� ������ ������������
m.ColorG = MOD(pColor, 65536)
* ������� �� ����� ������� ������������
m.ColorR = MOD(m.ColorG, 256)
m.ColorG = INT(m.ColorG / 256)
DO CASE
CASE pWhatMethod = 2
     * ������� ������� ������
     m.ForReturn = pDarkNessLevel * (0.3 * m.ColorR + 0.6 * m.ColorG + 0.1 * m.ColorB)
CASE pWhatMethod = 3
     m.ColorR = INT(m.ColorR * pDarkNessLevel)
     m.ColorG = INT(m.ColorG * pDarkNessLevel)
     m.ColorB = INT(m.ColorB * pDarkNessLevel)
     * ������� �������� ������������ ��������� �����
     m.ForReturn = m.ColorR + 256 * m.ColorG + 65536 * m.ColorB
OTHERWISE
ENDCASE

RETURN m.ForReturn
* End DeColorizeColor
*==========================================================================*
   