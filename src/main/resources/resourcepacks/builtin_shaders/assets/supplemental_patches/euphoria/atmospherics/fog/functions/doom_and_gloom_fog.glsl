void DoDoomAndGloomFog(inout vec3 color, float lViewPos) {
    #ifdef DO_DOOM_AND_GLOOM_FOG
        float fog = lViewPos * FOG_INTENSITY;
    #else
        float fog = lViewPos * FOG_INTENSITY * doomAndGloomFog;
    #endif
    fog *= fog;
    fog = 1.0 - exp(-fog);

    color = mix(color, vec3(0.5, 0.5, 0.5), fog);
}