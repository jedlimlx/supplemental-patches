if (
    CheckForColor(color.rgb, vec3(237, 72, 53)) ||
    CheckForColor(color.rgb, vec3(253, 227, 117)) ||
    CheckForColor(color.rgb, vec3(252, 198, 98)) ||
    CheckForColor(color.rgb, vec3(252, 153, 88)) ||
    CheckForColor(color.rgb, vec3(251, 106, 62)) ||
    CheckForColor(color.rgb, vec3(200, 51, 43))
) {
    #include "/lib/materials/specificMaterials/terrain/lava.glsl"
    emission *= 1.5;
} else {
    lmCoordM.x *= 0.95;
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
}

#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
    fractPos.xzy = fractPos.xyz;
    if (
        max(abs(fractPos.x), abs(fractPos.z)) > 0.25 &&
        abs(fractPos.y) < 0.45 &&
        abs(fractPos.y) > 0.35
    ) {
        float dist = min(
            length(abs(fractPos.xz) - vec2(min(abs(fractPos.x), 0.25), 0.25)),
            length(abs(fractPos.xz) - vec2(0.25, min(abs(fractPos.z), 0.25)))
        );
        lmCoordM.x += 40.0 * (0.45 - abs(fractPos.y)) * pow2(0.25 - dist);
    }
#endif

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 3.5, lViewPos);
#endif