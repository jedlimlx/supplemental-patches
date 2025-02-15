#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
    lmCoordM.x = pow2(pow2(smoothstep1(1.0 - 0.4 * dot(fractPos.xz, fractPos.xz))));

    float campfireBrightnessFactor = mix(1.0, 0.9, clamp01(UPPER_LIGHTMAP_CURVE - 1.0));
    lmCoordM.x *= campfireBrightnessFactor;
#endif

if (color.g - color.r > 0.1 || (color.r > 0.6 && color.g > color.r)) {
    noDirectionalShading = true;
    emission = 2.50;
    color.rgb *= sqrt1(GetLuminance(color.rgb));

    overlayNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/oakWood.glsl"
}