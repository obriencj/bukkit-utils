package net.preoccupied.bukkit;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public abstract class PlayerCommand implements CommandExecutor {

    private PluginCommand command = null;



    public PlayerCommand(PluginCommand com) {
	this.command = com;
	command.setExecutor(this);
    }



    public PlayerCommand(JavaPlugin plugin, String name) {
	this(plugin.getCommand(name));
    }



    public int parseInt(String m, int default_val) {
	return parseInt(null, m, default_val);
    }



    public int parseInt(Player p, String m, int default_val) {
	int i = default_val;
	try {
	    i = Integer.parseInt(m);
	} catch(Exception e) {
	    if(p != null) {
		msg(p, "Invalid number format (defaulting to " + default_val + "): " + m);
	    }
	}
	return i;
    }



    public void msg(Player p, String m) {
	if(p != null) p.sendMessage(m);
    }



    public void err(Player p, String m) {
	if(p != null) p.sendMessage("$4" + m + "$f");
    }



    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if(! (sender instanceof Player))
	    return false;

	Player player = (Player) sender;
	return runUnquoted(player, args);
    }



    /** Override this if you don't want command quote parsing to
	happen */
    public boolean runUnquoted(Player player, String[] args) {
	return run(player, CommandUtils.processQuotes(args));
    }

    
    
    /** Override this if you want and quote parsing, but want to be
	able to deal with a variable number of arguments. If not
	overridden, it will dispatch to the run with the correct
	number of string arguments. */
    public boolean run(Player player, String[] args) {
	switch(args.length) {
	case 0:
	    return run(player);
	case 1:
	    return run(player, args[0]);
	case 2:
	    return run(player, args[0], args[1]);
	case 3:
	    return run(player, args[0], args[1], args[2]);
	case 4:
	    return run(player, args[0], args[1], args[2], args[3]);
	case 5:
	    return run(player, args[0], args[1], args[2], args[3], args[4]);
	case 6:
	    return run(player, args[0], args[1], args[2], args[3], args[4],
		       args[5]);
	case 7:
	    return run(player, args[0], args[1], args[2], args[3], args[4],
		       args[5], args[6]);
	case 8:
	    return run(player, args[0], args[1], args[2], args[3], args[4],
		       args[5], args[6], args[7]);
	default:
	    return false;
	}
    }



    /* these get overridden with public versions when anonymous
       subclasses of PlayerCommand are created */

    public boolean run(Player p) { return false; }
    public boolean run(Player p, String a) { return false; }
    public boolean run(Player p, String a, String b) { return false; }
    public boolean run(Player p, String a, String b, String c) { return false; }
    public boolean run(Player p, String a, String b, String c, String d) { return false; }
    public boolean run(Player p, String a, String b, String c, String d,
		       String e) { return false; }
    public boolean run(Player p, String a, String b, String c, String d,
		       String e, String f) { return false; }
    public boolean run(Player p, String a, String b, String c, String d,
		       String e, String f, String g) { return false; }
    public boolean run(Player p, String a, String b, String c, String d,
		       String e, String f, String g, String h) { return false; }


}


/* The end. */
