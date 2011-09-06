package net.preoccupied.bukkit.permissions;


import java.util.Map;
import java.util.HashMap;

import org.bukkit.entity.Player;


/**
   An object acting as a reusable permissions check.

   Currently acts the same as calling Player.hasPermission, but will
   one day hopefully optimize a lot of the String unpacking, etc away.
 */
@Deprecated
public class PermissionCheck {

    private final String node;
    private static Map<String,PermissionCheck> cache = new HashMap<String,PermissionCheck>();


    public PermissionCheck(String node) {
	this.node = node;
    }


    public boolean check(Player player) {
	return player.hasPermission(node);
    }


    public static PermissionCheck forNode(String node) {
	PermissionCheck p;
	synchronized(cache) {
	    p = cache.get(node);
	    if(p == null) {
		p = new PermissionCheck(node);
		cache.put(node, p);
	    }
	}
	return p;
    }

}


/* The end. */
