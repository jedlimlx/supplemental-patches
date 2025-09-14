#include "/lib/materials/specificMaterials/terrain/roseQuartzBlock.glsl"

emission = color.r > 0.6 ? 0.2 : 0.0;
lmCoordM.x = max(lmCoordM.x, 0.1);