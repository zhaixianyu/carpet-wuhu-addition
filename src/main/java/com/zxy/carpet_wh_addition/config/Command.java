package com.zxy.carpet_wh_addition.config;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;

import static com.zxy.carpet_wh_addition.config.ServerConfig.configData;
import static net.minecraft.server.command.CommandManager.literal;

public class Command {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        List<LiteralArgumentBuilder<ServerCommandSource>> commands = List.of(
                //手长修改 已修更改carpet选项
//                literal("remote_interaction")
//                .requires(source -> source.hasPermissionLevel(2)) // 2 表示 OP 权限
//                        .then(CommandManager.argument("mode", StringArgumentType.string())
//                                .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"destructionRange", "putRange", "interactRange"}, builder))
//                                .then(CommandManager.argument("num", DoubleArgumentType.doubleArg())
//                                        .executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "mode"), DoubleArgumentType.getDouble(context, "num")))
//                                )
//                        )
        );
        for (LiteralArgumentBuilder<ServerCommandSource> command : commands) {
            dispatcher.register(command);
        }
    }

    private static int execute(ServerCommandSource source, String str, double num) {
        //#if MC > 11802
        source.sendMessage(Text.of(str + " changeTo: " + num));
        //#else
        //$$ try {
        //$$     source.getPlayer().sendMessage(Text.of(str+ " changeTo: " + num),false);
        //$$ } catch (com.mojang.brigadier.exceptions.CommandSyntaxException ignored) {
        //$$ }
        //#endif
        switch (str) {
            case ("destructionRange") -> configData.destructionRange = num * num;
            case ("putRange") -> configData.putRange = num * num;
            case ("interactRange") -> configData.interactRange = num * num;
        }
//        source.sendFeedback(new LiteralText("Received number: " + num), false);
        return 1;
    }

    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {

//        CommandRegistrationCallback.EVENT.register((dispatcher1, dedicated
//                //#if MC > 11802
//                , registrationEnvironment
//                //#else
//                //$$
//                //#endif
//        ) -> {
////            Command.register(dispatcher1);
//        });
//        Command.register(dispatcher);
    }
}
