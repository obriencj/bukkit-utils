package net.preoccupied.bukkit.permissions;


import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;


public class PermissionCommandForm {

    private Method method;
    private PermissionCheck permission;


    public PermissionCommandForm(Method m, String permission_node) {
	this.method = m;

	// the passed permission_node is just the default. Let's check
	// if we have been overridden with an annotation.
	RequirePermission pa = m.getAnnotation(RequirePermission.class);
	if(pa != null) {
	    permission_node = pa.permission();
	}
	
	if(permission_node != null) {
	    this.permission = PermissionCheck.forNode(permission_node);
	}
    }


    public PermissionCommandForm(Method m) {
	this(m, null);
    }

    
    public boolean invoke(Object o, Player player, String[] args) {
	// check the permission if there is one.
	if(permission != null && ! permission.check(player)) {
	    //player.sendMessage("$4insufficient permissions$f");
	    return true;
	}

	// construct the arguments for the method
	Object[] all_args = new Object[args.length + 1];
	all_args[0] = player;
	for(int i = args.length; i-- > 0; )
	    all_args[i + 1] = args[i];
	
	try {
	    return (Boolean) method.invoke(o, all_args);
	} catch(IllegalAccessException iae) {
	    System.out.println(iae);
	    return true;
	} catch(InvocationTargetException ite) {
	    System.out.println(ite);
	    return true;
	}
    }

}


/* The end. */

