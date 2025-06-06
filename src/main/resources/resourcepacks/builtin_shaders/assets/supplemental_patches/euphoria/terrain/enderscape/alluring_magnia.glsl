if (mat % 4 < 3) {
    if (color.b < 0.99) {
        #include "/lib/materials/specificMaterials/terrain/alluringMagnia.glsl"
    } else {
        emission = 2.0 * color.r;
        color.rgb *= color.rgb;
    }
} else {
    float NdotE = dot(normalM, eastVec);
    if (abs(abs(NdotE) - 0.5) < 0.4) {
        #include "/lib/materials/specificMaterials/terrain/alluringMagnia.glsl"
    }
}