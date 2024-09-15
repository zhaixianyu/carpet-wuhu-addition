package com.zxy.wuhuclient.mixin.masa.litematicahelper;


import com.zxy.wuhuclient.config.Configs;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.wuhuclient.features_list.litematica_helper.LitematicaHelper.process;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(method = "updateSlotStacks", at = @At("TAIL"))
    private void tweakerMoreAutoContainerProcessorProcess(CallbackInfo ci)
    {
        if(Configs.LITEMATICA_HELPER.getBooleanValue()) process((ScreenHandler)(Object)this);
    }
}
