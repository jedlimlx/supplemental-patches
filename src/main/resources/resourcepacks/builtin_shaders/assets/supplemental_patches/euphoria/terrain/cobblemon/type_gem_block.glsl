float factor;
if (mat % 64 < 32) {
    if (mat % 64 < 16) {
        if (mat % 64 < 8) {
            if (mat % 64 < 4) {
                if (mat % 64 < 2) {
                    // 0 bug
                    factor = color.g;
                } else {
                    // 2 dark
                    factor = color.b;
                }
            } else {
                if (mat % 64 < 6) {
                    // 4 dragon
                    factor = color.b;
                } else {
                    // 6 electric
                    factor = color.g;
                }
            }
        } else {
            if (mat % 64 < 12) {
                if (mat % 64 < 10) {
                    // 8 fairy
                    factor = (color.r + color.b) / 2;
                } else {
                    // 10 fighting
                    factor = color.r;
                }
            } else {
                if (mat % 64 < 14) {
                    // 12 fire
                    factor = color.r;
                } else {
                    // 14 flying
                    factor = color.b;
                }
            }
        }
    } else {
        if (mat % 64 < 24) {
            if (mat % 64 < 20) {
                if (mat % 64 < 18) {
                    // 16 ghost
                    factor = color.b;
                } else {
                    // 18 grass
                    factor = color.g;
                }
            } else {
                if (mat % 64 < 22) {
                    // 20 ground
                    factor = (color.r + color.g) / 2;
                } else {
                    // 22 ice
                    factor = color.b;
                }
            }
        } else {
            if (mat % 64 < 28) {
                if (mat % 64 < 26) {
                    // 24 normal
                    factor = (color.r + color.g + color.b) / 3;
                } else {
                    // 26 poison
                    factor = color.b;
                }
            } else {
                if (mat % 64 < 30) {
                    // 28 psychic
                    factor = color.r;
                } else {
                    // 30 rock
                    factor = (color.r + color.g + color.b) / 3;
                }
            }
        }
    }
} else if (mat % 64 == 32) {
    // 32 steel
    factor = color.b;
} else {
    // 34 water
    factor = color.b;
}

#include "/lib/materials/specificMaterials/terrain/typeGemBlock.glsl"
