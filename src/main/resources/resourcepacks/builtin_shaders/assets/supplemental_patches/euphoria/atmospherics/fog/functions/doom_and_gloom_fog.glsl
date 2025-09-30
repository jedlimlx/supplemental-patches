void DoDoomAndGloomFog(inout vec4 color, float lViewPos) {
    #ifdef DO_DOOM_AND_GLOOM_FOG
        float fog = lViewPos * FOG_INTENSITY;
        fog *= fog;
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.5), fog);
    #elif MOD_DOOM_AND_GLOOM
        float fog = lViewPos * FOG_INTENSITY * doomAndGloomFog;
        fog *= fog;
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.5), fog);
    #endif
}