if (mat % 4 == 0) {  // Grass
    float dif = GetMaxColorDif(color.rgb);
    if (dif < 0.05) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else {
        smoothnessG = pow2(color.g);

        #ifdef SNOWY_WORLD
            snowMinNdotU = min(pow2(pow2(color.g)) * 1.9, 0.1);
            color.rgb = color.rgb * 0.5 + 0.5 * (color.rgb / glColor.rgb);
        #endif
    }
} else if (mat % 8 == 2) {  // Snow
    float dotColor = dot(color.rgb, color.rgb);
    if (dotColor > 1.5) { // Snowy Variants:Snowy Part
        #include "/lib/materials/specificMaterials/terrain/snow.glsl"

        overlayNoiseIntensity = 0.0;
    } else { // Snowy Variants:Stone Part
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    }
}