package net.preoccupied.bukkit;


import org.bukkit.Server;
import org.bukkit.command.Command;


public abstract class DynamicCommand extends Command {
   

    public DynamicCommand(String name) {
	super(name);
    }


    public void register(Server server, String fallback) {
	CommandUtils.registerCommand(server, fallback, this);
    }


    public void unregister(Server server, String fallback) {
	CommandUtils.unregisterCommand(server, fallback, this);
    }

}


/* The end. */
