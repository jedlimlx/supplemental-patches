#include "/lib/materials/specificMaterials/terrain/zirconiaBlock.glsl"

noSmoothLighting = true;
lmCoordM.x *= 0.88;

emission = dot(color.rgb, color.rgb);
color.rgb *= color.rgb;

#ifdef DISTANT_LIGHT_BOKEH
	DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif
