smoothnessG = 0.6 * pow2(color.g);

translucentMultCalculated = true;
reflectMult = smoothnessG;
translucentMult.rgb = pow1_5(color.rgb) * 0.6;

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.2;
mossNoiseIntensity = 0.2;