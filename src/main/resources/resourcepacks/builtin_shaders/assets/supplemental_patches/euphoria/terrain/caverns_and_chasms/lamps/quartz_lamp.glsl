#include "/lib/materials/specificMaterials/terrain/quartzBlock.glsl"

noSmoothLighting = true;
lmCoordM.x *= 0.88;

emission = pow2(pow2(color.b)) * 5.5;
color.rgb *= color.rgb;

#ifdef DISTANT_LIGHT_BOKEH
	DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif
