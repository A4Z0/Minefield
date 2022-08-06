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

package com.a4z0.minefield.listener;

import com.a4z0.minefield.Minefield;
import com.a4z0.minefield.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
* Listens for Join, Kill, Respawn, Movement, Swap, Drop and Click events.
*/

public class MineListener implements Listener {

    private static final Collection<Player> alreadyIn = new HashSet<>();
    public static final Collection<Player> isTracking = new HashSet<>();
    private static final Map<Player, Location> lastBlock = new HashMap<>();
    public static final Collection<Integer> explodedBombs = new HashSet<>();

    @EventHandler
    private void Join(PlayerJoinEvent e) {
        Player Player = e.getPlayer();

        if(alreadyIn.contains(Player)) return;

        Bukkit.getScheduler().runTaskLater(Minefield.getInstance(), () -> {
            Player.sendMessage(ChatColor.RED + "Look around and be careful...");
        }, 2);

        alreadyIn.add(Player);

        ItemStack Item;

        if(Version.getRunningVersion().equals(Version.V1_8_R3)) {
            Item = Player.getInventory().getItemInHand();
        }else{
            Item = Player.getInventory().getItemInMainHand();
        }

        if(Item.getType().equals(Material.COMPASS)) {
            isTracking.add(Player);

            Minefield.sendTrackedLocation(Player, Player.getLocation());

            return;
        }

        isTracking.remove(Player);
        Minefield.removeTrackedLocation(Player);
    }

    @EventHandler
    private void Kill(PlayerDeathEvent e) {
        isTracking.remove(e.getEntity());
    }

    @EventHandler
    private void Respawn(PlayerRespawnEvent e) {
        Player Player = e.getPlayer();

        Minefield.removeTrackedLocation(Player);

        ItemStack Item;

        if(Version.getRunningVersion().equals(Version.V1_8_R3)) {
            Item = Player.getInventory().getItemInHand();
        }else{
            Item = Player.getInventory().getItemInMainHand();
        }

        if(Item.getType().equals(Material.COMPASS)) {
            Minefield.sendTrackedLocation(Player, Player.getLocation());

            isTracking.add(Player);
        }

        Player.sendMessage(ChatColor.RED + "Be careful when walking.");
    }

    @EventHandler
    private void Movement(PlayerMoveEvent e) {

        Location L1 = e.getFrom();
        Location L2 = e.getTo();

        if(L2 == null) return;

        if(!(L1.getBlockX() != L2.getBlockX() || L1.getBlockY() != L2.getBlockY() || L1.getBlockZ() != L2.getBlockZ())) return;

        Player Player = e.getPlayer();
        Location Location = L2.clone().subtract(0, 1, 0);

        if(lastBlock.containsKey(Player) && lastBlock.get(Player).equals(Location)) return;

        Minefield.removeTrackedLocation(Player);

        if(isTracking.contains(Player)) {
            Minefield.sendTrackedLocation(Player, e.getTo());
        }

        lastBlock.put(Player, Location);

        if(Minefield.hasBomb(Location)) {
            explodedBombs.add(Minefield.getLocationValue(Location));

            TNTPrimed TNT = Player.getWorld().spawn(Location.add(0, 1, 0), TNTPrimed.class);
            TNT.setFuseTicks(0);
            TNT.setIsIncendiary(true);

            if(isTracking.contains(Player)) {
                Minefield.removeTrackedLocation(Player);
                Minefield.sendTrackedLocation(Player, e.getTo());
            }
        }
    }

    @EventHandler
    private void Swap(PlayerItemHeldEvent e) {
        Player Player = e.getPlayer();

        ItemStack Item = Player.getInventory().getItem(e.getNewSlot());

        if(Item != null && Item.getType().equals(Material.COMPASS)) {
            isTracking.add(Player);

            Minefield.sendTrackedLocation(Player, Player.getLocation());

            return;
        }

        isTracking.remove(Player);
        Minefield.removeTrackedLocation(Player);
    }

    @EventHandler
    private void Drop(PlayerDropItemEvent e) {
        Player Player = e.getPlayer();

        ItemStack Item;

        if(Version.getRunningVersion().equals(Version.V1_8_R3)) {
            Item = Player.getInventory().getItemInHand();
        }else{
            Item = Player.getInventory().getItemInMainHand();
        }

        if(Item.getType().equals(Material.COMPASS)) {
            isTracking.add(Player);

            Minefield.sendTrackedLocation(Player, Player.getLocation());

            return;
        }

        isTracking.remove(Player);
        Minefield.removeTrackedLocation(Player);
    }

    @EventHandler
    private void Click(InventoryClickEvent e) {

        if(!(e.getWhoClicked() instanceof Player)) return;

        Player Player = (Player) e.getWhoClicked();

        Bukkit.getScheduler().runTaskLater(Minefield.getInstance(), () -> {
            ItemStack Item;

            if(Version.getRunningVersion().equals(Version.V1_8_R3)) {
                Item = Player.getInventory().getItemInHand();
            }else{
                Item = Player.getInventory().getItemInMainHand();
            }

            if(Item.getType().equals(Material.COMPASS)) {
                MineListener.isTracking.add(Player);

                Minefield.sendTrackedLocation(Player, Player.getLocation());

                return;
            }

            MineListener.isTracking.remove(Player);
            Minefield.removeTrackedLocation(Player);
        }, 2);
    }
}