if (color.r > color.b * 1.8) {
    emission = 3.0;
    color.gb = pow1_5(color.gb);
} else {
    #include "/lib/materials/specificMaterials/terrain/anvil.glsl"
}