#include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"

noSmoothLighting = true;
lmCoordM.x *= 0.88;

emission = pow3(color.g) * 5.5;
color.rgb *= color.rgb;

#ifdef DISTANT_LIGHT_BOKEH
	DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif
