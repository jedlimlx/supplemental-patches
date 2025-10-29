materialMask = OSIEBCA * deferredMaterial("supplemental_patches:dungeonsdelight/stained_scrap"); // Lead Fresnel; // Intense Fresnel
smoothnessG = color.b + 0.2;

// #ifdef EMISSIVE_STAINED_SCRAP
if (color.r > color.g * 7 && color.r - color.b > -0.05) {
    emission = 7.0;
}
// #endif

highlightMult = smoothnessG * 2.0;
smoothnessD = smoothnessG;

color.rgb *= 0.8 + 0.3 * GetLuminance(color.rgb);