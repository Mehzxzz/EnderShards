package io.github.mehzxzz.enderShards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


import java.util.Arrays;
import java.util.List;

public class Recipes {

    private final Plugin plugin;

    public Recipes(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        ItemStack shard = new ItemStack(Material.DISC_FRAGMENT_5);
        ItemMeta sm = shard.getItemMeta();
        sm.setDisplayName("§5Ender Shard");
        sm.setCustomModelData(100);
        shard.setItemMeta(sm);

        ItemStack shard5 = shard.clone();
        shard5.setAmount(5);

        ShapelessRecipe enderShards = new ShapelessRecipe(new NamespacedKey(plugin, "end_shards"), shard5);
        enderShards.addIngredient(Material.DRAGON_EGG);
        Bukkit.addRecipe(enderShards);

        ItemStack helmet = makeResult(Material.NETHERITE_HELMET, "§5End Helmet");
        ItemStack chest = makeResult(Material.NETHERITE_CHESTPLATE, "§5End Chestplate");
        ItemStack legs = makeResult(Material.NETHERITE_LEGGINGS, "§5End Leggings");
        ItemStack boots = makeResult(Material.NETHERITE_BOOTS, "§5End Boots");
        ItemStack sword = makeResult(Material.NETHERITE_SWORD, "§5End Sword");

        ItemStack bricks = new ItemStack(Material.END_STONE_BRICKS);
        ItemStack wall = new ItemStack(Material.END_STONE_BRICK_WALL);
        ItemStack endstone = new ItemStack(Material.END_STONE);
        ItemStack endrod = new ItemStack(Material.END_ROD);

        RecipeChoice shardChoice = new RecipeChoice.ExactChoice(shard);

        ShapedRecipe endChestplate = new ShapedRecipe(new NamespacedKey(plugin, "end_chestplate"), chest);
        endChestplate.shape("W W", "BSB", "EBE");
        endChestplate.setIngredient('W', new RecipeChoice.ExactChoice(wall));
        endChestplate.setIngredient('B', new RecipeChoice.ExactChoice(bricks));
        endChestplate.setIngredient('E', new RecipeChoice.ExactChoice(endstone));
        endChestplate.setIngredient('S', shardChoice);
        Bukkit.addRecipe(endChestplate);

        ShapedRecipe endLeggings = new ShapedRecipe(new NamespacedKey(plugin, "end_leggings"), legs);
        endLeggings.shape("BSB", "E E", "W W");
        endLeggings.setIngredient('B', new RecipeChoice.ExactChoice(bricks));
        endLeggings.setIngredient('S', shardChoice);
        endLeggings.setIngredient('E', new RecipeChoice.ExactChoice(endstone));
        endLeggings.setIngredient('W', new RecipeChoice.ExactChoice(wall));
        Bukkit.addRecipe(endLeggings);

        ShapedRecipe endBoots = new ShapedRecipe(new NamespacedKey(plugin, "end_boots"), boots);
        endBoots.shape("   ", "W W", "WSW");
        endBoots.setIngredient('W', new RecipeChoice.ExactChoice(wall));
        endBoots.setIngredient('S', shardChoice);
        Bukkit.addRecipe(endBoots);

        ShapedRecipe endHelmet = new ShapedRecipe(new NamespacedKey(plugin, "end_helmet"), helmet);
        endHelmet.shape("BSB", "E E", "   ");
        endHelmet.setIngredient('B', new RecipeChoice.ExactChoice(bricks));
        endHelmet.setIngredient('S', shardChoice);
        endHelmet.setIngredient('E', new RecipeChoice.ExactChoice(endstone));
        Bukkit.addRecipe(endHelmet);

        ShapedRecipe endSword = new ShapedRecipe(new NamespacedKey(plugin, "end_sword"), sword);
        endSword.shape(" W ", " S ", " R ");
        endSword.setIngredient('W', new RecipeChoice.ExactChoice(wall));
        endSword.setIngredient('S', shardChoice);
        endSword.setIngredient('R', new RecipeChoice.ExactChoice(endrod));
        Bukkit.addRecipe(endSword);
    }

    private ItemStack makeResult(Material mat, String name) {
        ItemStack is = new ItemStack(mat);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setCustomModelData(100);

        if (mat == Material.NETHERITE_SWORD) {
            im.addEnchant(Enchantment.SHARPNESS, 5, true);
            im.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            im.addEnchant(Enchantment.UNBREAKING, 3, true);
            im.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
            im.addEnchant(Enchantment.LOOTING, 3, true);
            im.addEnchant(Enchantment.MENDING, 1, true);
            List<String> lore = Arrays.asList(
                    "§5Forged from the sharpest shards of the dragon egg",
                    "§6Ability: Grants a boost of strength 3 for 15 seconds"
            );
            im.setLore(lore);
        } else if (mat == Material.NETHERITE_HELMET) {
            im.addEnchant(Enchantment.PROTECTION, 4, true);
            im.addEnchant(Enchantment.RESPIRATION, 3, true);
            im.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
            im.addEnchant(Enchantment.UNBREAKING, 3, true);
            im.addEnchant(Enchantment.MENDING, 1, true);
            List<String> lore = Arrays.asList(
                    "§5Forged from the strongest shards of the dragon egg",
                    "§6Grants Resistance II"
            );
            im.setLore(lore);
        } else if (mat == Material.NETHERITE_CHESTPLATE) {
            im.addEnchant(Enchantment.PROTECTION, 4, true);
            im.addEnchant(Enchantment.THORNS, 3, true);
            im.addEnchant(Enchantment.UNBREAKING, 3, true);
            im.addEnchant(Enchantment.MENDING, 1, true);
            List<String> lore = Arrays.asList(
                    "§5Forged from the strongest shards of the dragon egg",
                    "§6Grants Fire Resistance"
            );
            im.setLore(lore);
        } else if (mat == Material.NETHERITE_LEGGINGS) {
            im.addEnchant(Enchantment.PROTECTION, 4, true);
            im.addEnchant(Enchantment.UNBREAKING, 3, true);
            im.addEnchant(Enchantment.SWIFT_SNEAK, 3, true);
            im.addEnchant(Enchantment.MENDING, 1, true);
            List<String> lore = Arrays.asList(
                    "§5Forged from the strongest shards of the dragon egg",
                    "§6Grants Strength II"
            );
            im.setLore(lore);
        } else if (mat == Material.NETHERITE_BOOTS) {
            im.addEnchant(Enchantment.PROTECTION, 4, true);
            im.addEnchant(Enchantment.UNBREAKING, 3, true);
            im.addEnchant(Enchantment.SOUL_SPEED, 3, true);
            im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
            im.addEnchant(Enchantment.MENDING, 1, true);
            List<String> lore = Arrays.asList(
                    "§5Forged from the strongest shards of the dragon egg",
                    "§6Grants Speed II"
            );
            im.setLore(lore);
        }

        is.setItemMeta(im);
        return is;
    }
}