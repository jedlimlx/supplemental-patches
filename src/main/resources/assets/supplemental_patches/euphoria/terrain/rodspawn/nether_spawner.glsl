float factor = smoothstep1(min1(color.r * 1.5));
factor = factor > 0.12 ? factor : factor * 0.5;
smoothnessG = factor;
smoothnessD = factor;

if (mat % 4 == 0) {  // not ominous
    if (color.r > color.b * 10 || color.r > 0.75) {
        emission = dot(color.rgb, color.rgb) * 2.5;
        color.rgb *= color.rgb;
    }
} else if (mat % 4 == 2) {  // ominous
    if (color.b > 0.4) {
        emission = pow1_5(color.b) * 1.7 + 0.35 * pow1_5(1 - color.b);
        color.rgb *= GetLuminance(sqrt1(color.rgb));
    }
}