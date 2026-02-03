package lymesque.givemeaheart.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(FoodItem.class)
public class FoodItemMixin {

    //@Shadow @Final public int eatingTime;

    //@Inject(method = "<init>(IFZ)V", at = @At("HEAD"))
    //public void fooditemconst(CallbackInfo ci) {
    //  this.eatingTime = 8;
    //}

    @Overwrite
    public int getMaxUseTime(ItemStack stack) {
      return 2;
    }
    
    @Inject(method = "method_3367", at = @At("HEAD"))
    public void increaseHealth(ItemStack stack, World world, LivingEntity entity,  CallbackInfoReturnable<ItemStack> ci) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)entity;
            playerEntity.getAttributeContainer().get(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(playerEntity.getMaxHealth()+2);
            playerEntity.heal(2);
        }
    }
}
