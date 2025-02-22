emission = 2.0 * pow2(color.r) + 0.2 * pow2(color.b);
smoothnessG = 0.3 * color.r;

translucentMultCalculated = true;
reflectMult = max0(1.2 - emission);
translucentMult.rgb = pow2(color.rgb) * 0.4;

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.5;
mossNoiseIntensity = 0.5;