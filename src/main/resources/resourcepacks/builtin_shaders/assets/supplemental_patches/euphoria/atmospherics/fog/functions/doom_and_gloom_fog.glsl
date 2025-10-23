void DoDoomAndGloomFog(inout vec4 color, float lViewPos, float fogOverride) {
    #if DOOM_AND_GLOOM_FOG == 1
        float fog = lViewPos * FOG_INTENSITY;
    #elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
        float fog = lViewPos * FOG_INTENSITY * doomAndGloomFog;
    #endif

    fog *= fog;
    fog = 1.0 - exp(-fog);

    color.rgb = mix(color.rgb, vec3(0.5), fog * (1 - fogOverride));
}