package com.steamsworld.plugins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;

import java.util.function.Function;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:43 PM)
 * polls (com.steamsworld.plugins)
 */
public class Constants {

    public static Function<String, String> COLORS = (text) -> ChatColor.translateAlternateColorCodes('&', text);
    public static Function<String, String> ANSWERS = (answer) -> ChatColor.stripColor(COLORS.apply(answer));
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
