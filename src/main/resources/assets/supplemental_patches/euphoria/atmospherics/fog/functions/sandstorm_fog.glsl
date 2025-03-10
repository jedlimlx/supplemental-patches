void DoSandstormFog(inout vec3 color, float lViewPos) {
    float fog = lViewPos * sandstormFactor;
    fog = sqrt(fog) * SANDSTORM_FOG_INTENSITY;
    fog = 1.0 - exp(-fog);

    color = mix(color, vec3(0.8, 0.5, 0.1), fog);
}