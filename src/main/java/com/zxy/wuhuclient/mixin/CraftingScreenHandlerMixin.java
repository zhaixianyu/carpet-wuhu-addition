package com.zxy.wuhuclient.mixin;

import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC < 12001
//$$ import net.minecraft.inventory.CraftingInventory;
//#else
import net.minecraft.inventory.RecipeInputInventory;
//#endif
@Mixin(CraftingScreenHandler.class)
public interface CraftingScreenHandlerMixin {
    @Accessor("input")
    //#if MC < 12001
    //$$ CraftingInventory
    //#else
    RecipeInputInventory
    //#endif
    getInput();
}
