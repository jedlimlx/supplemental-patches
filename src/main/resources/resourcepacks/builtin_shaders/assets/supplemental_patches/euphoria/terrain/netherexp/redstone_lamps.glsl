materialMask = OSIEBCA; // Intense Fresnel
smoothnessG = sqrt(0.3 * dot(color.rgb, color.rgb)) * 0.5 + 0.2;

float factor = pow2(smoothnessG);
highlightMult = factor * 2.0 + 1.0;
smoothnessD = min1(factor * 2.0);

if (mat % 32 != 24) {
    overlayNoiseIntensity = 0.3;
    noDirectionalShading = true;

    lmCoordM.x = 0.84;

    if (color.b > 0.1) {
        float dotColor = dot(color.rgb, color.rgb);
        emission = dotColor * 1.2;
        color.rgb = pow1_5(color.rgb);
        maRecolor = vec3(emission * 0.2);

        overlayNoiseIntensity = 0.3;
    }

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 5.0, lViewPos);
    #endif
}
