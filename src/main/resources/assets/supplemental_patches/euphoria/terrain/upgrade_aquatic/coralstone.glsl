smoothnessG = pow3(color.g) * 2.5;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

#ifdef GLOWING_CORALSTONE
    #include "/lib/materials/specificMaterials/terrain/coral.glsl"
    float maxDiff = max(
        abs(color.r - color.b),
        max(abs(color.b - color.g), abs(color.r - color.g))
    );
    if (maxDiff > 0.07) {
        float dotColor = dot(color.rgb, color.rgb);
        emission = dotColor;
    }
#endif