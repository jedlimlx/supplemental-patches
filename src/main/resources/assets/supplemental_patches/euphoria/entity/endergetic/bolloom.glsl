vec2 tSize = textureSize(tex, 0);
ivec2 texCoordScaled = ivec2(texCoord * tSize);

if (texCoordScaled.y > 15) {
    lmCoordM.x = max(lmCoordM.x, 0.8);
}

if (entityId % 4 == 2 && color.g * 1.4 > color.r) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = pow2(pow2(dotColor * 0.2)) + 0.5 * dotColor;
}