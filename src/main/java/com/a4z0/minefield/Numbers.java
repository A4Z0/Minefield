package com.a4z0.minefield;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum Numbers {
    ONE("a39c846f65d5f272a839fd9c2aeb11bdc8e3f8229fbe3583486e78f4c23c8b5b"),
    TWO("5415c4d0c7b8141501949f73ce0c78b2b1e990255371a7fc7199960c9b037d51"),
    THREE("5f8d3c8cb0983a4f56cc26a71ffcedbd7becc521291c78361ff1e99df4144cbc"),
    FOUR("6127812166e14186decf17519603b355699499a545397f8931794fad6e9efd92"),
    FIVE("fe1008592e3ad24d65dfa4ff5a3c800d78a3db134cbd8e9ec3cbac1ea8391b9d"),
    SIX("93098f3a994c1cd68e0e862a00688866f2e673481cd3447c85d9801bc0317b5f"),
    SEVEN("8102a8eb0ffdfe5982073dbc41b75bc22e577e3b1ad00bb14868cee384bec7b"),
    EIGHT("83a6d9eca68628518e6b99054bc0a1f7fcc79e2c969f3eb8f9ef034165e03bb5");

    private final String Texture;

    Numbers(@NotNull String Texture) {
        this.Texture = Texture;
    }

    public @NotNull ItemStack getItem() {
        return v_1_8_PlayerHead.Create(Texture, "a", Texture);
    }
}
