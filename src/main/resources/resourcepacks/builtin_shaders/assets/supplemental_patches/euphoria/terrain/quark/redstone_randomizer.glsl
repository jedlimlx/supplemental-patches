#if ANISOTROPIC_FILTER > 0
    color = texture2D(tex, texCoord); // Fixes artifacts
    color.rgb *= glColor.rgb;
#endif

vec3 absDif = abs(vec3(color.r - color.g, color.g - color.b, color.r - color.b));
float maxDif = max(absDif.r, max(absDif.g, absDif.b));
if (color.g - color.r > 0.1 || CheckForColor(color.rgb, vec3(229, 237, 229)) || CheckForColor(color.rgb, vec3(202, 225, 219))) { // Prismarine
    smoothnessG = pow2(color.g) * 0.8;
    highlightMult = 1.5;
    smoothnessD = smoothnessG;

    if (mat % 4 == 0) emission = pow1_5(color.r) * 2.5 + 0.2;
    overlayNoiseIntensity = 0.5, overlayNoiseEmission = 0.1;
} else if (maxDif > 0.125 || color.b > 0.99) { // Redstone Parts
    if (color.r < 0.999 && color.b > 0.4) color.gb *= 0.5; // Comparator:Emissive Wire

    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"

    overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.2;
} else { // Quartz Base
    float factor = pow2(color.g) * 0.6;

    smoothnessG = factor;
    highlightMult = 1.0 + 2.5 * factor;
    smoothnessD = factor;
}