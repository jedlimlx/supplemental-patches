materialMask = OSIEBCA * deferredMaterial("supplemental_patches:savage_and_ravage/blast_proof_plates"); // Lead Fresnel

#ifdef GBUFFERS_TERRAIN
    smoothnessG = 0.8 * pow2(max(color.r, color.g));
#else
    smoothnessG = 0.8 * max(color.r, color.g);
#endif

highlightMult = 2.5 * min1(smoothnessG);
smoothnessD = smoothnessG;

color.rgb *= 0.2 + 0.7 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif