package com.a4z0.minefield;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;

public final class Minefield extends JavaPlugin {

    public static Collection<Location> Edges = new HashSet<>();
    public static Collection<Location> Bombs = new HashSet<>();

    public static Collection<Integer> Ids = new HashSet<>();

    @Override
    public void onEnable() {

        if(!Version.getRunningVersion().equals(Version.V1_8_R3)) {
            this.getLogger().log(Level.WARNING, "Use in 1.8.8 [1_8_R3]");
            this.getPluginLoader().disablePlugin(this);

            return;
        }

        this.getServer().getPluginManager().registerEvents(new Land(), this);
    }

    public static void genBombs() {
        for(Player Player : Bukkit.getOnlinePlayers()) {
            Location BL = Player.getWorld().getHighestBlockAt(
                    Player.getLocation().getBlockX(),
                    Player.getLocation().getBlockZ()
            ).getLocation().subtract(0, 1, 0);

            Square Square = new Square(
                    Player.getWorld(),
                    BL.clone().add(8, 0, 0).getBlockX(),
                    BL.clone().add(0, 0, -8).getBlockZ(),
                    BL.clone().add(-8, 0, 0).getBlockX(),
                    BL.clone().add(0, 0, 8).getBlockZ()
            );

            for(Block Block : Square.getBlocks()) {
                Edges.add(Block.getLocation());
            }

            for(Location Location : Edges) {
                int R = (new Random(Player.getWorld().getSeed())).nextInt() * (Location.getBlockX() * Location.getBlockZ());

                if(new Random(R).nextInt() >= 0x1 >> 8) {
                    Bombs.add(Location);
                }
            }

            v1_8_P.send(Player);

            for(Location Bomb : Bombs.toArray(new Location[0])) {
                if(BL.equals(Bomb)) {
                    TNTPrimed TNT = (TNTPrimed) Player.getWorld().spawnEntity(BL, EntityType.PRIMED_TNT);
                    TNT.setFuseTicks(0);
                    Ids.add(TNT.getEntityId());

                    Bombs.remove(Bomb);
                }
            }
        }

        Edges.clear();
    }
}