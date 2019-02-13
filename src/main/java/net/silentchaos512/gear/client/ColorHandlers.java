package net.silentchaos512.gear.client;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.init.ModItems;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.utils.Color;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SilentGear.MOD_ID, value = Dist.CLIENT)
public final class ColorHandlers {
    public static Map<String, Integer[]> gearColorCache = new HashMap<>();

    private ColorHandlers() {}

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        if (itemColors == null) {
            SilentGear.LOGGER.error("ItemColors is null?", new IllegalStateException("wat?"));
            return;
        }

        // Tools/Armor - mostly used for broken color, but colors could be changed at any time
        itemColors.register(ColorHandlers::getGearLayerColor, ModItems.gearClasses.values()
                .stream()
                .map(ICoreItem::asItem).toArray(Item[]::new)
        );
    }

    private static int getGearLayerColor(ItemStack stack, int tintIndex) {
        if (!(stack.getItem() instanceof ICoreItem) || tintIndex < 0) return Color.VALUE_WHITE;

        String modelKey = GearData.getCachedModelKey(stack, 0);
        Integer[] colors = gearColorCache.get(modelKey);
        return colors != null && tintIndex < colors.length ? colors[tintIndex] : Color.VALUE_WHITE;
    }
}
