package lymesque.givemeaheart.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.material.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {

    @Shadow @Final private MinecraftClient client;
    @Shadow private long heartJumpEndTick;
    @Shadow private int ticks;
    @Shadow private int renderHealthValue;
    @Shadow private long lastHealthCheckTime;
    @Shadow private int lastHealthValue;
    @Shadow @Final private Random random;
    
    @Overwrite
    private void renderStatusBars(Window window) {
      if (this.client.getCameraEntity() instanceof PlayerEntity) {
         PlayerEntity playerEntity = (PlayerEntity)this.client.getCameraEntity();
         int i = MathHelper.ceil(playerEntity.getHealth());
         boolean bl = this.heartJumpEndTick > (long)this.ticks && (this.heartJumpEndTick - (long)this.ticks) / 3L % 2L == 1L;
         if (i < this.renderHealthValue && playerEntity.timeUntilRegen > 0) {
            this.lastHealthCheckTime = MinecraftClient.getTime();
            this.heartJumpEndTick = (long)(this.ticks + 20);
         } else if (i > this.renderHealthValue && playerEntity.timeUntilRegen > 0) {
            this.lastHealthCheckTime = MinecraftClient.getTime();
            this.heartJumpEndTick = (long)(this.ticks + 10);
         }

         if (MinecraftClient.getTime() - this.lastHealthCheckTime > 1000L) {
            this.renderHealthValue = i;
            this.lastHealthValue = i;
            this.lastHealthCheckTime = MinecraftClient.getTime();
         }

         this.renderHealthValue = i;
         int j = this.lastHealthValue;
         this.random.setSeed((long)(this.ticks * 312871));
         EntityAttributeInstance entityAttributeInstance = playerEntity.initializeAttribute(EntityAttributes.GENERIC_MAX_HEALTH);
         int l = window.getWidth() / 2 - 91;
         int m = window.getWidth() / 2 + 91;
         int n = window.getHeight() - 39;
         float f = (float)entityAttributeInstance.getValue();
         int o = MathHelper.ceil(playerEntity.getAbsorption());
         int p = MathHelper.ceil((f + (float)o) / 2.0F / 10.0F);
         int q = Math.max(10 - (p - 2), 3);
         int r = n - (p - 1) * q - 10;
         int s = n;
         int t = o;
         int u = playerEntity.getArmorProtectionValue();
         int v = -1;
         if (playerEntity.hasStatusEffect(StatusEffects.REGENERATION)) {
            v = this.ticks % MathHelper.ceil(f + 5.0F);
         }

         this.client.profiler.push("armor");

         int w;
         int x;
         for(w = 0; w < 10; ++w) {
            if (u > 0) {
               x = l + w * 8;
               if (w * 2 + 1 < u) {
                  this.drawTexture(x, r, 34, 9, 9, 9);
               }

               if (w * 2 + 1 == u) {
                  this.drawTexture(x, r, 25, 9, 9, 9);
               }

               if (w * 2 + 1 > u) {
                  this.drawTexture(x, r, 16, 9, 9, 9);
               }
            }
         }

         this.client.profiler.swap("health");

         int y;
         int z;
         int aa;
         int ab;
         for(w = MathHelper.ceil((f + (float)o) / 2.0F) - 1; w >= 0; --w) {
            x = 16;
            if (playerEntity.hasStatusEffect(StatusEffects.POISON)) {
               x += 36;
            } else if (playerEntity.hasStatusEffect(StatusEffects.WITHER)) {
               x += 72;
            }

            y = 0;
            if (bl) {
               y = 1;
            }

            z = MathHelper.ceil((float)(w + 1) / 10.0F) - 1;
            aa = l + w % 10 * 8;
            ab = n - z * q;
            if (i <= 4) {
               ab += this.random.nextInt(2);
            }

            if (t <= 0 && w == v) {
               ab -= 2;
            }

            int ac = 0;
            if (playerEntity.world.getLevelProperties().isHardcore()) {
               ac = 5;
            }

            this.drawTexture(aa, ab, 16 + y * 9, 9 * ac, 9, 9);
            if (bl) {
               if (w * 2 + 1 < j) {
                  this.drawTexture(aa, ab, x + 54, 9 * ac, 9, 9);
               }

               if (w * 2 + 1 == j) {
                  this.drawTexture(aa, ab, x + 63, 9 * ac, 9, 9);
               }
            }

            if (t > 0) {
               if (t == o && o % 2 == 1) {
                  this.drawTexture(aa, ab, x + 153, 9 * ac, 9, 9);
                  --t;
               } else {
                  this.drawTexture(aa, ab, x + 144, 9 * ac, 9, 9);
                  t -= 2;
               }
            } else {
               if (w * 2 + 1 < i) {
                  this.drawTexture(aa, ab, x + 36, 9 * ac, 9, 9);
               }

               if (w * 2 + 1 == i) {
                  this.drawTexture(aa, ab, x + 45, 9 * ac, 9, 9);
               }
            }
         }

         

         this.client.profiler.swap("air");
         if (playerEntity.isSubmergedIn(Material.WATER)) {
            x = this.client.player.getAir();
            y = MathHelper.ceil((double)(x - 2) * 10.0 / 300.0);
            z = MathHelper.ceil((double)x * 10.0 / 300.0) - y;

            for(aa = 0; aa < y + z; ++aa) {
               if (aa < y) {
                  this.drawTexture(m - aa * 8 - 9, s, 16, 18, 9, 9);
               } else {
                  this.drawTexture(m - aa * 8 - 9, s, 25, 18, 9, 9);
               }
            }
         }

         this.client.profiler.pop();
      }
   }
}
