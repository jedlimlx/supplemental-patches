if (mat % 4 == 0) {
    if (color.g < 0.25) {
        #if GLOWING_BLOODCAP_MUSHROOM == 1
            emission = color.r;
            overlayNoiseEmission = 0.4;
        #elif GLOWING_BLOODCAP_MUSHROOM == 2
            emission = 1.5 * color.r;
            overlayNoiseEmission = 0.4;
        #elif GLOWING_BLOODCAP_MUSHROOM == 3
            emission = 2.0 * color.r;
            overlayNoiseEmission = 0.4;
        #endif
    }

    overlayNoiseIntensity = 0.6;
} else {
    noSmoothLighting = true;

    vec3 worldPos = playerPos + cameraPosition;
    float fractPos = fract(worldPos.y);
    if (fractPos > 0.375) {
        if (color.g < 0.25) {
            #if GLOWING_BLOODCAP_MUSHROOM == 1
                emission = color.r;
                overlayNoiseEmission = 0.4;
            #elif GLOWING_BLOODCAP_MUSHROOM == 2
                emission = 1.5 * color.r;
                overlayNoiseEmission = 0.4;
            #elif GLOWING_BLOODCAP_MUSHROOM == 3
                emission = 2.0 * color.r;
                overlayNoiseEmission = 0.4;
            #endif
        }

        overlayNoiseIntensity = 0.6;
    }

    sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
}