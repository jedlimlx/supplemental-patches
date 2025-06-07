if (color.g > 0.6 || color.b > 0.6) {
    #include "/lib/materials/specificMaterials/terrain/diamondBlock.glsl"
    emission = pow2(max(color.b, color.g)) + 1.5;
}