vec2 tSize = textureSize(tex, 0);
ivec2 texCoordScaled = ivec2(texCoord * tSize);

if (color.g * 1.3 > color.r) {
    if (
        (entityId % 4 == 0 && abs(texCoordScaled.x - 26.5) < 10 && abs(texCoordScaled.y - 52) < 3) ||
        (entityId % 4 == 1 && texCoordScaled.y < 15) ||
        (entityId % 4 == 2 && texCoordScaled.y > 14)
    ) {
        emission = 2.5 * color.g;
        color.rgb *= pow(color.rgb, vec3(0.3));
    }
} else if (
    (color.r > color.b && color.r > color.g * 2) ||
    CheckForColor(color.rgb, vec3(113, 41, 127)) ||
    CheckForColor(color.rgb, vec3(139, 47, 147))
) {
    emission = 3.0 * color.b;
    color.rgb *= 0.8;
}