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
import com.a4z0.minefield.util.Since;
import com.a4z0.minefield.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

/**
* Listen to Pickup event.
*/

@Since(Version = Version.V1_12_R1) public class PickupEvent_V1_12 implements Listener {

    @EventHandler
    private void Pickup(EntityPickupItemEvent e) {

        if(!(e.getEntity() instanceof Player)) return;

        Player Player = (Player) e.getEntity();

        Bukkit.getScheduler().runTaskLater(Minefield.getInstance(), () -> {
            ItemStack Item = Player.getInventory().getItemInMainHand();

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