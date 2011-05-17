package net.preoccupied.bukkit.permissions;


import java.lang.reflect.Method;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import net.preoccupied.bukkit.CommandUtils;
import net.preoccupied.bukkit.permissions.PermissionCheck;



public abstract class PermissionCommand implements CommandExecutor {
    
    private Map<Integer,PermissionCommandForm> forms;

    private PluginCommand command = null;
    
    private String permission = null;
    private PermissionCheck permcheck = null;


    public PermissionCommand(PluginCommand com) {
	this.forms = new TreeMap<Integer,PermissionCommandForm>();

	PluginDescriptionFile conf = com.getPlugin().getDescription();

	Map m;
	m = (Map) conf.getCommands();
	m = (Map) m.get(com.getName());

	this.permission = (String) m.get("permission");

	this.command = com;
	command.setExecutor(this);
    }


    public PermissionCommand(JavaPlugin plugin, String name) {
	this(plugin.getCommand(name));
    }


    public void msg(Player p, String m) {
	p.sendMessage(m);
    }



    public boolean permissionCheck(Player player) {
	if(permission == null) return true;

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
	    return true;
	} else {
	    return runUnquoted(player, args);
	}
    }


    /** Override this if you want permission checking but don't want
	command quote parsing to happen */
    public boolean runUnquoted(Player player, String[] args) {
	return run(player, CommandUtils.processQuotes(args));
    }
    
    
    /** Override this if you want permission checking and quote
	parsing, but want to be able to deal with a variable number of
	arguments */
    public boolean run(Player player, String[] args) {
    	PermissionCommandForm form = findCommandForm(args.length);
	return (form != null && form.invoke(this, player, args));
    }


    /* originally wrote this intending to find methods that existed
       only on the anonymous inner class, but then I discovered that I
       couldn't call those via reflection. I could probably tear all
       of the reflection code out and just switch which method to call
       by the argument count. Really disappointed with that, since we
       are now limited in how many arguments we can take by how many
       private run methods I put in this class. */
    

    private PermissionCommandForm findCommandForm(int count) {
	PermissionCommandForm pcf = forms.get(count);
	if(pcf == null) {
	    Method m = findRunMethod(count);
	    if(m != null) {
		pcf = new PermissionCommandForm(m, permission);
		forms.put(count, pcf);
	    }
	}
	return pcf;
    }


    private Method findRunMethod(int count) {
	Class<?>[] sig = new Class<?>[count + 1];
	sig[0] = Player.class;
	while(count > 0) sig[count--] = String.class;

	Method m = null;
	try {
	    m = PermissionCommand.class.getMethod("run", sig);

	} catch(NoSuchMethodException e) {
	    System.out.println(e);
	    m = null;
	}

	return m;
    }


    /* these get overridden with public versions when anonymous
       subclasses of PermissionCommand are created */

    public boolean run(Player p) { return false; }
    public boolean run(Player p, String a) { return false; }
    public boolean run(Player p, String a, String b) { return false; }
    public boolean run(Player p, String a, String b, String c) { return false; }
    public boolean run(Player p, String a, String b, String c, String d) { return false; }
    public boolean run(Player p, String a, String b, String c, String d, String e) { return false; }
    public boolean run(Player p, String a, String b, String c, String d, String e, String f) { return false; }


}


/* The end. */
