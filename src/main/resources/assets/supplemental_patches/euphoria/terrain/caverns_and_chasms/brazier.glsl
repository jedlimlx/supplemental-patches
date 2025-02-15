#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
    lmCoordM.x = pow2(pow2(smoothstep1(1.0 - 0.4 * dot(fractPos.xz, fractPos.xz))));

    float campfireBrightnessFactor = mix(1.0, 0.9, clamp01(UPPER_LIGHTMAP_CURVE - 1.0));
    lmCoordM.x *= campfireBrightnessFactor;
#endif

float dotColor = dot(color.rgb, color.rgb);
if (color.r - color.b > 0.1 || dotColor > 2.9) {
    noDirectionalShading = true;
    emission = 3.5;
    color.rgb *= sqrt1(GetLuminance(color.rgb));

    overlayNoiseIntensity = 0.0;

    #if defined SOUL_SAND_VALLEY_OVERHAUL_INTERNAL || defined PURPLE_END_FIRE_INTERNAL
        float uniformValue = 1.0;
        vec3 colorFire = vec3(0.0);
        float gradient = 0.0;

        #if defined NETHER
            uniformValue = inSoulValley;
            colorFire = colorSoul;
            #ifdef GBUFFERS_TERRAIN
                gradient = mix(1.0, 0.0, clamp01(blockUV.y + 0.5 * blockUV.y));
            #elif defined GBUFFERS_HAND
                float handUV = gl_FragCoord.y / viewHeight;
                gradient = mix(1.0, 0.0, handUV + 0.4);
            #endif
        #endif

        #ifdef END
            colorFire = colorEndBreath;
            #ifdef GBUFFERS_TERRAIN
                gradient = mix(1.0, 0.0, clamp01(blockUV.y + 0.07 - 1.1 * clamp01(sin(texture2D(noisetex, vec2(frameTimeCounter * 0.01)).r) * blockUV.y)));
            #elif defined GBUFFERS_HAND
                float handUV = gl_FragCoord.y / viewHeight;
                gradient = mix(1.0, 0.0, clamp01(handUV + 0.3 - 1.3 * clamp01(sin(texture2D(noisetex, vec2(frameTimeCounter * 0.01)).r) * handUV)));
            #endif
        #endif

        color.rgb = mix(color.rgb, mix(color.rgb, vec3(GetLuminance(color.rgb)), 0.88), uniformValue * gradient);
        color.rgb *= mix(vec3(1.0), colorFire * 2.0, uniformValue * gradient);
    #endif
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}