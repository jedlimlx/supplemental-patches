lmCoordM.x *= 0.88;
if (color.r - color.b > 0.1) {
    if (color.r < 0.28) {  // nether bricks
        float factor = smoothstep1(min1(color.r * 1.5));
        factor = factor > 0.12 ? factor : factor * 0.5;
        smoothnessG = factor;
        smoothnessD = factor;
    } else {  // fire
        float dotColor = dot(color.rgb, color.rgb);
        emission = 2.5 * dotColor * max0(pow2(pow2(pow2(color.r))) - color.b) + pow(dotColor * 0.35, 32.0);
        color.r *= 1.0 + 0.1 * emission;

        #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
            color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
        #endif
        #ifdef PURPLE_END_FIRE_INTERNAL
            color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
        #endif
        overlayNoiseIntensity = 0.0;
    }
} else if (color.b - color.r > 0.1 && color.b > 0.35) {
    emission = 1.5;
    color.rgb = pow1_5(color.rgb);
} else if (abs(color.r - color.b) < 0.1) {  // blackstone
    #include "/lib/materials/specificMaterials/terrain/blackstone.glsl"
}