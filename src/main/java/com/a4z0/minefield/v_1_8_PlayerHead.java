package com.a4z0.minefield;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class v_1_8_PlayerHead {

    public static ItemStack Create(String Displayname) {
        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        ItemMeta Meta = Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Item.setItemMeta(Meta);

        return Item;
    };

    public static ItemStack Create(Player Player, String Displayname) {

        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        SkullMeta Meta = (SkullMeta) Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Meta.setOwner(Player.getName());

        Item.setItemMeta(Meta);

        return Item;
    };

    public static ItemStack Create(Player Player, String Displayname, String TID) {

        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        SkullMeta Meta = (SkullMeta) Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        Meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        Meta.setOwner(Player.getName());

        Item.setItemMeta(Meta);

        net.minecraft.server.v1_8_R3.ItemStack NMS = ProtoStackMagic.getNMS(Item);

        NBTTagCompound Tags = ProtoStackMagic.getTags(NMS);

        Tags.setString("ID", TID);

        NMS.setTag(Tags);

        return CraftItemStack.asBukkitCopy(NMS);
    };

    public static ItemStack Create(Player Player, String Displayname, String TID, List<String> Lore) {

        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        SkullMeta Meta = (SkullMeta) Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        Meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        List<String> NLore = new ArrayList<>();

        Lore.forEach(Line -> {
            NLore.add(Line);
        });

        Meta.setLore(NLore);

        Meta.setOwner(Player.getName());

        Item.setItemMeta(Meta);

        net.minecraft.server.v1_8_R3.ItemStack NMS = ProtoStackMagic.getNMS(Item);

        NBTTagCompound Tags = ProtoStackMagic.getTags(NMS);

        Tags.setString("ID", TID);

        NMS.setTag(Tags);

        return CraftItemStack.asBukkitCopy(NMS);
    };

    public static ItemStack Create(String Texture, String Displayname, String TID) {

        Texture = "http://textures.minecraft.net/texture/" + Texture;

        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        SkullMeta Meta = (SkullMeta) Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        Meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        StringBuilder SUUID = new StringBuilder("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");

        for(int i = 0; i < SUUID.length(); i++) {
            if(SUUID.charAt(i) != '-') {
                SUUID.setCharAt(i, Texture.charAt(i + 38));
            };
        };

        GameProfile Profile = new GameProfile(UUID.fromString(SUUID.toString()), null);

        byte[] Bytes = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", Texture).getBytes());

        Profile.getProperties().put("textures", new Property("textures", new String(Bytes)));

        try {
            Field Field = Meta.getClass().getDeclaredField("profile");

            Field.setAccessible(true);

            Field.set(Meta, Profile);
        }catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        };

        Item.setItemMeta(Meta);

        net.minecraft.server.v1_8_R3.ItemStack NMS = ProtoStackMagic.getNMS(Item);

        NBTTagCompound Tags = ProtoStackMagic.getTags(NMS);

        Tags.setString("ID", TID);

        NMS.setTag(Tags);

        return CraftItemStack.asBukkitCopy(NMS);
    };

    public static ItemStack Create(String Texture, String Displayname, String TID, List<String> Lore) {

        Texture = "http://textures.minecraft.net/texture/" + Texture;

        ItemStack Item = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);

        SkullMeta Meta = (SkullMeta) Item.getItemMeta();

        Meta.setDisplayName(Displayname);

        Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        Meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        List<String> NLore = new ArrayList<>();

        Lore.forEach(Line -> {
            NLore.add(Line);
        });

        Meta.setLore(NLore);

        StringBuilder SUUID = new StringBuilder("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");

        for(int i = 0; i < SUUID.length(); i++) {
            if(SUUID.charAt(i) != '-') {
                SUUID.setCharAt(i, Texture.charAt(i + 38));
            };
        };

        GameProfile Profile = new GameProfile(UUID.fromString(SUUID.toString()), null);

        byte[] Bytes = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", Texture).getBytes());

        Profile.getProperties().put("textures", new Property("textures", new String(Bytes)));

        try {
            Field Field = Meta.getClass().getDeclaredField("profile");

            Field.setAccessible(true);

            Field.set(Meta, Profile);
        }catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        };

        Item.setItemMeta(Meta);

        net.minecraft.server.v1_8_R3.ItemStack NMS = ProtoStackMagic.getNMS(Item);

        NBTTagCompound Tags = ProtoStackMagic.getTags(NMS);

        Tags.setString("ID", TID);

        NMS.setTag(Tags);

        return CraftItemStack.asBukkitCopy(NMS);
    };
};