if (mat % 4 < 3) {
    if (color.r < 0.8) {
        #include "/lib/materials/specificMaterials/terrain/repulsiveMagnia.glsl"
    } else {
        emission = 2.5 * color.g;
        color.rgb *= color.rgb;
    }
} else {
    float NdotE = dot(normalM, eastVec);
    if (abs(abs(NdotE) - 0.5) < 0.4) {
        #include "/lib/materials/specificMaterials/terrain/repulsiveMagnia.glsl"
    }
}