vec3 oilColour(float t) {
	t = fract(t);

	const int N = 15;
	vec3 colors[N] = vec3[](
		vec3(1.0, 1.0, 1.0),
		vec3(1.0, 0.96, 0.81),
		vec3(1.0, 0.89, 0.83),
		vec3(1.0, 0.75, 0.89),
		vec3(0.91, 0.91, 1.0),
		vec3(0.90, 1.0, 1.0),
		vec3(0.91, 0.91, 1.0),
		vec3(1.0, 0.75, 0.89),
		vec3(1.0, 0.89, 0.83),
		vec3(1.0, 0.96, 0.81),
		vec3(1.0, 0.89, 0.83),
		vec3(1.0, 0.75, 0.89),
		vec3(1.0, 0.89, 0.83),
		vec3(1.0, 0.96, 0.81),
		vec3(1.0, 1.0, 1.0)
	);

	float scaled = t * float(N - 1);
	int i = int(floor(scaled));
	float f = fract(scaled);

	vec3 a = colors[i];
	vec3 b = colors[(i + 1) % N];

	f = f * f * (3.0 - 2.0 * f);
	return mix(a, b, f);
}
