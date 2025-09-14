if (color.r / color.b > 2.5) {  // Smouldering Part
    emission = pow2(color.r) * 3.5;
    color.gb *= 0.5;
    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.15;

    #ifdef GBUFFERS_TERRAIN
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.501)
                  + floor(playerPos.y + cameraPosition.y + 0.501);
        bpos = bpos * 0.01 + 0.0005 * frameTimeCounter;
        emission *= pow2(texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r));
        emission *= 6.0;
    #endif
} else {  // Wood Part
    #include "/lib/materials/specificMaterials/planks/scorchedPlanks.glsl"
}

if (mat % 4 == 3) {  // Powered Redstone Components
    redstoneIPBR(color.rgb, emission);
}