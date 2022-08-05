package com.a4z0.minefield;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class v1_8_P {

    private static final Map<Player, Collection<EntityArmorStand>> Entities = new HashMap<>();

    public static void send(@NotNull Player Player) {
        if(Entities.containsKey(Player)) {
            for(EntityArmorStand Armor : Entities.get(Player)) {
                PacketPlayOutEntityDestroy Packet = new PacketPlayOutEntityDestroy(Armor.getId());

                ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(Packet);
            }
        }

        Location BL = Player.getWorld().getHighestBlockAt(
                Player.getLocation().getBlockX(),
                Player.getLocation().getBlockZ()
        ).getLocation().subtract(0, 1, -1);

        Location A = BL.clone().add(1, 0, 0);
        Location B = BL.clone().add(-1, 0, 0);
        Location C = BL.clone().add(0, 0, 1);
        Location D = BL.clone().add(0, 0, -1);
        Location E = BL.clone().add(1, 0, 1);
        Location F = BL.clone().add(-1, 0, -1);
        Location G = BL.clone().add(1, 0, -1);
        Location H = BL.clone().add(-1, 0, 1);

        Map<Location, Numbers> quantity = new HashMap<>();


            for(Location BB : new Location[]{A, B, C, D, E, F, G, H}) {
                int b = 0;

                for(Location Bomb : Minefield.Bombs) {
                    if (BB.getBlockX() + 1 == Bomb.getBlockX() && BB.getBlockZ() == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() - 1 == Bomb.getBlockX() && BB.getBlockZ() == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() == Bomb.getBlockX() && BB.getBlockZ() + 1 == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() == Bomb.getBlockX() && BB.getBlockZ() - 1 == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() +1 == Bomb.getBlockX() && BB.getBlockZ() -1 == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() -1 == Bomb.getBlockX() && BB.getBlockZ() +1 == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() +1 == Bomb.getBlockX() && BB.getBlockZ() +1 == Bomb.getBlockZ()) {
                        b++;
                    }
                    if (BB.getBlockX() -1 == Bomb.getBlockX() && BB.getBlockZ() -1 == Bomb.getBlockZ()) {
                        b++;
                    }
                }

                switch (b) {
                    case 2 -> quantity.put(BB, Numbers.TWO);
                    case 3 -> quantity.put(BB, Numbers.THREE);
                    case 4 -> quantity.put(BB, Numbers.FOUR);
                    case 5 -> quantity.put(BB, Numbers.FIVE);
                    case 6 -> quantity.put(BB, Numbers.SIX);
                    case 7 -> quantity.put(BB, Numbers.SEVEN);
                    case 8 -> quantity.put(BB, Numbers.EIGHT);
                    default -> quantity.put(BB, Numbers.ONE);
                }
        }

        Entities.put(Player, new HashSet<>());

        quantity.forEach((Location, Number) -> {
            EntityArmorStand Armor = new EntityArmorStand(((CraftWorld) Player.getWorld()).getHandle());
            Armor.setLocation(Location.getBlockX(), Location.getBlockY() - 0.5, Location.getBlockZ(), 0, -90);
            Armor.setInvisible(false);

            PacketPlayOutSpawnEntityLiving Packet = new PacketPlayOutSpawnEntityLiving(Armor);
            PacketPlayOutEntityEquipment P2 = new PacketPlayOutEntityEquipment(Armor.getId(), 4, ProtoStackMagic.getNMS(Number.getItem()));
            PacketPlayOutEntityHeadRotation P3 = new PacketPlayOutEntityHeadRotation(Armor, (byte) 1);

            ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(Packet);
            ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(P2);
            ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(P3);


            Entities.get(Player).add(Armor);
        });
    }
}