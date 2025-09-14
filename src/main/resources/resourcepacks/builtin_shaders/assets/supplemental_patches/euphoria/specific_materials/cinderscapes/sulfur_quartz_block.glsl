float blockRes = absMidCoordPos.x * atlasSize.x;

materialMask = OSIEBCA; // Intense Fresnel

float factor = color.g;
float factor2 = pow2(factor);
float factor4 = pow2(factor2);
float factor8 = pow2(factor4);
float noise = 0.5 + Noise3D(floor((playerPos + cameraPosition) * blockRes) / blockRes);

smoothnessG = noise * (factor - factor8 * 0.5);
highlightMult = 3.5 * factor8;

smoothnessD = noise * factor8;

#ifdef GBUFFERS_TERRAIN
    DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.8;
#endif