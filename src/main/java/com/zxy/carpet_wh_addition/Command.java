package com.zxy.carpet_wh_addition;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.zxy.carpet_wh_addition.config.ServerConfig.configData;

public class Command {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("remote_interaction")
                .requires(source -> source.hasPermissionLevel(2)) // 2 表示 OP 权限
                .then(CommandManager.argument("mode", StringArgumentType.string())
                        .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"destructionRange", "putRange", "interactRange"}, builder))
                        .then(CommandManager.argument("num", DoubleArgumentType.doubleArg())
                                .executes(context -> execute(context.getSource(),StringArgumentType.getString(context,"mode") , DoubleArgumentType.getDouble(context, "num")))
                        )
                )
        );
    }
    private static int execute(ServerCommandSource source,String str, double num) {
        source.sendMessage(Text.of(str+ " changeTo: " + num));
        switch (str) {
            case ("destructionRange") -> configData.destructionRange = num*num;
            case ("putRange") -> configData.putRange = num*num;
            case ("interactRange") -> configData.interactRange = num*num;
        }
//        source.sendFeedback(new LiteralText("Received number: " + num), false);
        return 1;
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> {
            Command.register(dispatcher);
        });
    }
}
