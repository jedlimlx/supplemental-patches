#ifdef MOD_SCORCHFUL
    vec3 sandstormM = mix(vec3(0.0), mix(vec3(0.4, 0.05, -0.4), vec3(0.25, 0.05, -0.25), hasRegularSandstorm), hasSandstorm);
#else
    vec3 sandstormM = vec3(0.0);
#endif