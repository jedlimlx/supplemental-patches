float dotColor = dot(color.rgb, color.rgb);
if (color.r > color.b || dotColor > 2.9) {
	noDirectionalShading = true;
	overlayNoiseIntensity = 0.0;

	if (maxOf(abs(normal)) < 0.8) emission = 3.5;
	else emission = 1.7;

	color.rgb *= sqrt1(GetLuminance(color.rgb));
}
