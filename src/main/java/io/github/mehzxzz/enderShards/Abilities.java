package io.github.mehzxzz.enderShards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Abilities implements Listener {

    private final Plugin plugin;
    private final Map<UUID, Long> strengthCooldown = new HashMap<>();
    private final long STRENGTH_COOLDOWN_MS = 90_000L;
    private final int STRENGTH_DURATION_TICKS = 20 * 15;
    private BossBar strengthBar;

    public Abilities(Plugin plugin) {
        this.plugin = plugin;
        strengthBar = Bukkit.createBossBar("§cStrength Ability Cooldown", BarColor.RED, BarStyle.SOLID);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    handleArmorEffects(p);
                    updateStrengthBar(p);
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    private boolean isEndShardGear(ItemStack item, Material expectedMaterial) {
        if (item == null) return false;
        if (item.getType() != expectedMaterial) return false;
        ItemMeta m = item.getItemMeta();
        if (m == null) return false;
        Integer cmd = m.getCustomModelData();
        return cmd != null && cmd == 100;
    }

    private void handleArmorEffects(Player p) {
        ItemStack helmet = p.getInventory().getHelmet();
        ItemStack chest = p.getInventory().getChestplate();
        ItemStack legs = p.getInventory().getLeggings();
        ItemStack boots = p.getInventory().getBoots();

        boolean hasHelmet = isEndShardGear(helmet, Material.NETHERITE_HELMET);
        boolean hasChest = isEndShardGear(chest, Material.NETHERITE_CHESTPLATE);
        boolean hasLegs = isEndShardGear(legs, Material.NETHERITE_LEGGINGS);
        boolean hasBoots = isEndShardGear(boots, Material.NETHERITE_BOOTS);

        if (hasHelmet) {
            applyEffect(p, PotionEffectType.RESISTANCE, 40, 1);
        }
        if (hasChest) {
            applyEffect(p, PotionEffectType.FIRE_RESISTANCE, 40, 0);
        }
        if (hasLegs) {
            applyEffect(p, PotionEffectType.STRENGTH, 40, 1);
        }
        if (hasBoots) {
            applyEffect(p, PotionEffectType.SPEED, 40, 1);
        }
        if (hasHelmet && hasChest && hasLegs && hasBoots) {
            for (Entity e : p.getNearbyEntities(20, 20, 20)) {
                if (e instanceof Player) {
                    Player other = (Player) e;
                    if (!other.equals(p)) {
                        applyEffect(other, PotionEffectType.GLOWING, 40, 0);
                    }
                }
            }
        }
    }

    private void applyEffect(Player p, PotionEffectType type, int durationTicks, int amplifier) {
        PotionEffect current = p.getPotionEffect(type);
        if (current == null || current.getAmplifier() != amplifier || current.getDuration() < 20) {
            p.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, false, false));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack inHand = p.getInventory().getItemInMainHand();
            if (isEndShardGear(inHand, Material.NETHERITE_SWORD)) {
                long now = System.currentTimeMillis();
                Long last = strengthCooldown.get(p.getUniqueId());
                if (last != null && (now - last) < STRENGTH_COOLDOWN_MS) {
                    long remain = (STRENGTH_COOLDOWN_MS - (now - last)) / 1000;
                    p.sendMessage("§cStrength ability is on cooldown: " + remain + "s");
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, STRENGTH_DURATION_TICKS, 2, true, false, false));
                strengthCooldown.put(p.getUniqueId(), now);
                p.sendMessage("§6Strength III applied for 15 seconds!");
                p.setCooldown(Material.NETHERITE_SWORD, (int) (STRENGTH_COOLDOWN_MS / 50));
            }
        }
    }

    private void updateStrengthBar(Player p) {
        Long last = strengthCooldown.get(p.getUniqueId());
        if (last == null) {
            strengthBar.removePlayer(p);
            return;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - last;
        if (elapsed >= STRENGTH_COOLDOWN_MS) {
            strengthBar.removePlayer(p);
        } else {
            strengthBar.addPlayer(p);
            double progress = 1.0 - ((double) elapsed / STRENGTH_COOLDOWN_MS);
            strengthBar.setProgress(progress);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        strengthCooldown.remove(e.getPlayer().getUniqueId());
        strengthBar.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        strengthBar.removePlayer(p);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        Bukkit.getScheduler().runTaskLater(plugin, () -> handleArmorEffects(p), 1L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        handleArmorEffects(e.getPlayer());
    }
}