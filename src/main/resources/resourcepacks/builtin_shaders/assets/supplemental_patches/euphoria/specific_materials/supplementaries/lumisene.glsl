// pixelise the fresnel effect
#ifdef LUMISENE_IRIDESCENCE
	vec3 pWorldPos = floor(worldPos * 16) / 16;
	vec3 pViewPos = normalize(cameraPosition - pWorldPos);
	float fresnel = clamp(pViewPos.y, 0.0, 1.0);
#else
	float fresnel = 1.0;
#endif

#ifdef LUMISENE_PIXELATED_TEXTURE
	// varying thickness of oil layer
	vec2 warp = fbm2d_2d(0.1 * (pWorldPos.xz + vec2(0.3 * frameTimeCounter, 0.0)), 5);
	float noise = 0.5 * sin(1.5 * (pWorldPos.x + pWorldPos.z + 0.5 * (warp.x + warp.y) + 0.3 * frameTimeCounter)) + 0.5;
	noise = sqrt(noise);

	vec3 temp = pWorldPos + 0.1 * vec3(warp.x, 0.5 * frameTimeCounter, warp.y);
	noise += 0.5 * simplex(0.8 * temp);
	noise += 0.2 * simplex(1.6 * temp);
	noise += 0.05 * hash13(pWorldPos);
#endif

// using fresnel to obtain reflectiveness and transmittance
smoothnessG = 0.8 - 0.5 * fresnel;
reflectMult = smoothnessG;

float oilTexture = dot(color.rgb, color.rgb);
color.a = 0.5 + 0.1 * fresnel;
#ifdef LUMISENE_PIXELATED_TEXTURE
	color.rgb = oilColour(noise * fresnel);
#endif

translucentMultCalculated = true;
translucentMult.rgb = 1 - color.rgb;

highlightMult = 2.0;
emission = 0.6 * dot(color.rgb, color.rgb);
color.rgb = saturateColors(color.rgb, 2.0);
