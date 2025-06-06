smoothnessG = 1.2;
highlightMult = 2.0;
smoothnessD = 1.0;

vec2 tSize = textureSize(tex, 0);
ivec2 texCoordScaled = ivec2(texCoord * tSize);

if (texCoordScaled.x > 5 && texCoordScaled.y > 5 && texCoordScaled.x < 11 && texCoordScaled.y < 11) {
    emission = 2.0;
    color.rgb = pow1_5(color.rgb);
}

emission = texCoordScaled.x * 0.02;