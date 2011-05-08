package net.preoccupied.bukkit;


import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import java.lang.reflect.Field;
import java.util.Map;


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

}


/* The end. */
