package net.preoccupied.bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class TeleportQueue {
    
    
    private JavaPlugin plugin;
    private Map<Chunk,List<Runnable>> teleportQueue = null;



    public TeleportQueue(JavaPlugin plugin) {
	this.plugin = plugin;
	this.teleportQueue = new HashMap<Chunk,List<Runnable>>();
    }



    public void enable() {
	PluginManager pm = plugin.getServer().getPluginManager();

	EventExecutor ee = new EventExecutor() {
		public void execute(Listener ignored, Event e) {
		    onChunkLoad((ChunkLoadEvent) e);
		}
	    };

	pm.registerEvent(Event.Type.CHUNK_LOAD, null, ee, Priority.Low, plugin);
    }



    public void disable() {
	teleportQueue.clear();
    }



    private void onChunkLoad(ChunkLoadEvent cle) {
	Chunk chunk = cle.getChunk();
	List<Runnable> queue = teleportQueue.get(chunk);

	if(queue != null) {
	    for(Runnable task : queue) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task);
	    }
	    teleportQueue.remove(chunk);
	}
    }



    public void safeTeleport(final Player player, final Location destination) {

	Runnable task = new Runnable() {
		public void run() {
		    player.teleport(destination);
		}
	    };
	
	World world = destination.getWorld();
	Chunk chunk = world.getChunkAt(destination);

	if(world.isChunkLoaded(chunk)) {
	    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task);

	} else {
	    List<Runnable> queue = teleportQueue.get(chunk);
	    if(queue == null) {
		queue = new LinkedList<Runnable>();
		teleportQueue.put(chunk, queue);
	    }
	    queue.add(task);
	    world.loadChunk(chunk);
	}
    }

}


/* The end. */
