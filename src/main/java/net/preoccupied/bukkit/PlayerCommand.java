package net.preoccupied.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.bukkit.ChatColor;
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
	    warning(plugin, "attempted to create a PlayerCommand for",
		    name, "which is not defined in plugin.yml");
	} else {
	    com.setExecutor(this);
	    this.command = com;
	}
    }



    /**  
	 Borrowed this quote processing from john@pointysoftware.net
	 https://github.com/Nephyrin/ForcegenChunks
    */
    private static final Pattern quotesPattern =
	Pattern.compile("\\s*(?:\\\"((?:[^\\\"\\\\]|\\\\.)*)\\\"|((?:[^\\s\\\\\\\"]|\\\\(?:.|$))+))(?:\\s|$)");



    public static List<String> processQuotes(String allArgs) {
	List<String> ret = new ArrayList<String>();

	Matcher m = quotesPattern.matcher(allArgs);
	while(m.regionStart() < m.regionEnd()) {
	    if(m.lookingAt()) {
		ret.add((m.group(1) == null ? m.group(2) : m.group(1)).replaceAll("\\\\(.|$)", "$1"));
		m.region(m.end(), m.regionEnd());

	    } else {
		break;
            }
	}

	return ret;
    }



    public static List<String> processQuotes(List<String> args) {
	if(args == null || args.size() == 0) {
	    return args;
	}
	return processQuotes(join(' ', args));
    }



    public static String[] processQuotes(String[] args) {
	return (String[]) processQuotes(Arrays.asList(args)).toArray(new String[0]);
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


    
    public static String join(char c, List<String> l) {
	if(l == null || l.size() == 0) {
	    return "";
	}

	StringBuilder concat = new StringBuilder();
	for(String a : l) {
	    concat.append(a);
	    concat.append(c);
	}
	concat.setLength(concat.length() - 1);

	return concat.toString();
    }



    public static String join(char c, Object... obs) {
	if(obs == null || obs.length == 0) {
	    return "";
	}

	StringBuilder concat = new StringBuilder();
	for(Object o : obs) {
	    concat.append(safestr(o));
	    concat.append(c);
	}
	concat.setLength(concat.length() - 1);

	return concat.toString();
    }



    public static void msg(Player p, String m) {
	if(p != null) p.sendMessage(ChatColor.AQUA + m + ChatColor.WHITE);
    }



    public static void msg(Player p, Object... obs) {
	if(p == null) return;
	StringBuilder sb = new StringBuilder();
	sb.append(ChatColor.AQUA);
	sb.append(join(' ', obs));
	sb.append(ChatColor.WHITE);
	p.sendMessage(sb.toString());
    }



    public static void err(Player p, String m) {
	if(p != null) p.sendMessage(ChatColor.RED + m + ChatColor.WHITE);
    }



    public static void err(Player p, Object... obs) {
	if(p == null) return;
	StringBuilder sb = new StringBuilder();
	sb.append(ChatColor.RED);
	sb.append(join(' ', obs));
	sb.append(ChatColor.WHITE);
	p.sendMessage(sb.toString());
    }



    public void info(String msg) {
	if(logger != null) logger.info(msg);
    }



    public void info(Object... obs) {
	if(logger == null) return;
	logger.info(join(' ', obs));
    }



    public void warning(String msg) {
	if(logger != null) logger.warning(msg);
    }



    public void warning(Object... obs) {
	if(logger == null) return;
	logger.warning(join(' ', obs));
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
	return run(player, processQuotes(args));
    }

    
    
    /** Override this if you want quote parsing, but want to be able
	to deal with a variable number of arguments. If not
	overridden, it will dispatch to the run with the correct
	number of string arguments. If you want to handle more than 10
	arguments, you will need to override this. */
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
	case 9:
	    return run(player, args[0], args[1], args[2], args[3], args[4],
		       args[5], args[6], args[7], args[8]);
	case 10:
	    return run(player, args[0], args[1], args[2], args[3], args[4],
		       args[5], args[6], args[7], args[8], args[9]);
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
    public boolean run(Player p, String a, String b, String c, String d,
		       String e, String f, String g, String h, String i) { return false; }
    public boolean run(Player p, String a, String b, String c, String d,
		       String e, String f, String g, String h, String i,
		       String j) { return false; }


}


/* The end. */
