#ifdef (DARKER_END_ATMOSPHERE == 1 && defined MOD_ENDERSCAPE) || DARKER_END_ATMOSPHERE == 2
    vec3 ambientColorTint = 0.8 * vec3(0.6, 1.0, 0.6);
#else
    vec3 ambientColorTint = vec3(1.0);
#endif
vec3 ambientColor = ambientColorTint * mix(ambientTempC, vec3(END_AMBIENT_R_NEW, END_AMBIENT_G_NEW, END_AMBIENT_B_NEW), END_AMBIENT_INFLUENCE) * END_AMBIENT_I;
