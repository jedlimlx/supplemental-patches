if (mat % 4 == 0) {
    vec2 absCoord = abs(signMidCoordPos);
    if (color.r / color.b > 2.0 && NdotU > 0.9) {
        #include "/lib/materials/specificMaterials/terrain/lumiereBlock.glsl"
        float yellow = 0.5 * color.r + 0.5 * color.g;
        emission = 2.1 * pow2(yellow) + 0.4;
    } else if (
        absCoord.x < 0.5 &&
        signMidCoordPos.y > -5/8.0 &&
        signMidCoordPos.y < 0.25 && NdotU < 0.1
    ) {  // Lumiere Part
        #include "/lib/materials/specificMaterials/terrain/lumiereBlock.glsl"
        float yellow = 0.5 * color.r + 0.5 * color.g;
        emission = 2.1 * pow2(yellow) + 0.4;
    } else {
        #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
    }
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}