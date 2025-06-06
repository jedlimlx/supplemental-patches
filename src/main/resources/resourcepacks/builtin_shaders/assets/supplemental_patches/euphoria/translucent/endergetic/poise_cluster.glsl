smoothnessG = 0.3 * color.r;

translucentMultCalculated = true;
reflectMult = pow2(color.r - color.g);
translucentMult.rgb = pow2(color.rgb);

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.2;
mossNoiseIntensity = 0.2;