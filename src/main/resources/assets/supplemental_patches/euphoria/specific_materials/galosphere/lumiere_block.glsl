materialMask = OSIEBCA; // Intense Fresnel
float factor = pow2(0.8 * color.g + 0.2 * color.r);
highlightMult = factor * 3.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

#if GLOWING_LUMIERE >= 2
    emission = dot(color.rgb, color.rgb) * 0.3;
    overlayNoiseEmission = 0.5;
#endif

smoothnessG = 0.8 - factor * 0.3;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif