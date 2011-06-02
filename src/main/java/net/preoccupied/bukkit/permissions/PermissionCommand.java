package net.preoccupied.bukkit.permissions;


import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import net.preoccupied.bukkit.CommandUtils;
import net.preoccupied.bukkit.PlayerCommand;
import net.preoccupied.bukkit.permissions.PermissionCheck;



public abstract class PermissionCommand extends PlayerCommand {

    
    private String permission = null;
    private PermissionCheck permcheck = null;



    public PermissionCommand(PluginCommand com) {
	super(com);

	PluginDescriptionFile conf = com.getPlugin().getDescription();

	Map m;
	m = (Map) conf.getCommands();
	m = (Map) m.get(com.getName());

	this.permission = (String) m.get("permission");
    }



    public boolean permissionCheck(Player player) {
	if(permission == null) {
	    return true;
	}

	if(permcheck == null) {
	    permcheck = PermissionCheck.forNode(permission);
	}

	return permcheck.check(player);
    }



    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if(! (sender instanceof Player))
	    return false;

	Player player = (Player) sender;

	if(! permissionCheck(player)) {
	    err(player, "You do not have the permission to do that.");
	    return true;

	} else {
	    return runUnquoted(player, args);
	}
    }


}


/* The end. */
