/*
 *     Minefield
 *     Copyright (C) 2022.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.a4z0.minefield;

import com.a4z0.minefield.listener.MineListener;
import com.a4z0.minefield.listener.PickupEvent_V1_12;
import com.a4z0.minefield.listener.PickupEvent_V1_8;
import com.a4z0.minefield.packet.DestroyNumberPacket;
import com.a4z0.minefield.packet.NumberMetadataPacket;
import com.a4z0.minefield.util.Packet;
import com.a4z0.minefield.packet.SpawnNumberPacket;
import com.a4z0.minefield.util.Since;
import com.a4z0.minefield.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

/**
* A seed-based minesweeper.
*/

@Since(Version = Version.V1_8_R3) public final class Minefield extends JavaPlugin {

    private static final Map<Player, Collection<Integer>> lastIds = new HashMap<>();
    private static Minefield Instance;

    @Override
    public void onEnable() {
        Instance = this;

        if(!Version.canUse(this)) {
            this.getLogger().log(Level.WARNING, "Shutting down, running version isn't supported.");
            this.getPluginLoader().disablePlugin(this);

            return;
        }

        this.getServer().getPluginManager().registerEvents(new MineListener(), this);

        if(Version.canUse(PickupEvent_V1_12.class)) {
            this.getServer().getPluginManager().registerEvents(new PickupEvent_V1_12(), this);
        }else{
            this.getServer().getPluginManager().registerEvents(new PickupEvent_V1_8(), this);
        }

        for(Player Player : Bukkit.getOnlinePlayers()) {
            ItemStack Item;

            if(Version.getRunningVersion().equals(Version.V1_8_R3)) {
                Item = Player.getInventory().getItemInHand();
            }else {
                Item = Player.getInventory().getItemInMainHand();
            }

            if(!Item.getType().equals(Material.COMPASS)) continue;

            sendTrackedLocation(Player, Player.getLocation());

            MineListener.isTracking.add(Player);
        }
    }

    @Override
    public void onDisable() {
        for(Player Player : lastIds.keySet()) {
            removeTrackedLocation(Player);
        }
    }

    /**
    * @param Location Location to be verified.
    *
    * @return true if the location is a bomb.
    */

    public static boolean hasBomb(@NotNull Location Location) {
        int Value = Minefield.getLocationValue(Location);

        return (new Random(Value).nextFloat() < 0.1 && !MineListener.explodedBombs.contains(Value)); //10% of chance;
    }

    /**
    * @param Location Location to be verified.
    *
    * @return the unique location number based on the seed.
    */

    public static int getLocationValue(@NotNull Location Location) {
        if(Location.getWorld() == null)
            throw new NullPointerException("World can't be null");

        return (new Random(Location.getWorld().getSeed())).nextInt() * (Location.getBlockX() * Location.getBlockY() * Location.getBlockZ());
    }

    /**
    * @param Location Location to be verified.
    *
    * @return the number of bombs based on location.
    */

    public static int getLocationNumber(@NotNull Location Location) {
        int nearbyBombs = 0;

        for(int X = (Location.getBlockX() -1); X <= (Location.getBlockX() +1); X++) {
            for(int Z = (Location.getBlockZ() -1); Z <= (Location.getBlockZ() +1); Z++) {
                Location maybeBomb = new Location(Location.getWorld(), X, Location.getBlockY(), Z);

                if(hasBomb(maybeBomb)) nearbyBombs++;
            }
        }

        return nearbyBombs;
    }

    /**
    * Sends location with number to player.
    *
    * @param Player Player who will be sent the numbers.
    * @param Location Location where the numbers will be added.
    */

    public static void sendTrackedLocation(@NotNull Player Player, @NotNull Location Location) {
        if(!lastIds.containsKey(Player))
            lastIds.put(Player, new HashSet<>());

        removeTrackedLocation(Player);

        Location L1 = Location.clone().subtract(0, 1, 0);

        for(int X = (L1.getBlockX() -1); X <= (L1.getBlockX() +1); X++) {
            for(int Z = (L1.getBlockZ() - 1); Z <= (L1.getBlockZ() + 1); Z++) {
                Location L2 = new Location(Player.getWorld(), X, L1.getBlockY(), Z);

                while(!L2.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
                    L2.setY(L2.getY() + 1);
                }

                if(L1.getBlockX() == L2.getBlockX() && L1.getBlockY() == L2.getBlockY() && L1.getBlockZ() == L2.getBlockZ()) continue;

                SpawnNumberPacket Number = new SpawnNumberPacket(getLocationNumber(L2), L2);
                NumberMetadataPacket Metadata = new NumberMetadataPacket(Number.getNumberID(), Number.getDataWatcher(), false);

                Packet.sendPacket(Player, Number);
                Packet.sendPacket(Player, Metadata);

                lastIds.get(Player).add(Number.getNumberID());
            }
        }
    }

    /**
    * Removes player numbered locations.
    *
    * @param Player Player to remove the numbers.
    */

    public static void removeTrackedLocation(@NotNull Player Player) {
        if(!lastIds.containsKey(Player)) return;

        for(int ID : lastIds.get(Player).toArray(new Integer[0])) {
            DestroyNumberPacket Number = new DestroyNumberPacket(ID);

            Packet.sendPacket(Player, Number);

            lastIds.get(Player).remove(ID);
        }
    }

    /**
    * @return an instance of {@link Minefield}.
    */

    public static @NotNull Minefield getInstance() {
        if(Instance == null)
            throw new NullPointerException("You can't call this before [Minefield] is enabled");

        return Instance;
    }
}