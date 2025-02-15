package io.github.jedlimlx.supplemental_patches.shaders


object MaterialShaders {
}

object BlockEntityShaders {
}

object TranslucentShaders {
}

object ItemShaders {
}

object EntityShaders {
}

object ParticleShaders {
    val GENERIC_PARTICLE = ShaderBuilder(
        name = "generic_particle",
        // language=glsl
        glsl = """
            float dotColor = dot(color.rgb, color.rgb);
            if (dotColor > 0.25 && color.g < 0.5 && (color.b > color.r * 1.1 && color.r > 0.3 || color.r > (color.g + color.b) * 3.0)) {
                // Ender Particle, Crying Obsidian Particle, Redstone Particle
                emission = clamp(color.r * 8.0, 1.6, 5.0);
                color.rgb = pow1_5(color.rgb);
                lmCoordM = vec2(0.0);
                #if defined NETHER && defined BIOME_COLORED_NETHER_PORTALS
                    if (color.b > color.r * color.r && color.g < 0.16 && color.r > 0.2) color.rgb = changeColorFunction(color.rgb, 10.0, netherColor, 1.0); // Nether Portal
                #endif
            } else if (color.r > 0.83 && color.g > 0.23 && color.b < 0.4) {
                // Lava Particles
                emission = 2.0;
                color.b *= 0.5;
                color.r *= 1.2;
                color.rgb += vec3(min(pow2(pow2(emission * 0.35)), 0.4)) * LAVA_TEMPERATURE * 0.5;
                emission *= LAVA_EMISSION;
                #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
                    color.rgb = changeColorFunction(color.rgb, 3.5, colorSoul, inSoulValley);
                #endif
                #ifdef PURPLE_END_FIRE_INTERNAL
                    color.rgb = changeColorFunction(color.rgb, 3.5, colorEndBreath, 1.0);
                #endif
            } else if (abs(color.r - 0.07) < 0.02 && abs(color.g - 0.93) < 0.02 && abs(color.b - 0.84) < 0.02) {
                emission = 5.0;
                color.rgb = pow1_5(color.rgb);
                lmCoordM = vec2(0.0);
            }
        """.trimIndent(),
        mat0 = (0..7).map { "minecraft:generic_$it" }
    ).register(PARTICLES)
}
