materialMask = OSIEBCA * deferredMaterial("supplemental_patches:pigsteel/pigsteel"); // Pigsteel Fresnel

smoothnessG = pow2(pow2(color.r)) + 2.0 * max0(color.r - color.g);
highlightMult = 2.5 * min1(smoothnessG);
smoothnessD = smoothnessG;

color.rgb *= 0.4 + 0.8 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 1 - 0.7 * clamp01(smoothnessG);
#endif