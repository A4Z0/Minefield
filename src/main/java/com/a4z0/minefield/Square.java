package com.a4z0.minefield;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class Square {

    private final World World;
    private final int X1, Z1, X2, Z2;

    public Square(World World, int X1, int Z1, int X2, int Z2) {
        this.World = World;
        this.X1 = Math.min(X1, X2);
        this.X2 = Math.max(X1, X2);
        this.Z1 = Math.min(Z1, Z2);
        this.Z2 = Math.max(Z1, Z2);
    }

    public @NotNull Collection<Block> getBlocks() {
        Collection<Block> Blocks = new HashSet<>();

        for(int X = this.X1; X < this.X2; X++) {
            for(int Z = this.Z1; Z < this.Z2; Z++) {
                Blocks.add(this.World.getHighestBlockAt(X, Z).getLocation().subtract(0, 1, 0).getBlock());
            }
        }

        return Blocks;
    }
}