materialMask = OSIEBCA; // Intense Fresnel
float factor = pow2(color.g);
float factor2 = pow2(factor);
smoothnessG = 1.0 - 0.5 * factor;
highlightMult = factor2 * 3.5;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif

#ifdef SSS_SNOW_ICE
    subsurfaceMode = 3, noSmoothLighting = true, noDirectionalShading = true;
#endif

if (
    CheckForColor(color.rgb, vec3(72, 106, 232)) || 
    CheckForColor(color.rgb, vec3(113, 133, 246)) || 
    CheckForColor(color.rgb, vec3(145, 189, 232)) || 
    CheckForColor(color.rgb, vec3(214, 233, 255))
) {
    emission = 2.5 * pow2(color.b) + 0.5;
    color.rg *= color.rg;
    maRecolor = vec3(0.1);
    
    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
    #endif
    
    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif
    
    overlayNoiseIntensity = 0.3;
}
