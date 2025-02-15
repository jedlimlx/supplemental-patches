if (mat % 2 == 0) {
    #ifdef GBUFFERS_TERRAIN
        vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
        lmCoordM.x = pow2(pow2(smoothstep1(1.0 - 0.4 * dot(fractPos.xz, fractPos.xz))));

        float campfireBrightnessFactor = mix(1.0, 0.9, clamp01(UPPER_LIGHTMAP_CURVE - 1.0));
        lmCoordM.x *= campfireBrightnessFactor;
    #endif
}

if (color.r > 0.8) {
    #include "/lib/materials/specificMaterials/terrain/lumiseneFire.glsl"
} else {
    #include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"
}