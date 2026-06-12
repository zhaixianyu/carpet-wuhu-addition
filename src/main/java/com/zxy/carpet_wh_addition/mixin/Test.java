package com.zxy.carpet_wh_addition.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(RecipeManager.class)
//@Mixin(TradeOffers.SellEnchantedToolFactory.class)
public class Test {

//    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getOptional(Lnet/minecraft/registry/tag/TagKey;)Ljava/util/Optional;"), method = "create")
//    private Optional t(Registry instance, TagKey tagKey, Operation<Optional> original) {
//        return original.call(instance, tagKey);
//    }
//    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"))
//    private void a(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci){}
}
