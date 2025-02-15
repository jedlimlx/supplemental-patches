vec4 pikeColour = texelFetch(tex, ivec2(0, 31), 0);
if (CheckForColor(pikeColour.rgb, vec3(16, 12, 28))) {  // obsidian
    #include "/lib/materials/specificMaterials/terrain/obsidian.glsl"

    highlightMult *= 0.5;

    if (color.r > 0.3) {
        emission = 4.0 * color.r;
        color.r *= 1.15;
    }
} else if (CheckForColor(pikeColour.rgb, vec3(49, 71, 83))) {  // supercharged
    if (color.r > 0.35 && color.b > 1.8 * color.r)
    emission = 3.5 * color.b;
}