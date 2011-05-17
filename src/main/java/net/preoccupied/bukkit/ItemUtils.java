package net.preoccupied.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;


public class ItemUtils {



    public static void spawnItem(Player player, int item_type, short item_dmg, int count) {
	spawnItem(player, new ItemStack(item_type, count, item_dmg));
    }



    public static void spawnItem(Player player, ItemStack item) {
	PlayerInventory inv = player.getInventory();

	// if the item is armor
	//  - we try to put it on the appropriate slot
	//  - return

	Material m = item.getType();
	if(isHelmet(m)) {
	    if(inv.getHelmet().getType() == Material.AIR) {
		inv.setHelmet(item);
		return;
	    }
	    
	} else if(isChestplate(m)) {
	    if(inv.getChestplate().getType() == Material.AIR) {
		inv.setChestplate(item);
		return;
	    }

	} else if(isLeggings(m)) {
	    if(inv.getLeggings().getType() == Material.AIR) {
		inv.setLeggings(item);
		return;
	    }

	} else if(isBoots(m)) {
	    if(inv.getBoots().getType() == Material.AIR) {
		inv.setBoots(item);
		return;
	    }
	}

	// if the player has a free slot
	//  - put the item in the player's inventory
	//  - return
	
	int slot = inv.firstEmpty();
	if(slot >= 0 && slot < 80) {
	    inv.setItem(slot, item);
	    return;
	}

	// spawn the item at the player's feet
	Location where = player.getLocation();
	where.getWorld().dropItemNaturally(where, item);
    }



    public static boolean isHelmet(int material) {
	return isHelmet(Material.getMaterial(material));
    }



    public static boolean isHelmet(Material m) {
	switch(m) {
	case LEATHER_HELMET:
	case IRON_HELMET:
	case GOLD_HELMET:
	case DIAMOND_HELMET:
	    return true;
	default:
	    return false;
	}
    }



    public static boolean isChestplate(int material) {
	return isChestplate(Material.getMaterial(material));
    }



    public static boolean isChestplate(Material m) {
	switch(m) {
	case LEATHER_CHESTPLATE:
	case IRON_CHESTPLATE:
	case GOLD_CHESTPLATE:
	case DIAMOND_CHESTPLATE:
	    return true;
	default:
	    return false;
	}
    }



    public static boolean isLeggings(int material) {
	return isLeggings(Material.getMaterial(material));
    }



    public static boolean isLeggings(Material m) {
	switch(m) {
	case LEATHER_LEGGINGS:
	case IRON_LEGGINGS:
	case GOLD_LEGGINGS:
	case DIAMOND_LEGGINGS:
	    return true;
	default:
	    return false;
	}
    }



    public static boolean isBoots(int material) {
	return isBoots(Material.getMaterial(material));
    }



    public static boolean isBoots(Material m) {
	switch(m) {
	case LEATHER_BOOTS:
	case IRON_BOOTS:
	case GOLD_BOOTS:
	case DIAMOND_BOOTS:
	    return true;
	default:
	    return false;
	}
    }


}


/* The end. */
