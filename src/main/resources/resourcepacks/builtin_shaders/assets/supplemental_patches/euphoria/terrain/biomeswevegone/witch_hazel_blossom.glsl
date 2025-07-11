float r = sqrt(pow2(signMidCoordPos.x) + pow2(0.5 + signMidCoordPos.y));
emission = smoothstep1(1 - r);