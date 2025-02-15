smoothnessG = 2.5 * color.b;
highlightMult = 3.0 * pow2(pow2(color.r)) * smoothnessG;

translucentMultCalculated = true;
reflectMult = 0.8;
translucentMult.rgb = pow2(color.rgb);

overlayNoiseAlpha = 0.4;