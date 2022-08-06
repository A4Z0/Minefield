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

package com.a4z0.minefield.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
* Represents a Packet.
*/

public abstract class Packet {

    private static final Class<?> NMSClass;

    static {
        try {
            NMSClass = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.network.protocol.Packet" : "net.minecraft.server." + Version.getPacketName() + ".Packet");
        }catch (ClassNotFoundException e) {
            throw new NullPointerException("Desired class not found");
        }
    }

    /**
    * @return NMS Objects.
    */

    public abstract @NotNull Object getNMSObject();

    /**
    * Sends a Packet to a player.
    *
    * @param Player Player that the packet will be sent.
    * @param Packet Packet that will be sent to the player.
    */

    public static void sendPacket(@NotNull Player Player, @NotNull Packet Packet) {
        try {
            Object Handle = Player.getClass().getMethod("getHandle").invoke(Player);

            Object Connection = Handle.getClass().getField(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "b" : "playerConnection").get(Handle);

            Connection.getClass().getMethod(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "a" : "sendPacket", NMSClass).invoke(Connection, Packet.getNMSObject());
        }catch(NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}