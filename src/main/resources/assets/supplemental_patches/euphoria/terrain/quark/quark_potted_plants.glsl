noSmoothLighting = true;

vec3 worldPos = playerPos + cameraPosition;
float fractPos = fract(worldPos.y);
if (fractPos > 0.375) {
    subsurfaceMode = 1, noDirectionalShading = true;

    if (mat % 4 == 0) {  // glow berries
        lmCoordM.x *= 0.875;

        if (color.r > 0.64) {
            emission = color.r < 0.75 ? 2.5 : 8.0;
            color.rgb = color.rgb * vec3(1.0, 0.8, 0.6);
            isFoliage = false;
        } else {
            isFoliage = true;
        }
    } else if (mat % 4 == 2) {  // glow lichen
        float dotColor = dot(color.rgb, color.rgb);
        emission = min(pow2(pow2(dotColor) * dotColor) * 1.4 + dotColor * 0.9, 6.0);

        color.r *= 1.15;
    }
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
