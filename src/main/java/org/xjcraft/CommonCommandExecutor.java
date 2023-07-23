package org.xjcraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xjcraft.annotation.RCommand;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class CommonCommandExecutor implements CommandExecutor {
    private final Map<String, Method> commandMap = new HashMap<>();

    public CommonCommandExecutor(){
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(RCommand.class)) {
                RCommand cmd = method.getAnnotation(RCommand.class);
                commandMap.put(cmd.value().toLowerCase(), method);
            }
        }
    }

    public Map<String, Method> getCommands() {
        return commandMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Method method = commandMap.get(command.getName().toLowerCase());
        Player player = (Player) sender;
        if (method != null) {
            try {
                switch  (label){
                    case "test":
                        method.invoke(this,player);
                        break;
                    case "label":
                        method.invoke(this,player);
                        break;
                    case "on":
                        method.invoke(this,player);
                        break;
                    case "mine":
                        method.invoke(this,player);
                        break;
                    case "check":
                        method.invoke(this,sender);
                        break;
                    case "get":
                        Material material = Material.matchMaterial(args[0]);
                        if (material != null) {
                            method.invoke(this,sender,args[0]);
                        }else{
                            sender.sendMessage("无效的物品类型：" + args[0]);
                        }
                        break;  
                    case "save":
                        method.invoke(this,sender,args[0]);
                        break;

                }
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "执行命令 " + command.getName() + " 时发生错误", e);
                sender.sendMessage("命令执行错误，请联系管理员查看详细日志");
                return false;
            }
            return true;
        }
        sender.sendMessage("未找到命令：" + command.getName());
        return false;
    }
}

