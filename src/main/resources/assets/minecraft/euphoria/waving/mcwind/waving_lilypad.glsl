float phase = mcw_hash(floor(blockCenter.xz)) * 6.28318531;
playerPos.y += sin(mcw_windPhase * 1.6 + phase) * 0.02 * (0.4 + 0.6 * mcw_windMag(blockCenter));
