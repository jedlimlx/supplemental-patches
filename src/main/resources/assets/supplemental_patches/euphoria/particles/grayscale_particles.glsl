if (color.b > 1.15 * (color.r + color.g) && color.g > color.r * 1.25 && color.g < 0.425 && color.b > 0.75) { // Water Particle
    materialMask = 0.0;
    color.rgb = sqrt3(color.rgb);
    color.rgb *= 0.7;
    if (dither > 0.4) discard;
    #ifdef NO_RAIN_ABOVE_CLOUDS
        if (cameraPosition.y > maximumCloudsHeight) discard;
    #endif
#ifdef OVERWORLD
} else if (color.b > 0.7 && color.r < 0.28 && color.g < 0.425 && color.g > color.r * 1.4) { // physics mod rain
    #ifdef NO_RAIN_ABOVE_CLOUDS
        if (cameraPosition.y > maximumCloudsHeight) discard;
    #endif

    if (color.a < 0.1 || isEyeInWater == 3) discard;
    color.a *= rainTexOpacity;
    color.rgb = sqrt2(color.rgb) * (blocklightCol * 2.0 * lmCoord.x + ambientColor * lmCoord.y * (0.7 + 0.35 * sunFactor));
} else if (color.rgb == vec3(1.0) && color.a < 0.765 && color.a > 0.605) { // physics mod snow (default snow opacity only)
    #ifdef NO_RAIN_ABOVE_CLOUDS
        if (cameraPosition.y > maximumCloudsHeight) discard;
    #endif

    if (color.a < 0.1 || isEyeInWater == 3) discard;
    color.a *= snowTexOpacity;
    color.rgb = sqrt2(color.rgb) * (blocklightCol * 2.0 * lmCoord.x + lmCoord.y * (0.7 + 0.35 * sunFactor) + ambientColor * 0.2);
#endif
}

float dotColor = dot(color.rgb, color.rgb);
if (dotColor > 0.25 && color.g < 0.5 && (color.b > color.r * 1.1 && color.r > 0.3 || color.r > (color.g + color.b) * 3.0)) {
    // Ender Particle, Crying Obsidian Particle, Redstone Particle
    emission = clamp(color.r * 8.0, 1.6, 5.0);
    color.rgb = pow1_5(color.rgb);
    lmCoordM = vec2(0.0);
    #if defined NETHER && defined BIOME_COLORED_NETHER_PORTALS
        if (color.b > color.r * color.r && color.g < 0.16 && color.r > 0.2)
            color.rgb = changeColorFunction(color.rgb, 10.0, netherColor, 1.0); // Nether Portal
    #endif
} else if (color.r > 0.83 && color.g > 0.23 && color.b < 0.4) {
    // Lava Particles
    emission = 2.0;
    color.b *= 0.5;
    color.r *= 1.2;
    color.rgb += vec3(min(pow2(pow2(emission * 0.35)), 0.4)) * LAVA_TEMPERATURE * 0.5;
    emission *= LAVA_EMISSION;
    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 3.5, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 3.5, colorEndBreath, 1.0);
    #endif
}