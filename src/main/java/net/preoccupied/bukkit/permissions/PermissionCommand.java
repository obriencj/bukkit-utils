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



public abstract class PermissionCommand implements CommandExecutor {
    
    private Map<Integer,PermissionCommandForm> forms;

    private PluginCommand command = null;
    
    private String permission = null;


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


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if(! (sender instanceof Player))
	    return false;

    	PermissionCommandForm form = findCommandForm(args.length);
	if(form == null) {
	    return false;

	} else {
	    return form.invoke(this, (Player) sender, args);
	}
    }


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


    public boolean run(Player p) { return false; }
    public boolean run(Player p, String a) { return false; }
    public boolean run(Player p, String a, String b) { return false; }
    public boolean run(Player p, String a, String b, String c) { return false; }
    public boolean run(Player p, String a, String b, String c, String d) { return false; }
    public boolean run(Player p, String a, String b, String c, String d, String e) { return false; }
    public boolean run(Player p, String a, String b, String c, String d, String e, String f) { return false; }


}


/* The end. */
