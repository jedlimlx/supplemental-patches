#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz - vec3(0, 0.0625, 0));
    if ((fractPos.y > 0.5 && abs(NdotU) < 0.1) || (fractPos.y > 0.475 && fractPos.y < 0.5 && NdotU < -0.1)) {
        #include "/lib/materials/specificMaterials/terrain/pumpkin.glsl"
        noSmoothLighting = true, noDirectionalShading = true;
        lmCoordM.y = 0.0;
        lmCoordM.x = 1.0;

        if (color.b > 0.28 && color.r > 0.9) {
            float factor = pow2(color.g);
            emission = pow2(factor) * factor * 5.0;
            #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
                color.rgb = changeColorFunction(color.rgb, 1.5, colorSoul, inSoulValley);
            #endif
            #ifdef PURPLE_END_FIRE_INTERNAL
                color.rgb = changeColorFunction(color.rgb, 1.5, colorEndBreath, 1.0);
            #endif
        }

        overlayNoiseIntensity = 0.3;

        #ifdef SPOOKY
            float noiseAdd = 0.0;
            #ifdef GBUFFERS_TERRAIN
                noiseAdd = hash13(mod(floor(worldPos + atMidBlock / 64) + frameTimeCounter * 0.000001, vec3(100)));
            #endif
            emission *= mix(0.0, 1.0, smoothstep(0.2, 0.9, texture2DLod(noisetex, vec2(frameTimeCounter * 0.025 + noiseAdd), 0.0).r));
        #endif
    }
#endif