if (color.b > 0.33) {
    materialMask = OSIEBCA; // Intense Fresnel
                                    
    float factor = pow2(color.g);
    highlightMult = factor * 3.0;
    color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);
    
    emission = dot(color.rgb, color.rgb) * 0.35 + 0.1;
    overlayNoiseEmission = 0.5;
    
    smoothnessG = 0.8 - factor * 0.3;
    smoothnessD = factor;
    
    #ifdef COATED_TEXTURES
        noiseFactor = 0.66;
    #endif
} else {
    smoothnessG = pow2(color.r) * 0.4 + 0.1;
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}