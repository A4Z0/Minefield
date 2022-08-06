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
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class DestroyNumberPacket extends Packet {

    private static final Class<?> PACKET_ENTITY_DESTROY;

    static {
        try {
            PACKET_ENTITY_DESTROY = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy" : "net.minecraft.server." + Version.getPacketName() + ".PacketPlayOutEntityDestroy");
        }catch (ClassNotFoundException e) {
            throw new NullPointerException("Desired class not found");
        }
    }

    private final Object NMSObject;

    /**
    * Construct a {@link DestroyNumberPacket}.
    *
    * @param ID ID of the number to be destroyed.
    */

    public DestroyNumberPacket(int... ID) {
        try {
            this.NMSObject = PACKET_ENTITY_DESTROY.getConstructor(int[].class).newInstance(ID);
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NullPointerException("Can't destroy a number");
        }
    }

    @Override
    public @NotNull Object getNMSObject() {
        return this.NMSObject;
    }
}