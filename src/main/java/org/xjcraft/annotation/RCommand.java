package org.xjcraft.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RCommand {
    // 命令的名称
    String value();
    
    // 命令的发送者
    Sender sender() default Sender.ANY;
    
    // 命令的描述
    String desc() default "";
    
    // 定义一个枚举用于表示命令的发送者的类型
    enum Sender {
        PLAYER, CONSOLE, ANY
    }
}
