package com.a4z0.minefield;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ProtoStackMagic {

    public static net.minecraft.server.v1_8_R3.ItemStack getNMS(ItemStack Item) {
        return CraftItemStack.asNMSCopy(Item);
    };

    public static NBTTagCompound getTags(net.minecraft.server.v1_8_R3.ItemStack NMS) {
        return NMS != null ? (NMS.hasTag() ? NMS.getTag() : new NBTTagCompound()) : new NBTTagCompound();
    };

}
