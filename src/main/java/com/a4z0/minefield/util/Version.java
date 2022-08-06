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

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
* Enumeration of Bukkit versions.
*/

public enum Version {
    UNKNOWN,

    V1_8_R3,

    V1_9_R1,
    V1_9_R2,
    V1_10_R1,
    V1_11_R1,
    V1_12_R1,
    V1_13_R1,
    V1_13_R2,
    V1_14_R1,
    V1_15_R1,

    V1_16_R1,
    V1_16_R2,
    V1_16_R3,
    V1_17_R1,
    V1_18_R1,
    V1_18_R2,
    V1_19_R1;

    /**
    * @param Version Version to be compared.
    *
    * @return true if the given version is the equal or newer.
    */

    public boolean isEqualOrNewer(@NotNull Version Version) {
        return Version.ordinal() >= this.ordinal();
    }

    /**
    * @return the packet name based on the running version.
    */

    public static @NotNull String getPacketName() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    /**
    * @return the running version.
    */

    public static @NotNull Version getRunningVersion() {
        for(Version Version : Version.values()) {
            if(getPacketName().contains(Version.name().substring(1))) return Version;
        }

        return Version.UNKNOWN;
    }

    /**
    * @param Clazz Class with its declared version.
    *
    * @return true if the class can be used in the running version.
    */

    public static boolean canUse(@NotNull Class<?> Clazz) {
        if(Clazz.isAnnotationPresent(Since.class)) {
            return Clazz.getAnnotation(Since.class).Version().isEqualOrNewer(getRunningVersion());
        }

        return true;
    }

    /**
    * @param Object Object with its declared version.
    *
    * @return true if the object can be used in the running version.
    */

    public static boolean canUse(@NotNull Object Object) {
        return canUse(Object.getClass());
    }
}