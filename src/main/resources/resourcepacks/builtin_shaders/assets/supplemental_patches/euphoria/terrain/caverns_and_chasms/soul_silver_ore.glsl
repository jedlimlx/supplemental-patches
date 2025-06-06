if (color.b > 0.4) {  // Raw Silver Part
    #include "/lib/materials/specificMaterials/terrain/rawSilverBlock.glsl"

    #ifdef GLOWING_ORE_SOULSILVER
        emission = 2.0 * pow1_5(color.b) + min(0.6 * color.r, 0.1);

        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
        #endif
        emission *= GLOWING_ORE_MULT;
    #endif
} else {  // Soul Soil Part
    smoothnessG = color.r * 0.4;
    smoothnessD = color.r * 0.25;
    #if defined GBUFFERS_TERRAIN && defined EMISSIVE_SOUL_SAND
        vec3 playerPosM = playerPos + relativeEyePosition;
        if (length(playerPosM) < 3.0 && length(playerPosM.y) > 1.1)
        if (
            color.r < 0.26 && color.r > 0.257 && color.g < 0.2 && color.b < 0.17 && blockUV.y > 0.9999 // A lot of hardcoded stuff
            && (
                blockUV.x > 0.625 && blockUV.z < 0.5 && blockUV.z > 0.375
                || blockUV.x < 0.5 && blockUV.x > 0.25 && blockUV.z < 0.1875
                || blockUV.x < 0.375 && blockUV.z > 0.8125
            )
        ) {
            float randomPos = step(0.5, hash13(mod(floor(worldPos + atMidBlock / 64) + frameTimeCounter * 0.0001, vec3(100))));
            vec2 flickerNoiseBlock = texture2D(noisetex, vec2(frameTimeCounter * 0.04)).rb;
            float noise = mix(1.0, min1(max(flickerNoiseBlock.r, flickerNoiseBlock.g) * 1.7), 0.6) * (clamp(3.0 / length(vec3(playerPosM.x * 1.5, playerPosM.y, playerPosM.z * 1.5)) - 1.0, 0.0, 0.25) * 2.0) * randomPos;
            color.rgb = changeColorFunction(color.rgb, 80.0, colorSoul, noise);
            emission = 4.0 * noise;
            overlayNoiseIntensity = 1.0 - noise;
        }
    #endif
}