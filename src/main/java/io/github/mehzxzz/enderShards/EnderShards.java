package io.github.mehzxzz.enderShards;

import org.bukkit.plugin.java.JavaPlugin;

public class EnderShards extends JavaPlugin {

    private Recipes recipes;
    private Abilities abilities;

    @Override
    public void onEnable() {
        this.recipes = new Recipes(this);
        this.recipes.registerAll();

        this.abilities = new Abilities(this);
        getServer().getPluginManager().registerEvents(this.abilities, this);

        getLogger().info("EnderShards has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EnderShards has been successfully disabled.");
    }
}