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

package com.a4z0.minefield.packet;

import com.a4z0.minefield.util.Packet;
import com.a4z0.minefield.util.Version;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;

public class SpawnNumberPacket extends Packet {

    private static final Class<?> WORLD_CLASS;
    private static final Class<?> ARMOR_STAND_CLASS;
    private static final Class<?> CRAFT_WORLD_CLASS;
    private static final Class<?> PACKET_SPAWN_ENTITY;
    private static final Class<?> CRAFT_CHAT_MESSAGE_CLASS;
    private static final Class<?> ICHAT_BASE_COMPONENT_CLASS;

    static {
        try {
            WORLD_CLASS = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.world.level.World" : "net.minecraft.server." + Version.getPacketName() + ".World");
            CRAFT_WORLD_CLASS = Class.forName("org.bukkit.craftbukkit." + Version.getPacketName() + ".CraftWorld");
            ARMOR_STAND_CLASS = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.world.entity.decoration.EntityArmorStand" : "net.minecraft.server." + Version.getPacketName() + ".EntityArmorStand");
            PACKET_SPAWN_ENTITY = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving" : "net.minecraft.server." + Version.getPacketName() + ".PacketPlayOutSpawnEntityLiving");
            CRAFT_CHAT_MESSAGE_CLASS = Class.forName("org.bukkit.craftbukkit." + Version.getPacketName() + ".util.CraftChatMessage");
            ICHAT_BASE_COMPONENT_CLASS = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.network.chat.IChatBaseComponent" : "net.minecraft.server." + Version.getPacketName() + ".IChatBaseComponent");
        }catch (ClassNotFoundException e) {
            throw new NullPointerException("Desired class not found");
        }
    }

    private final Object NMSObject;
    private final Object DataWatcher;
    private final int ID;

    /**
    * Construct a {@link SpawnNumberPacket}.
    *
    * @param Number Number to be invoked.
    * @param Location Number location.
    */

    public SpawnNumberPacket(int Number, @NotNull Location Location) {
        try {
            if(Location.getWorld() == null)
                throw new NullPointerException("World can't be null");

            Object World = CRAFT_WORLD_CLASS.cast(Location.getWorld());
            Object Handle = World.getClass().getMethod("getHandle").invoke(World);

            Object ArmorStand = ARMOR_STAND_CLASS.getConstructor(WORLD_CLASS, double.class, double.class, double.class).newInstance( Handle, Location.getBlockX() + 0.5, Location.getBlockY() - 0.75, Location.getBlockZ() + 0.5);

            if(!Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion())) {
                ArmorStand.getClass().getSuperclass().getMethod("setCustomName", String.class).invoke(ArmorStand, String.valueOf(Number));
                ArmorStand.getClass().getSuperclass().getMethod("setCustomNameVisible", boolean.class).invoke(ArmorStand, true);
                ArmorStand.getClass().getMethod("setInvisible", boolean.class).invoke(ArmorStand, true);
            }else{
                Object Name = CRAFT_CHAT_MESSAGE_CLASS.getMethod("fromStringOrNull", String.class).invoke(CRAFT_CHAT_MESSAGE_CLASS, String.valueOf(Number));

                ArmorStand.getClass().getMethod("a", ICHAT_BASE_COMPONENT_CLASS).invoke(ArmorStand, Name);
                ArmorStand.getClass().getMethod("n", boolean.class).invoke(ArmorStand, true);
                ArmorStand.getClass().getMethod("j", boolean.class).invoke(ArmorStand, true);
            }

            this.ID = (int) ArmorStand.getClass().getMethod(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "ae" : "getId").invoke(ArmorStand);
            this.DataWatcher = ArmorStand.getClass().getMethod(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "ai" : "getDataWatcher").invoke(ArmorStand);
            this.NMSObject = PACKET_SPAWN_ENTITY.getDeclaredConstructor(ARMOR_STAND_CLASS.getSuperclass()).newInstance(ArmorStand);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new NullPointerException("Can't spawn a Number");
        }
    }

    /**
    * @return the number ID.
    */

    public int getNumberID() {
        return this.ID;
    }

    /**
    * @return the number NMS DataWatcher.
    */

    public @NotNull Object getDataWatcher() {
        return this.DataWatcher;
    }

    @Override
    public @NotNull Object getNMSObject() {
        return this.NMSObject;
    }
}