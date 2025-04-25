noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/terrain/pigsteel.glsl"

if (color.r - color.b > 0.3 || color.r > 0.9) {
    emission = 4.3 * max0(color.r - color.b);
    emission += min(pow2(pow2(0.75 * dot(color.rgb, color.rgb))), 5.0);
    color.gb *= pow(vec2(0.8, 0.7), vec2(sqrt(emission) * 0.5));
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
#endif

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.0;

#if defined SOUL_SAND_VALLEY_OVERHAUL_INTERNAL || defined PURPLE_END_FIRE_INTERNAL
    if (color.g > color.b) {
        float uniformValue = 1.0;
        float gradient = 1.0;
        vec3 colorFire = vec3(0.0);
        #if defined NETHER
            #ifdef GBUFFERS_TERRAIN
                float lanternOffset = 0.0;
                if (mat == 10562) lanternOffset = 0.1; // Hanging Lantern
                gradient = mix(1.0, 0.0, clamp01(blockUV.y - lanternOffset + 3.0 * blockUV.y - lanternOffset));
            #else
                gradient = 0.0;
            #endif
            uniformValue = inSoulValley;
            colorFire = colorSoul;
        #endif
        #ifdef END
            colorFire = colorEndBreath;
        #endif
        color.rgb = mix(color.rgb, mix(color.rgb, vec3(GetLuminance(color.rgb)), 0.88), uniformValue * gradient);
        color.rgb *= mix(vec3(1.0), colorFire * 2.0, uniformValue * gradient);
    }
#endif