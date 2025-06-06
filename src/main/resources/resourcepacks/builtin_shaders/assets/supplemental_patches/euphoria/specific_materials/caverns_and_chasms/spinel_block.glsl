materialMask = OSIEBCA; // Intense Fresnel

float factor = max(GetLuminance(color.rgb), 1 - color.r);
float factor4 = pow2(pow2(factor));

smoothnessG = factor - factor4 * 0.2;
highlightMult = 4.5 * factor4;

smoothnessD = factor4 * 0.75;

#ifdef COATED_TEXTURES
noiseFactor = 0.5;
#endif
