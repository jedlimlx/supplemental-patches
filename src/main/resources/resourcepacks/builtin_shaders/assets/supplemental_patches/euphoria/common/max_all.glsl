// removed in Complementary Shaders 5.8 / Euphoria Patches 1.9 for some reason
float maxAll(vec2 x) {
	return max(x.x, x.y);
}

float maxAll(vec3 x) {
	return max(x.x, max(x.y, x.z));
}

float maxAll(vec4 x) {
	return max(x.x, max(x.y, max(x.z, x.w)));
}
