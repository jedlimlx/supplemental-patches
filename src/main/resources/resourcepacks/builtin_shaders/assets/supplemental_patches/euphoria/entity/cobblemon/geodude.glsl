if (color.r < 0.32 && color.g < 0.41) {  // alolan geodude magnet
    #include "/lib/materials/specificMaterials/terrain/anvil.glsl"
} else if (abs(color.r - color.g) < 0.1 && color.r < 0.9) {  // geodude body
    #include "/lib/materials/specificMaterials/terrain/cobblestone.glsl"
    if (abs(color.g - color.b) < 0.1) {  // alolan geodude body
        smoothnessD *= 2.0;
        smoothnessG *= 2.0;
    }
} else if (color.r / color.g < 1.75 && color.r - color.g > 0.05 && color.g / color.b > 1.3) {
    emission = 2.0;
}