vec4 shellColour = texelFetch(tex, ivec2(26, 0), 0);
if (!CheckForColor(shellColour.rgb, vec3(56, 50, 55)) || shellColour.a < 0.1) {
    if (CheckForColor(shellColour.rgb, vec3(55, 103, 146))) {  // Lapis Lazuli
        #include "/lib/materials/specificMaterials/terrain/lapisBlock.glsl"
        #ifdef GLOWING_ORETORTOISE
        emission = dot(color.rgb, color.rgb) * 1.2;
        #endif
    } else {
        vec4 oreColour = texelFetch(tex, ivec2(9, 44), 0);
        if (shellColour.r > 10 * shellColour.b) {  // Redstone
            #include "/lib/materials/specificMaterials/terrain/redstoneBlock.glsl"
            #ifdef GLOWING_ORETORTOISE
            emission = 0.75 + 3.0 * pow2(pow2(color.r));
            color.gb *= 0.65;
            #endif
        } else if (shellColour.r > 2 * shellColour.b || shellColour.a < 0.1) {  // Copper
            #include "/lib/materials/specificMaterials/terrain/rawCopperBlock.glsl"
            #ifdef GLOWING_ORETORTOISE
            emission = pow2(color.r) * 1.5 + color.g * 1.3 + 0.2;
            #endif
        } else if (shellColour.r > shellColour.b) {
            if (shellColour.r > 2.5 * shellColour.g) {  // Spinel
                #include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"
                #ifdef GLOWING_ORETORTOISE
                emission = 0.45 * (1.6 * sqrt2(color.r) + 2.2 * pow2(color.r));
                #endif
            } else {  // Iron
                #include "/lib/materials/specificMaterials/terrain/rawIronBlock.glsl"
                #ifdef GLOWING_ORETORTOISE
                emission = pow1_5(color.r) * 0.5;
                #endif
            }
        } else {  // Lead
            #include "/lib/materials/specificMaterials/terrain/rawLeadBlock.glsl"
            #ifdef GLOWING_ORETORTOISE
            emission = 6.0 * sqrt2(max(color.r, color.b));
            #endif
        }
    }
}

#ifdef GLOWING_ORETORTOISE
    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    #ifdef SITUATIONAL_ORETORTOISE
        emission *= skyLightCheck;
        color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORETORTOISE_MULT))), skyLightCheck);
    #else
        color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORETORTOISE_MULT)));
    #endif

    emission *= GLOWING_ORETORTOISE_MULT * 1.5;
#endif