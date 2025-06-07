highlightMult = 2.0;
smoothnessG = pow2(pow2(color.b));
smoothnessD = smoothnessG;

if (mat % 4 == 2) {
    if (color.b > 1.7 * color.r) {
        emission = 7.0 - 4.0 * pow2(color.b);
        #ifdef DISTANT_LIGHT_BOKEH
            DoDistantLightBokehMaterial(emission, 2.5, lViewPos);
        #endif
    } else {
        lmCoordM.x -= 0.1;
    }
}