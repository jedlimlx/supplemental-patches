vec3 PlayerToView(vec3 pos) {
	return mat3(gbufferModelView) * (pos - gbufferModelViewInverse[3].xyz);
}
