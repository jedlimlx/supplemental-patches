subsurfaceMode = 2, isFoliage = true;
sandNoiseIntensity = 0.4, mossNoiseIntensity = 0.0;

float factor = min1(pow2(color.r) * 3.0);
smoothnessG = factor * 0.2;
smoothnessD = factor * 0.2;
highlightMult = factor * 3.0 + 1.0;