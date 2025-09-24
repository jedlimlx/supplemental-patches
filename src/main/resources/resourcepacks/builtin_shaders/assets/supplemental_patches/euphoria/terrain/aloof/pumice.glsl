#include "/lib/materials/specificMaterials/terrain/pumice.glsl"

if (color.r - color.b > 0.3) {  // lava
    emission = pow2(pow2(color.r)) * 4.0;

    #if RAIN_PUDDLES >= 1 || defined SPOOKY_RAIN_PUDDLE_OVERRIDE
        noPuddles = color.g * 4.0;
    #endif

    color.gb *= max(2.0 - 11.0 * pow2(color.g), 0.5);

    maRecolor = vec3(emission * 0.075);

    overlayNoiseIntensity = 0.0;

    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif
} else if (color.b - color.r > 0.15) {  // ectoplasm
    float dotColor = dot(color.rgb, color.rgb);
    emission = 1.5 * dotColor;
    color.rgb = pow3(color.rgb);
    
    maRecolor = vec3(emission * 0.075);
} else {
    lmCoordM.x *= 0.7;
}