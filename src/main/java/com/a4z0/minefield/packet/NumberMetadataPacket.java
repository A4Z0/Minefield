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

public class NumberMetadataPacket extends Packet {

    private static final Class<?> PACKET_ENTITY_METADATA_CLASS;

    static {
        try {
            PACKET_ENTITY_METADATA_CLASS = Class.forName(Version.V1_16_R1.isEqualOrNewer(Version.getRunningVersion()) ? "net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata" : "net.minecraft.server." + Version.getPacketName() + ".PacketPlayOutEntityMetadata");
        }catch (ClassNotFoundException e) {
            throw new NullPointerException("Desired class not found");
        }
    }

    private final Object NBTObject;

    /**
    * Construct a {@link NumberMetadataPacket}.
    *
    * @param ID Number ID to send the metadata.
    * @param DataWatcher Number DataWatcher.
    * @param updateAll true for force update all.
    */

    public NumberMetadataPacket(int ID, Object DataWatcher, boolean updateAll) {
        try {
            this.NBTObject = PACKET_ENTITY_METADATA_CLASS.getDeclaredConstructor(int.class, DataWatcher.getClass(), boolean.class).newInstance(ID, DataWatcher, updateAll);
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new NullPointerException("Can't create Number metadata");
        }
    }

    @Override
    public @NotNull Object getNMSObject() {
        return this.NBTObject;
    }
}