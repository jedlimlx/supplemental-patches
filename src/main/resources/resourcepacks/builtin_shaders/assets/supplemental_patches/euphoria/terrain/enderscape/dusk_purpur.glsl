highlightMult = 2.0;
smoothnessG = pow2(color.r) * 1.2;
smoothnessG = min1(smoothnessG);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
	noiseFactor = 0.5;
#endif
