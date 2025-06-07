noSmoothLighting = true;

vec3 worldPos = playerPos + cameraPosition;
float fractPos = fract(worldPos.y);
if (fractPos > 0.375) {
    subsurfaceMode = 1, noDirectionalShading = true;

    if (mat % 4 == 0) {  // glow shroom
        if (color.g > color.b + 0.035 && color.g > 0.72) {
            emission = 1.25 * pow2(pow2(color.g)) + 0.3 * pow2(color.b);
        }
    }
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
