*==========================================================================*
*PROCEDURE ShadowForObject
*--------------------------------------------------------------------------*
PARAMETERS lnOffset, lcNameObject

lcName = 'Shape' + ALLTRIM(lcNameObject.Name) + ALLTRIM(TRANSFORM(1 + 1000000 * RAND(),'@B 999999'))

oParent = lcNameObject.Parent
oParent.addobject(lcName,'Shape')

WITH oParent.&lcName  
   .width  = lcNameObject.width
   .height = lcNameObject.height  
   .left   = lcNameObject.left + lnOffset  
   .top    = lcNameObject.top  + lnOffset  
   .BackStyle   = 0  
   .BorderStyle = 0  
   .BorderWidth = 0  
   .Curvature   = 4  
   .DrawMode    = 9  
   .FillColor   = RGB(175,175,175)
   .FillStyle   = 0
   .SpecialEffect = 1
   .Anchor      = lcNameObject.Anchor    && 240
   .visible     = .t.  
ENDWITH
lcNameObject.ZOrder(0)

* End ShadowForObject
*==========================================================================*
