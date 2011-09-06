package net.preoccupied.bukkit;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public abstract class PlayerCommand implements CommandExecutor {


    private PluginCommand command = null;
    private Logger logger = null;



    public PlayerCommand(PluginCommand com) {
	this.command = com;
	com.setExecutor(this);
	this.logger = com.getPlugin().getServer().getLogger();
    }



    public PlayerCommand(JavaPlugin plugin, String name) {
	this.logger = plugin.getServer().getLogger();

	PluginCommand com = plugin.getCommand(name);
	if(com == null) {
	    warning(plugin, "attempted to create a PlayerCommand for ",
		    name, "which is not defined in plugin.yml");
	} else {
	    com.setExecutor(this);
	    this.command = com;
	}
    }



    public static int parseInt(String m, int default_val) {
	return parseInt(null, m, default_val);
    }



    public static int parseInt(Player p, String m, int default_val) {
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


    
    public static String safestr(Object o) {
	return (o == null)? "[null]": o.toString();
    }



    public static void msg(Player p, String m) {
	if(p != null) p.sendMessage("$b" + m + "$f");
    }



    public static void msg(Player p, Object... obs) {
	if(p == null) return;
	StringBuilder sb = new StringBuilder("$b");
	for(Object o : obs) {
	    sb.append(safestr(o));
	    sb.append(" ");
	}
	sb.append("$f");
	p.sendMessage(sb.toString());
    }



    public static void err(Player p, String m) {
	if(p != null) p.sendMessage("$4" + m + "$f");
    }



    public static void err(Player p, Object... obs) {
	if(p == null) return;
	StringBuilder sb = new StringBuilder("$4");
	for(Object o : obs) {
	    sb.append(safestr(o));
	    sb.append(" ");
	}
	sb.append("$f");
	p.sendMessage(sb.toString());
    }



    public void info(String msg) {
	if(logger != null) logger.info(msg);
    }



    public void info(Object... obs) {
	if(logger == null) return;

	StringBuilder sb = new StringBuilder();
	for(Object o : obs) {
	    sb.append(safestr(o));
	    sb.append(" ");
	}

	logger.info(sb.toString());
    }



    public void warning(String msg) {
	if(logger != null) logger.warning(msg);
    }



    public void warning(Object... obs) {
	if(logger == null) return;

	StringBuilder sb = new StringBuilder();
	for(Object o : obs) {
	    sb.append(safestr(o));
	    sb.append(" ");
	}

	logger.warning(sb.toString());
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
