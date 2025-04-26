lmCoordM = vec2(0.75, 0.0);

if (color.g > 0.22) { // Emissive Part
    emission = pow2(pow2(color.r)) * 4.0;
    color.gb *= max(2.0 - 11.0 * pow2(color.g), 0.5);
    maRecolor = vec3(emission * 0.075);

    overlayNoiseIntensity = 0.0;
}