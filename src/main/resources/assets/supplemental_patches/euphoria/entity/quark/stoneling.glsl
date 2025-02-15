#include "/lib/materials/specificMaterials/terrain/stone.glsl"

if (
    CheckForColor(color.rgb, vec3(118, 148, 134)) ||
    CheckForColor(color.rgb, vec3(106, 121, 120)) ||
    CheckForColor(color.rgb, vec3(188, 188, 157)) ||
    CheckForColor(color.rgb, vec3(92, 108, 96)) ||
    CheckForColor(color.rgb, vec3(178, 178, 136))
) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = min(pow2(pow2(dotColor) * dotColor) * 1.4 + dotColor * 0.9, 6.0);
    emission = mix(emission, dotColor * 1.5, min1(lViewPos / 96.0));
}