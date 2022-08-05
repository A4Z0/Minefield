package com.a4z0.minefield;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Land implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Minefield.genBombs();
    }

    @EventHandler
    private void onSpawn(PlayerRespawnEvent e) {
        Minefield.genBombs();
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Minefield.genBombs();
    }

    @EventHandler
    private void onExplode(EntityExplodeEvent e) {
        if(e.getEntity() instanceof TNTPrimed) {
            for(int ID : Minefield.Ids) {
                if(e.getEntity().getEntityId() == ID) {
                    e.blockList().clear();
                }
            }
        }
    }
}