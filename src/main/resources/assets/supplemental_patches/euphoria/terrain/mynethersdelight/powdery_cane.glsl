bool bloomed = mat % 4 == 2;
#include "/lib/materials/specificMaterials/terrain/powderyCane.glsl"

if (mat % 4 == 3) {  // cane plants
    if (absMidCoordPos.x > 0.005)
        subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;

    sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0, isFoliage = true;
}