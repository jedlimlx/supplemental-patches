vec3 nebulaViewPos = viewPos.xyz;
#if BLACK_HOLE > 0
	nebulaViewPos = GetBlackHoleLensedDirection(nViewPos, getBlackHoleDir());
#endif

color.rgb += GetEnderscapeNebula(nebulaViewPos, VdotU);
