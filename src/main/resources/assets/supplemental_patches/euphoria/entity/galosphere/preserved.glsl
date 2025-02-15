if (color.r > 0.8) {
    #include "/lib/materials/specificMaterials/terrain/pinkSalt.glsl"

    if (entityId % 4 == 0) {
        #if GLOWING_PINK_SALT >= 3
            emission = pow2(color.g) * color.g * 3.0;
            color.g *= 1.0 - emission * 0.07;
            emission *= 1.3;
        #endif
    } else {
        #if GLOWING_PINK_SALT >= 2
            emission = pow2(color.g) * color.g * 3.0;
            color.g *= 1.0 - emission * 0.07;
            emission *= 1.3;
        #endif
    }

    overlayNoiseIntensity = 0.5;
}