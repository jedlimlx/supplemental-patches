float dotColor = dot(color.rgb, color.rgb);
smoothnessG = 0.3;
smoothnessD = smoothnessG;

emission = dotColor * 0.4;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif