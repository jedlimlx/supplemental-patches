emission = 1.2 * pow2(color.b);
smoothnessG = 0.2 * color.b;

translucentMultCalculated = true;
reflectMult = 1.2 - pow2(color.b);
translucentMult.rgb = pow2(color.rgb) * 0.2;

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.5;
mossNoiseIntensity = 0.5;