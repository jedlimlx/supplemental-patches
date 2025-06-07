noSmoothLighting = true; noDirectionalShading = true;
lmCoordM = vec2(1.0, 0.0);

float dotColor = dot(color.rgb, color.rgb);
emission = min(pow2(pow2(pow2(pow2(dotColor * 0.5)))), 5.0) * 0.4;

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif

overlayNoiseIntensity = 0.3;