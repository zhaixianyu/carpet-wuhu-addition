package com.zxy.carpet_wh_addition.featuresList;

import carpet.CarpetServer;
import carpet.api.settings.CarpetRule;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import com.zxy.carpet_wh_addition.config.Translate;
import com.zxy.carpet_wh_addition.mixin.setting.SettingsManagerAccessor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

//#if MC > 11802
import carpet.api.settings.RuleHelper;
import carpet.utils.CommandHelper;
import net.minecraft.network.chat.contents.TranslatableContents;
//#else
//$$
//#endif

public class RuleSearchCommand {
    //#if MC > 11802

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cCommandSearch")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetWuHuSettings.commandSearch))
                .then(Commands.argument("搜索等级", IntegerArgumentType.integer(1, 3))
                        .then(getContent().executes(context -> listRule(context,IntegerArgumentType.getInteger(context,"搜索等级")))))
                .then(getContent().executes(context -> listRule(context,1)))
        );
    }
    public static RequiredArgumentBuilder<CommandSourceStack, String> getContent(){
        return Commands.argument("rule", StringArgumentType.greedyString());
    }

    // 列出符合条件的规则
    private static int listRule(CommandContext<CommandSourceStack> context , int level) {
        String rule = StringArgumentType.getString(context, "rule");
        if (rule.matches("\".*\"")) {
            rule = rule.substring(1, rule.length() - 1);
        }
        if (CarpetServer.settingsManager == null) {
            return 0;
        }
        List<CarpetRule<?>> list = CarpetServer.settingsManager.getCarpetRules().stream().toList();
        String str = "carpet.commands.commandSearch.backMessage";
        MutableComponent text = Component.translatableWithFallback(str, Translate.getTranslate().get(str),rule);
        // 将文本设置为粗体
        text.withStyle(style -> style.withBold(true));
        context.getSource().sendSuccess(
                //#if MC > 11904
                () -> text
                //#else
                //$$ text
                //#endif
                , false);
        // 如果字符串为空，不搜索规则
        if (rule.isEmpty()) {
            return 0;
        }
        int ruleCount = 0;
        for (CarpetRule<?> carpet : list) {

            switch (level){
                case 1:
                    if (RuleHelper.translatedName(carpet).contains(rule)) {
                        Messenger.m(context.getSource(),
                                ((SettingsManagerAccessor) CarpetServer.settingsManager).displayInteractiveSettings(carpet));
                        ruleCount++;
                    }
                    break;
                case 2:
                    if (RuleHelper.translatedDescription(carpet).contains(rule)) {
                        Messenger.m(context.getSource(),
                                ((SettingsManagerAccessor) CarpetServer.settingsManager).displayInteractiveSettings(carpet));
                        ruleCount++;
                    }
                    break;
                case 3:
//                    if (RuleHelper.translatedName(carpet).contains(rule)) {
//                        Messenger.m(context.getSource(),
//                                ((SettingsManagerAccessor) CarpetServer.settingsManager).displayInteractiveSettings(carpet));
//                        ruleCount++;
//                    }
            }
//            if (RuleHelper.translatedName(carpet).contains(rule)) {
//                Messenger.m(context.getSource(),
//                        ((SettingsManagerAccessor) CarpetServer.settingsManager).displayInteractiveSettings(carpet));
//                ruleCount++;
//            }
        }
        return ruleCount;
    }
    //#else
    //$$ public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    //$$
    //$$ }
    //$$
    //#endif
}
