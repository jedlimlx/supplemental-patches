translucentMultCalculated = true;
reflectMult = 2.5 - pow2(color.r);
translucentMult.rgb = pow2(color.rgb) * 0.2;

smoothnessG = min(0, 1.5 * color.b - color.r) * 0.8;
highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.5;
mossNoiseIntensity = 0.5;