materialMask = OSIEBCA; // Intense Fresnel
float factor = pow2(color.r);
highlightMult = factor * 3.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

#if GLOWING_AMETHYST >= 2
    emission = dot(color.rgb, color.rgb) * 0.3;
    overlayNoiseEmission = 0.5;
#endif

#if ALTERNATIVE_AMETHYST_STYLE == 0
    smoothnessG = 0.8 - factor * 0.3;
    smoothnessD = factor;
#elif ALTERNATIVE_AMETHYST_STYLE == 1
    smoothnessG = max(sqrt(1.0 - factor), 0.04);
    smoothnessD = factor * 2;
    color.rgb *= 1.75 - 0.4 * GetLuminance(color.rgb);
    color.g *= 0.85 * pow2(color.g);
    color.rgb = saturateColors(color.rgb, 0.6);
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif