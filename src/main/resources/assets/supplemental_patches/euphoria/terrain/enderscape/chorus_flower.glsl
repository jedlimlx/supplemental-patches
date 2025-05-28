#ifdef GLOWING_CHROUS_FLOWER
    float dotColor = dot(color.rgb, color.rgb);
    if (dotColor > 1.0) {
        emission = pow2(pow2(pow2(dotColor * 0.33))) + 0.2 * dotColor;
        overlayNoiseIntensity = 0.5, overlayNoiseEmission = 0.6;
    }
#endif