#ifdef MOD_DOOM_AND_GLOOM
    if (doomAndGloomFog > 0.0001) {
        clouds.rgb = mix(clouds.rgb, vec3(0.5), clamp01(lViewPos / 900.0));
    }
#endif