highlightMult = 2.0;
smoothnessG = pow2(color.r) * 1.2;
smoothnessG = min1(smoothnessG);
reflectMult = smoothnessG;

if (color.r > 0.5 || (color.r > 0.35 && color.g < 0.1)) {
	emission = 2.0;
}

#ifdef COATED_TEXTURES
	noiseFactor = 0.5;
#endif
