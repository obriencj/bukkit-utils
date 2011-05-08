package net.preoccupied.bukkit;


import java.util.Comparator;
import java.util.TreeMap;
import org.bukkit.Location;


public class LocationMap<V> extends TreeMap<Location,V> {


    public LocationMap() {
	super(locationComparator);
    }


    public static final Comparator<Location> locationComparator = new Comparator<Location>() {
	public int compare(Location a, Location b) {
	    if (a == b) {
		return 0;
	    } else if (a == null) {
		return -1;
	    } else if (b == null) {
		return 1;
	    }

	    int tmp = a.getWorld().hashCode() - b.getWorld().hashCode();
	    if (tmp != 0) return tmp;
	    
	    tmp = a.getBlockX() - b.getBlockX();
	    if (tmp != 0) return tmp;
	    
	    tmp = a.getBlockY() - b.getBlockY();
	    if (tmp != 0) return tmp;
	    
	    tmp = a.getBlockZ() - b.getBlockZ();
	    return tmp;
	}
	
	public boolean equals(Location a, Location b) {
	    return ((a == b) ||
		    ((a != null && b != null) &&
		     (a.getWorld() == b.getWorld()) &&
		     (a.getBlockX() == b.getBlockX()) &&
		     (a.getBlockY() == b.getBlockY()) &&
		     (a.getBlockZ() == b.getBlockZ())));
	}
    };


}


// The end.
