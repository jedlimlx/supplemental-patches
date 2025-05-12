subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

emission = 2.0 * pow2(5.0 * max0(color.r - 0.8));
color.rgb = pow1_5(color.rgb);