#include "/lib/materials/specificMaterials/terrain/iridescentAsh.glsl"

materialMask = OSIEBCA; // Intense Fresnel

highlightMult = 3.5 * pow2(color.g);
smoothnessG = 0.8 * iridescence + 0.4 * pow2(color.r);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif