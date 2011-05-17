package net.preoccupied.bukkit;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;


public class CommandUtils {


    public static SimpleCommandMap getCommandMap(Server server) {
	try {
	    Class cls = Class.forName("org.bukkit.craftbukkit.CraftServer");
	    Field fld = cls.getDeclaredField("commandMap");
	    
	    return (SimpleCommandMap) fld.get(server);

	} catch(Exception e) {
	    return null;
	}
    }


    public static void registerCommand(Server server, String fallback, Command cmd) {
	SimpleCommandMap scm = getCommandMap(server);
	scm.register(cmd.getName(), fallback, cmd);
    }


    public static void unregisterCommand(Server server, String fallback, Command cmd) {
	SimpleCommandMap scm = getCommandMap(server);
	unregisterCommand(scm, fallback, cmd);
    }


    private static boolean testAndRemove(Map m, String key, Object o) {
	Object found = m.get(key);

	if(o == found) {
	    m.remove(key);
	    return true;

	} else {
	    return false;
	}
    }


    public static void unregisterCommand(SimpleCommandMap scm, String fallback, Command cmd) {
	try {
	    Field fld = SimpleCommandMap.class.getDeclaredField("knownCommands");
	    Map m = (Map) fld.get(scm);

	    if (! testAndRemove(m, cmd.getName(), cmd)) {
		testAndRemove(m, fallback+":"+cmd.getName(), cmd);
	    }
	
	    for (String n : cmd.getAliases()) {
		if( ! testAndRemove(m, n, cmd)) {
		    testAndRemove(m, fallback+":"+n, cmd);
		}
	    }

	} catch(Exception e) {
	    ;
	}
    }



    /**
       An extremely rudimentary processor for argument strings to
       convert quoted text into a single argument. Has the side effect
       of normalizing whitespace within the quotes.
     */
    public static List<String> processQuotes(List<String> args) {
	List<String> out = new ArrayList<String>(args.size());
	StringBuilder concat = null;

	for(String tmp : args) {
	    int tl = tmp.length();
	    if(tl == 0) continue;
		
	    if(concat != null) {
		if(tmp.charAt(tl - 1) == '"') {
		    concat.append(" ");
		    concat.append(tmp.substring(0, tl - 1));
		    out.add(concat.toString());
		    concat = null;
		} else {
		    concat.append(" ");
		    concat.append(tmp);
		}

	    } else {
		if(tmp.charAt(0) == '"') {
		    concat = new StringBuilder(tmp.substring(1));
		} else {
		    out.add(tmp);
		}
	    }
	}

	if(concat != null) {
	    out.add(concat.toString());
	    concat = null;
	}

	return out;
    }



    public static String[] processQuotes(String[] args) {
	return (String[]) processQuotes(Arrays.asList(args)).toArray(new String[0]);
    }


}


/* The end. */
