color.rgb *= sqrt2(color.rgb);

emission = 0.9 * GetLuminance(color.rgb) * emissiveness;
emission += pow2(color.b) * 0.15 * emissiveness;