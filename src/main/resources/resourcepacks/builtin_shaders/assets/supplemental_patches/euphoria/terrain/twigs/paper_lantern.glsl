if (color.r / color.b > 1.9) {
    #include "/lib/materials/specificMaterials/planks/oakPlanks.glsl"
    lmCoordM.x *= 0.88;
} else {
    vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.5)
    + floor(playerPos.y + cameraPosition.y + 0.5);
    vec2 flickerNoise = texture2D(noisetex, vec2(frameTimeCounter * 0.015 + bpos.r * 0.1)).rb;

    noSmoothLighting = true;
    lmCoordM.x = 0.77;

    emission = 0.4 * (color.r + color.b + color.g);

    // motion of candle within lantern
    vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz) - 0.5;
    if (abs(NdotU) < 0.1 && max(abs(fractPos.x), abs(fractPos.z)) > 0.1)
        emission /= (1 + length(signMidCoordPos - 0.05 * sin(flickerNoise.gr * 5.0)));

    // appearance of flickering
    emission *= mix(1.0, min1(max(flickerNoise.r, flickerNoise.g) * 1.7), pow2(7 * 0.1));

    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.r *= mix(max(emission, 1) + 1, 1, inSoulValley);
        color.b *= mix(1, pow3(max(emission, 1)) + 1, inSoulValley);
        color.g *= max(emission, 1) + 1;
    #else
        color.r *= pow1_5(max(emission, 1)) + 1;
        color.g *= max(emission, 1) + 1;
    #endif

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
    #endif
}