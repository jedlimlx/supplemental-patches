if (color.b > 0.4 || CheckForColor(color.rgb, vec3(5, 85, 87))) {
	emission = 5.0;
	color.rgb *= sqrt1(GetLuminance(color.rgb));
}
