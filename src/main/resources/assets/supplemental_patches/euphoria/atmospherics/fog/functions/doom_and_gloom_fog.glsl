void DoDoomAndGloomFog(inout vec3 color, float lViewPos) {
    float fog = lViewPos * FOG_INTENSITY;
    fog *= fog;
    fog = 1.0 - exp(-fog);

    color = mix(color, vec3(0.5, 0.5, 0.5), fog);
}