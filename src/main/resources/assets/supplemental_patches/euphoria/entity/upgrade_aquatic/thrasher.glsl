// TODO get sonar to be visible in shader
vec2 tSize = textureSize(tex, 0);
ivec2 texCoordScaled = ivec2(texCoord * tSize);
if (entityId % 4 == 0) {  // thrasher
    if ((texCoordScaled.x >= 30 && texCoordScaled.y >= 88) || (texCoordScaled.x >= 50 && texCoordScaled.y >= 63)) {
        emission = 2.0 * pow2(color.b);
        color.rgb *= sqrt(color.rgb);
    }
} else {  // great thrasher
    if (texCoordScaled.x >= 48 && texCoordScaled.y >= 63) {
        emission = 2.0 * pow2(color.b);
    }
}