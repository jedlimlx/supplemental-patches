#ifndef NOT_GLOWING_CHORUS_FLOWER
    vec3 checkColor = texture2DLod(tex, texCoord, 0).rgb;
    if (
        CheckForColor(checkColor, vec3(164, 157, 126)) ||
        CheckForColor(checkColor, vec3(201, 197, 176)) ||
        CheckForColor(checkColor, vec3(226, 221, 188)) ||
        CheckForColor(checkColor, vec3(153, 142, 95))
    ) {
        emission = min(GetLuminance(color.rgb), 0.75) / 0.75;
        emission = pow2(pow2(emission)) * 6.5;
        color.gb *= 0.85;

        overlayNoiseIntensity = 0.1, overlayNoiseEmission = 0.8;
    } else emission = max0(GetLuminance(color.rgb) - 0.5) * 3.0;
#endif