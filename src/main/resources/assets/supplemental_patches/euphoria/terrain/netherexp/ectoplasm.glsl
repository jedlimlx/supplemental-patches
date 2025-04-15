lmCoordM = vec2(0.0);

emission = smoothstep1(sqrt1(color.b)) + 0.2;
emission *= 2.0;

color.rgb *= pow(color.rgb, vec3(0.5 + 0.3 * emission));

#if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
    doTileRandomisation = false;
#endif

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 4.5, lViewPos);
#endif

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;