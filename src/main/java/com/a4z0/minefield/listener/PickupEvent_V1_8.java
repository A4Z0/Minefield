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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

/**
* Listen to Pickup event.
*/

public class PickupEvent_V1_8 implements Listener {

    @EventHandler
    private void Pickup(PlayerPickupItemEvent e) {
        Player Player = e.getPlayer();

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
        }, 3);
    }
}