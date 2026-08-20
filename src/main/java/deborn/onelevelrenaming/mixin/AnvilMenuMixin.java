package deborn.onelevelrenaming.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow private @Nullable String itemName;
    @Shadow private DataSlot cost;

    @Inject(method = "createResult", at = @At("TAIL"))
    private void oneLevelRename(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;

        ItemStack input = self.getSlot(AnvilMenu.INPUT_SLOT).getItem();
        ItemStack addition = self.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem();
        ItemStack result = self.getSlot(AnvilMenu.RESULT_SLOT).getItem();

        if (!addition.isEmpty()) return;
        if (input.isEmpty() || result.isEmpty()) return;

        boolean renaming;
		if (!StringUtil.isBlank(this.itemName)) {
			if (this.itemName == null) return;
            renaming = !this.itemName.equals(input.getHoverName().getString());
        } else {
            renaming = input.has(DataComponents.CUSTOM_NAME);
        }

        if (renaming && this.cost.get() > 0) {
            this.cost.set(1);
        }
    }
}