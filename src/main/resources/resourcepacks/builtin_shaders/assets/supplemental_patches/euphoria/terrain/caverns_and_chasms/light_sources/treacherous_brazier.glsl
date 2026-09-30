#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
    lmCoordM.x = pow2(pow2(smoothstep1(1.0 - 0.4 * dot(fractPos.xz, fractPos.xz))));

    float campfireBrightnessFactor = mix(1.0, 0.9, clamp01(UPPER_LIGHTMAP_CURVE - 1.0));
    lmCoordM.x *= campfireBrightnessFactor;
#endif

if (color.r > 1.6 * color.b) {
    noDirectionalShading = true;
    emission = 3.00;
    color.rgb *= pow(GetLuminance(color.rgb), 0.4);

    overlayNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}
