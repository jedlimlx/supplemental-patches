materialMask = OSIEBCA; // Intense Fresnel

float factor = sqrt2(GetLuminance(color.rgb) * color.b);
float factor2 = pow2(factor);
float factor4 = pow2(factor2);

smoothnessG = factor - factor4 * 0.3;
highlightMult = 3.0 * factor4;

smoothnessD = factor4 + 0.2;

#ifdef COATED_TEXTURES
	noiseFactor = 0.5;
#endif
