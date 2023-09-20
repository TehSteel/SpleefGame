package com.github.tehsteel.spleef.game.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;


public record SavedBlock(Material material, BlockData blockData, Location location) {

}
