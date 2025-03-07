isFoliage = false;
if (color.r - color.b > 0.35 || color.r < 0.4) {
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
} else {
    emission = 2.0 * color.r;
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif