materialMask = OSIEBCA * deferredMaterial("supplemental_patches:caverns_and_chasms/tin");  // Tin Fresnel

#ifdef GBUFFERS_TERRAIN
	smoothnessG = 1.5 * pow2(pow2(color.r)) + 0.2;
#else
	smoothnessG = 1.5 * pow2(color.r) + 0.2;
#endif

highlightMult = smoothnessG * 3.0;
smoothnessD = smoothnessG;
materialMask = OSIEBCA; // Intense Fresnel

color.rgb *= 0.6 + 0.5 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
	noiseFactor = 0.33;
#endif
