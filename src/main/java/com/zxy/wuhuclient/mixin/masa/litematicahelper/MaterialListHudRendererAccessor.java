package com.zxy.wuhuclient.mixin.masa.litematicahelper;

import com.zxy.wuhuclient.features_list.litematica_helper.Condition;
import com.zxy.wuhuclient.features_list.litematica_helper.Restriction;
import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition("litematica"))
@Mixin(MaterialListHudRenderer.class)
public interface MaterialListHudRendererAccessor
{
    @Accessor(remap = false)
    void setLastUpdateTime(long value);

    @Invoker(remap = false)
    String invokeGetFormattedCountString(int count, int maxStackSize);
}
