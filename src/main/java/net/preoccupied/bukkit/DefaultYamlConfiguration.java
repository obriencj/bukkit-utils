package net.preoccupied.bukkit;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.jar.JarFile;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


/**
   Represents a YAML configuration file which has sensible defaults
   defined inside of a plugin JAR. If an overriding copy of the
   configuration does not exist, it will be created as a copy from the
   default stored in the JAR.

 */
public class DefaultYamlConfiguration extends YamlConfiguration {


    private Plugin plugin;
    private File jarfile;
    private String configyml;
    private String defaultyml;
    

    public DefaultYamlConfiguration(Plugin plugin, File jarfile, String configyml, String defaultyml) {
	this.plugin = plugin;
	this.jarfile = jarfile;
	this.configyml = configyml;
	this.defaultyml = defaultyml;
    }
    

    public DefaultYamlConfiguration(Plugin plugin, File jarfile, String configyml) {
	this(plugin, jarfile, configyml, configyml);
    }


    public void load() throws IOException {
	load(plugin, jarfile, configyml, defaultyml);
    }


    private static void copyover(InputStream source, OutputStream sink) throws IOException {
	byte[] buffer = new byte[2^16];
	int count = source.read(buffer);
	while(count > 0) {
	    sink.write(buffer, 0, count);
	    count = source.read(buffer);
	}
    }


    private static void copyover(InputStream source, File sinkfile) throws IOException {
	FileOutputStream sink = new FileOutputStream(sinkfile);
	copyover(source, sink);
	sink.close();
    }



    public static Configuration load(Plugin plugin, File jarfile,
				     String configyml) throws IOException {

	return load(plugin, jarfile, configyml, configyml);
    }



    public static Configuration load(Plugin plugin, File jarfile,
				     String configyml, String defaultyml) throws IOException {

	/* - check if configyml exists in the plugins data folder.
	   - if not, create it as a copy of the defaultyml entry from jarfile.
	   - load and return the configyml */
	
	File dir = plugin.getDataFolder();
	if(! dir.exists()) {
	    dir.mkdirs();
	}

	File configfile = new File(dir, configyml);
	if(! configfile.exists()) {
	    JarFile jar = new JarFile(jarfile);
	    copyover(jar.getInputStream(jar.getEntry(defaultyml)), configfile);
	}

	YamlConfiguration conf = new YamlConfiguration();

	try {
	    conf.load(configfile);
	} catch(InvalidConfigurationException ice) {
	    ;
	}

	return conf;
    }

}


/* The end. */
