if (mat % 4 == 2)
	emission = pow2(pow2(color.r)) * 4.0 + 0.15;

if (color.r < 0.9) {
	smoothnessG = 6.2 * pow2(pow2(color.r)) + 0.2 * color.r + 0.05;
	smoothnessD = smoothnessG;
}
