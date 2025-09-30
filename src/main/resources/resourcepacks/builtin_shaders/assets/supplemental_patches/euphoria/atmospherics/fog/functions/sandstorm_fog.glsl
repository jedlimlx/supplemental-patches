void DoSandstormFog(inout vec4 color, float lViewPos) {
    #ifdef MOD_YUNGSCAVEBIOMES
        float fog = lViewPos * yungSandstormFactor;
        fog = sqrt(fog) * YUNGS_SANDSTORM_FOG_INTENSITY;
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.8, 0.5, 0.1), fog);
    #endif
}