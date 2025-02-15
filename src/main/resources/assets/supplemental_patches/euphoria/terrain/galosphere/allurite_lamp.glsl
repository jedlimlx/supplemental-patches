materialMask = OSIEBCA; // Intense Fresnel
float factor = pow2(0.6 * color.b + 0.4 * color.g);
smoothnessG = 0.8 - factor * 0.3;
highlightMult = factor * 3.0;
smoothnessD = factor;

noSmoothLighting = true;
lmCoordM.x *= 0.88;

emission = pow2(0.8 * color.r + 0.2 * color.g) * 5.5;
color.r *= 1.0 - emission * 0.09;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif