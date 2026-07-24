if (color.b - color.r > 0.05) {
	emission = 5.0;
	color.rgb *= sqrt1(sqrt1(GetLuminance(color.rgb)));
}
