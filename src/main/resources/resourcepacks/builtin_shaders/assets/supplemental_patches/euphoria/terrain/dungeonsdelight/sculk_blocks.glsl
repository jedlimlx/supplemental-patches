float boneFactor = max0(color.r * 1.25 - color.b);
if (boneFactor < 0.0001) emission = pow2(max0(color.g - color.r));

smoothnessG = min1(boneFactor * 1.7);
smoothnessD = smoothnessG;