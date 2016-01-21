package com.sunyard.util;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

//修复bukkit使用系统字符编码读取yaml配置
public class YamlFix extends YamlConfiguration {

    private String charset = "UTF-8";

    public static YamlConfiguration loadConfigurationFix(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null");

        YamlConfiguration config = new YamlFix();

        try {
            config.load(stream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }

/*	@Override
    public Object get(String path, Object def) {
		Object obj = super.get(path,null);
		if(obj == null ){
			set(path, def);
		}
		return def;
	}*/

    public static YamlConfiguration loadConfigurationFix(File file) {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlFix();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

    @Override
    public void load(InputStream stream) throws IOException,
            InvalidConfigurationException {
        InputStreamReader reader = new InputStreamReader(stream, charset);
        StringBuilder builder = new StringBuilder();
        BufferedReader input = new BufferedReader(reader);


        try {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        loadFromString(builder.toString());
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");

        Files.createParentDirs(file);

        String data = saveToString();

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset);

        try {
            writer.write(data);
            writer.flush();
        } finally {
            writer.close();
        }
    }
}