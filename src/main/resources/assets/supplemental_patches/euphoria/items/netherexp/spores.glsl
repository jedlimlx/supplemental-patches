float dotColor = dot(color.rgb, color.rgb);
emission = min(pow2(pow2(pow2(dotColor * 0.6))), 6.0) * 0.8 + 0.5;