if (mat % 4 < 3) {
    if (color.r > color.g * 5.0) {
        emission = 8.0 * color.r;
        color.rgb *= color.rgb;

        overlayNoiseIntensity = 0.35, overlayNoiseEmission = 0.2;
    }

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