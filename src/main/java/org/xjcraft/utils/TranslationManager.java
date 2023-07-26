package org.xjcraft.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TranslationManager {
    private final Plugin plugin;
    private final File modFolder;
    private final Gson gson;

    // 构造函数，初始化TranslationManager对象
    public TranslationManager(Plugin plugin, File modFolder) {
        this.plugin = plugin;
        this.modFolder = modFolder;
        // 创建Gson对象，并采用自定义的跳过重复项的处理器
        this.gson = new GsonBuilder()
        .registerTypeAdapter(new TypeToken<Map<String, String>>(){}.getType(), new DuplicateKeyDeserializer())
        .create();
    }

    // 合并翻译内容的方法，接受一个原始翻译的Map作为参数
    public Map<String, String> getMergedTranslations(Map<String, String> originalTranslations) {
        // 使用原始翻译内容创建一个新的Map用于存储合并后的翻译结果
        Map<String, String> mergedTranslations = originalTranslations;

        // 遍历mod文件夹中的所有文件
        for (File modFile : modFolder.listFiles()) {
            try {
                // 打开mod文件（JAR文件）作为ZipFile
                ZipFile modJar = new ZipFile(modFile);
                Enumeration<? extends ZipEntry> entries = modJar.entries();

                // 遍历JAR文件中的所有条目
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();

                    // 检查条目是否为"lang/zh_cn.json"文件
                    if (entry.getName().endsWith("/lang/zh_cn.json")) {
                        // 读取JSON文件内容并转换为Map<String, String>对象
                        InputStream stream = modJar.getInputStream(entry);
                        Reader reader = new InputStreamReader(stream,StandardCharsets.UTF_8);

                        Type type = new TypeToken<Map<String, String>>(){}.getType();

                        Map<String, String> modTranslations = gson.fromJson(reader, type);

                        // 将mod的翻译内容合并到主翻译Map中
                        mergedTranslations.putAll(modTranslations);
                    }
                }

                // 关闭JAR文件
                modJar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 返回合并后的翻译结果
        return mergedTranslations;
    }

    // 加载原始翻译内容的方法
    public Map<String, String> loadOriginalTranslations() {
        // 从插件资源中读取"zh_cn.json"文件并转换为Map<String, String>对象
        InputStream resource = plugin.getResource("zh_cn.json");
        return gson.fromJson(new InputStreamReader(resource), Map.class);
    }
}

