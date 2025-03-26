if (color.b < 0.4) {  // obsidian part
    lmCoordM.x *= 0.88;
    #include "/lib/materials/specificMaterials/terrain/obsidian.glsl"
} else {
    emission = 2.0 * (0.5 * color.b + color.g);
    color.rgb *= pow(color.rgb, vec3(0.1 * emission));
}

#ifdef DISTANT_LIGHT_BOKEH
DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
#endif