package net.preoccupied.bukkit.permissions;


import java.util.Map;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


/**
   An object acting as a reusable permissions check.

   Currently acts the same as calling Permissions.has, but will one
   day hopefully optimize a lot of the String unpacking, etc away.
 */
public class PermissionCheck {

    private final String node;
    private static PermissionHandler handler = null;
    private static Map<String,PermissionCheck> cache = new HashMap<String,PermissionCheck>();


    public PermissionCheck(String node) {
	this.node = node;
    }


    private static PermissionHandler getPermissionHandler() {
	if(handler == null) {
	    synchronized(PermissionCheck.class) {
		if(handler == null) {
		    PluginManager pm = Bukkit.getServer().getPluginManager();
		    Plugin plug = pm.getPlugin("Permissions");
		    handler = ((Permissions) plug).getHandler();
		}
	    }
	}
	return handler;
    }


    public boolean check(Player player) {
	return getPermissionHandler().has(player, this.node);
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
