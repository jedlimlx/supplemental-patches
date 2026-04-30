noSmoothLighting = true, noDirectionalShading = true;

smoothnessG = color.r;
if (color.b > 0.95) emission = 1.0;

translucentMultCalculated = true;
reflectMult = 0.4 * color.r;
translucentMult.rgb = pow2(color.rgb);

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.2;
mossNoiseIntensity = 0.2;