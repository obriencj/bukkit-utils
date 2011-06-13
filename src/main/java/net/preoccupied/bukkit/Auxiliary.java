package net.preoccupied.bukkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;


public class Auxiliary {


    private static Map<Class,Auxiliary> classes;


    static {
	classes = new HashMap<Class,Auxiliary>();
    }
    
 
   
    public static Auxiliary forClass(Class c) {
	Auxiliary aux = classes.get(c);
	if(aux == null) {
	    aux = new Auxiliary();
	    classes.put(c, aux);
	}
	return aux;
    }



    public static Map<String,Object> forOwner(Class byclass, Object owner) {
	return forClass(byclass).getData(owner);
    }



    public static Map<String,Object> forOwner(Object owner) {
	return forOwner(owner.getClass(), owner);
    }



    public static Object get(Class byclass, Object owner, String key) {
	Auxiliary aux = forClass(byclass);
	Map<String,Object> conf = aux.confmap.get(owner);
	return (conf == null)? null: conf.get(key);
    }



    public static Object get(Object owner, String key) {
	return get(owner.getClass(), owner, key);
    }



    public static String getString(Class byclass, Object owner, String key) {
	Object ret = get(byclass, owner, key);
	return (ret == null)? null: ret.toString();
    }



    public static String getString(Object owner, String key) {
	return getString(owner.getClass(), owner, key);
    }



    public static int getInt(Class byclass, Object owner, String key, int default_val) {
	Integer ret = (Integer) get(byclass, owner, key);
	return (ret == null)? default_val: ret;
    }



    public static int getInt(Object owner, String key, int default_val) {
	return getInt(owner.getClass(), owner, key, default_val);
    }



    public static boolean getBoolean(Class byclass, Object owner, String key) {
	Boolean ret = (Boolean) get(byclass, owner, key);
	return (ret == null)? false: ret;
    }



    public static boolean getBoolean(Object owner, String key) {
	return getBoolean(owner.getClass(), owner, key);
    }



    public static void set(Class byclass, Object owner, String key, Object val) {
	forOwner(byclass,owner).put(key, val);
    }



    public static void set(Object owner, String key, Object val) {
	forOwner(owner).put(key, val);
    }



    private Map<Object, Map<String,Object>> confmap;



    public Auxiliary() {
	confmap = new WeakHashMap<Object, Map<String,Object>>();
    }



    public Iterator<Map.Entry<Object,Map<String,Object>>> iterator() {
	return confmap.entrySet().iterator();
    }



    public Map<String,Object> getData(Object owner) {
	Map<String,Object> conf = confmap.get(owner);
	if(conf == null) {
	    conf = new HashMap<String,Object>();
	    confmap.put(owner, conf);
	}
	return conf;
    }
    

}


/* The end. */
