materialMask = OSIEBCA; // Intense Fresnel

float factor = 0.6 * pow2(pow2(color.r + color.b)) + 0.1;
float factor4 = pow4(factor);
highlightMult = factor * 4.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

if (mat % 4 >= 2) factor *= 1.5;

smoothnessG = factor * 0.3 - factor4 * 0.2;
smoothnessD = factor - factor4 * 0.2;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif
