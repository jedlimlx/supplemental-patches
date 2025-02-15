// TODO find out why this doesnt work
materialMask = OSIEBCA * deferredMaterial("supplemental_patches:caverns_and_chasms/necronium");  // Necronium Fresnel

smoothnessG = pow2(color.g) * 2.0;
highlightMult = smoothnessG * 2.0;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif