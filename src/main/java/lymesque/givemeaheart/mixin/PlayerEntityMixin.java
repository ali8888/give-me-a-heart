package lymesque.givemeaheart.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow protected HungerManager hungerManager;
	@Shadow public PlayerAbilities abilities;

	public PlayerEntityMixin(World world) {
        super(world);
    }


	@Inject(at = @At("TAIL"), method = "initializeAttributes()V")
	private void init(CallbackInfo info) {
		this.getAttributeContainer().get(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20);
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void tickmixin(CallbackInfo info) {
		this.getAttributeContainer().get(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Math.min(this.getHealth(),20));
		this.hungerManager.setFoodLevel(10);
	}

	@Overwrite
	public boolean canConsume(boolean ignoreHunger) {
      return (ignoreHunger || this.getHealth()<20) && !this.abilities.invulnerable;
   	}
}
