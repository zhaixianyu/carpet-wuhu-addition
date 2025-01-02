package com.zxy.carpet_wh_addition.config;


public class CarpetWuHuSettings {
    public static final String WUHU = "wuhu";

    //远程交互容器
    @Rule(categories = WUHU)
    public static boolean remoteOpenInventory = false;
    //无限无需箭
    @Rule(categories = WUHU)
    public static boolean infiniteNotRequireArrows = false;
    //盔甲架无视潜影贝子弹
    @Rule(categories = WUHU)
    public static boolean armorStandIgnoredShulkerBullet = false;
    //更新野生龙龙战数据
    @Rule(categories = WUHU)
    public static boolean dragonsDropMoreExperience = false;
    //手长
    @Rule(categories = WUHU)
    public static double handLength = -1;
    //铁砧附魔等级解除限制
    @Rule(categories = WUHU)
    public static boolean anvilEnchantRemoveRestriction = false;
    //无限宝库
    @Rule(categories = WUHU)
    public static boolean infiniteTreasureTrove = false;
    //自动经验修补
    @Rule(categories = WUHU)
    public static boolean autoMending = false;
    //carpet命令搜索
    @Rule(categories = WUHU)
    public static boolean commandSearch = true;
}
