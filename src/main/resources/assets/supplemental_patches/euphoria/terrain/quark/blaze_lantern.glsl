noSmoothLighting = true, noDirectionalShading = true;
lmCoordM.x = 0.85;

smoothnessD = min1(max0(0.5 - color.b) * 2.0);
smoothnessG = color.b;

float blockRes = absMidCoordPos.x * atlasSize.x;
vec2 signMidCoordPosM = (floor((signMidCoordPos + 1.0) * blockRes) + 0.5) / blockRes - 1.0;
float dotsignMidCoordPos = dot(signMidCoordPosM, signMidCoordPosM);
float lBlockPosM = pow2(max0(1.0 - 1.7 * pow2(pow2(dotsignMidCoordPos))));
emission = pow2(color.r) * 1.6 + 2.2 * lBlockPosM;
emission *= 0.4 + max0(0.6 - 0.006 * lViewPos);

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif

overlayNoiseIntensity = 0.5;