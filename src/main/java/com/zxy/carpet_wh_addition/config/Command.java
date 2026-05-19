package com.zxy.carpet_wh_addition.config;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.zxy.carpet_wh_addition.featuresList.RuleSearchCommand;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

import static com.zxy.carpet_wh_addition.config.ServerConfig.configData;

public class Command {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        RuleSearchCommand.register(dispatcher);

        List<LiteralArgumentBuilder<CommandSourceStack>> commands = List.of(
                //手长修改 已被carpet接管
//                literal("remote_interaction")
//                .requires(source -> source.hasPermissionLevel(2)) // 2 表示 OP 权限
//                        .then(CommandManager.argument("mode", StringArgumentType.string())
//                                .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"destructionRange", "putRange", "interactRange"}, builder))
//                                .then(CommandManager.argument("num", DoubleArgumentType.doubleArg())
//                                        .executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "mode"), DoubleArgumentType.getDouble(context, "num")))
//                                )
//                        )
        );
        for (LiteralArgumentBuilder<CommandSourceStack> command : commands) {
            dispatcher.register(command);
        }
    }

    private static int execute(CommandSourceStack source, String str, double num) {
        //#if MC > 11802
        source.sendSystemMessage(Component.nullToEmpty(str + " changeTo: " + num));
        //#else
        //$$ try {
        //$$     source.getPlayerOrException().displayClientMessage(Component.nullToEmpty(str+ " changeTo: " + num),false);
        //$$ } catch (Exception e) {
        //$$     throw new RuntimeException(e);
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

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {

//        CommandRegistrationCallback.EVENT.register((dispatcher1, dedicated
//                                                    //#if MC > 11802
//                , registrationEnvironment
//                                                    //#else
//                                                    //$$
//                                                    //#endif
//        ) -> {
//            Command.register(dispatcher1);
//        });
        Command.register(dispatcher);
    }
}
