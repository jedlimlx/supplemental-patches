if (abs(dot(textureRad, vec2(atlasSize.x, -atlasSize.y))) < 4.5)
    storeToAllFaces = true;
else return;