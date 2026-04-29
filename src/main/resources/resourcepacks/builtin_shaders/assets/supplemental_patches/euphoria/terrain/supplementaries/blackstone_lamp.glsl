if (color.b > 0.7) {
    lmCoordM = vec2(1.0, 0.0);

    float dotColor = dot(color.rgb, color.rgb);
    emission = pow2(dotColor * 0.6) * 0.8;
    color.b *= 1.2;

	color.rgb *= color.rgb;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.5, lViewPos);
    #endif
} else {
    lmCoordM.x *= 0.85;
    #include "/lib/materials/specificMaterials/terrain/blackstone.glsl"
}
