if (mat % 4 == 0) {  // lampear
    #include "/lib/materials/specificMaterials/terrain/lampearBlock.glsl"
} else {  // copper lampear
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"

    emission = pow2(color.g) * 2.5 + 0.2;
}

#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz);
    float r = length(fractPos - vec3(0.5, 0.3, 0.5));
    emission *= pow2(max0(1.7 - 2.4 * r) * color.b) * (mat % 4 == 0 ? 10.0 : 13.0);
    color.b *= 1 - 0.1 * emission;
#endif