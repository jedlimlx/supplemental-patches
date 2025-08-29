#ifdef DO_DOOM_AND_GLOOM_FOG
    clouds.rgb = mix(clouds.rgb, vec3(0.5), clamp01(lViewPos / 900.0));
#elif MOD_DOOM_AND_GLOOM
    if (doomAndGloomFog > 0.0001) {
        clouds.rgb = mix(clouds.rgb, vec3(0.5), clamp01(lViewPos / 900.0) * doomAndGloomFog);
    }
#endif