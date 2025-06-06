#include "/lib/materials/specificMaterials/terrain/pinkSalt.glsl"

noSmoothLighting = true;
lmCoordM.x *= 0.88;

#if GLOWING_PINK_SALT >= 1
    emission = pow2(color.g) * 2.0;
    color.g *= 1.0 - emission * 0.07;
    emission *= 1.3;
#endif

smoothnessG = pow2(color.g) * 2.0;
smoothnessD = smoothnessG;

overlayNoiseIntensity = 0.5;