noSmoothLighting = true;
if (color.g > 1.3 * color.b && color.r < 0.5) {
    #include "/lib/materials/specificMaterials/terrain/leaves.glsl"
} else if (color.r > 0.5 && color.b < 0.5) {
    subsurfaceMode = 1;
    lmCoordM.x *= 0.875;

    emission = color.r < 0.75 ? 2.5 : 8.0;
    color.rgb = color.rgb * vec3(1.0, 0.8, 0.6);
    isFoliage = false;
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}