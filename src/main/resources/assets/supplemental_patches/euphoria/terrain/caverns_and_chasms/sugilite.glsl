materialMask = OSIEBCA; // Intense Fresnel

float factor = 0.6 * pow2(pow2(color.r + color.b));
highlightMult = factor * 4.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

if (mat % 4 >= 2) factor *= 1.5;

smoothnessG = factor * 0.3;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif