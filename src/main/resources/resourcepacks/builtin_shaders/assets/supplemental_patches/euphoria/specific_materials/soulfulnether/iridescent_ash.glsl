float fresnel = clamp(1.0 + dot(normalM, normalize(viewPos)), 0.0, 1.0);
float iridescence = 12.0 * GetMaxColorDif(color.rgb) * pow3(fresnel);

// TODO work on better looking iridescence
// vec3 newColor = hsv2rgb(rgb2hsv(color.rgb) + vec3(2.0, 0, 0));
// color.rgb = changeColorFunction(color.rgb, fresnel, newColor, smoothstep1());
color.rgb = saturateColors(color.rgb, min(1.0 + 4.0 * iridescence, 3.0));