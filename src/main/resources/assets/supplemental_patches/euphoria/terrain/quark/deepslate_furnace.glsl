#include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
if (mat % 4 == 1) {
    lmCoordM.x *= 0.95;

    float dotColor = dot(color.rgb, color.rgb);
    emission = 2.5 * dotColor * max0(pow2(pow2(pow2(color.r))) - color.b) + pow(dotColor * 0.35, 32.0);
    color.r *= 1.0 + 0.1 * emission;

    if (color.r > color.b * 2.0 && dotColor > 0.7) {
        #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
            color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
        #endif
        #ifdef PURPLE_END_FIRE_INTERNAL
            color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
        #endif
        overlayNoiseIntensity = 0.0;
    }
}