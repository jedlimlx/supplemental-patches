if (GetMaxColorDif(color.rgb) < 0.05) {
    emission = dot(color.rgb, color.rgb) * 0.6;
} else {
    emission = pow2(color.r) * 3.0;
}

color.rgb *= pow(color.rgb, vec3(0.3 * emission));