emission = 2.0 + 0.5 * color.r;
color.rgb = pow(color.rgb, vec3(1.0 + 0.6 * sqrt(emission)));