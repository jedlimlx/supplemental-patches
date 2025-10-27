if (mat % 2 == 0) {
    vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
    #ifdef GBUFFERS_TERRAIN
        if (mat % 4 == 0) {
            lmCoordM.x = pow2(pow2(smoothstep1(1.0 - 0.4 * dot(fractPos.xz, fractPos.xz))));
    
            float campfireBrightnessFactor = mix(1.0, 0.9, clamp01(UPPER_LIGHTMAP_CURVE - 1.0));
            lmCoordM.x *= campfireBrightnessFactor;

            lmCoordM.x *= min1(0.7 + 0.3 * smoothstep1(max0(0.4 - signMidCoordPos.y)));
        }
    #endif
    
    float dotColor = dot(color.rgb, color.rgb);
    if (
        (mat % 4 == 0 && color.g - color.r > 0.1 && fractPos.y > 0.0) || 
        (mat % 4 == 2 && (color.g - color.r > 0.1 || color.b > 0.95))
    ) {
        noDirectionalShading = true;
        emission = 2.1;
        color.rgb *= sqrt1(GetLuminance(color.rgb));

        overlayNoiseIntensity = 0.0;
    } else {
        #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
    }
} else {
    if (color.b > 0.5) {
        #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
    } else if (color.b > 0.3) {
        #include "/lib/materials/specificMaterials/terrain/anvil.glsl"
    } else {
        #include "/lib/materials/specificMaterials/planks/darkOakPlanks.glsl"
    }
}