noSmoothLighting = true;

vec3 worldPos = playerPos + cameraPosition;
float fractPos = fract(worldPos.y);
if (fractPos > 0.375) {
    subsurfaceMode = 1, noDirectionalShading = true;

    if (mat % 4 == 0) {  // sea pickle
        if (color.b > 0.5) { // Sea Pickle:Emissive Part
            color.g *= 1.1;
            emission = 5.0;
        }
    } else if (mat % 4 == 2) {  // weeping vines
        #if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
            doTileRandomisation = false;
        #endif

        if (color.r > 0.91) {
            emission = 3.0 * color.g;
            color.r *= 1.2;
            maRecolor = vec3(0.1);
        }

        isFoliage = false;
    }
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
