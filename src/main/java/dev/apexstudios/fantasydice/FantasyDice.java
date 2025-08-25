package dev.apexstudios.fantasydice;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.apexstudios.apexcore.lib.registree.Registree;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredDataComponent;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredEntity;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredItem;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FantasyDice.ID)
public final class FantasyDice {
    public static final String ID = "fantasydice";
    public static final Registree REGISTREE = new Registree(ID);

    public static final int[] DEFAULT_SIDES = new int[] { 4, 6, 8, 10, 12, 20 };
    public static final int BASE_SIDES = DEFAULT_SIDES[0];
    public static final Codec<Integer> SIDES_CODEC = ExtraCodecs.POSITIVE_INT;
    public static final DeferredDataComponent<Integer> SIDES_COMPONENT = REGISTREE.registerDataComponent("sides", SIDES_CODEC);

    public static final String[] DEFAULT_MATERIALS = new String[] {
            "wooden", "stone", "bone", "iron", "golden", "diamond", "emerald", "netherite", "copper", "ender", "frozen",
            "slime", "redstone", "paper", "amethyst", "chocolate"
    };
    public static final String BASE_MATERIAL = DEFAULT_MATERIALS[0];
    public static final Codec<String> MATERIAL_CODEC = Codec.STRING.validate(
            str -> isValidMaterial(str) ? DataResult.success(str) : DataResult.error(() -> "Invalid string for dice material: " + str)
    );
    public static final DeferredDataComponent<String> MATERIAL_COMPONENT = REGISTREE.registerDataComponent("material", MATERIAL_CODEC);

    public static final DeferredItem<DiceItem> DICE_ITEM = REGISTREE.registerItem("dice", DiceItem::new, properties -> properties.stacksTo(8));

    public static final DeferredEntity<DiceEntity> DICE_ENTITY = REGISTREE.registerEntity("dice", DiceEntity::new, MobCategory.MISC, properties -> properties
            .noLootTable()
            .sized(.25F, .25F)
            .eyeHeight(ItemEntity.EYE_HEIGHT)
            .clientTrackingRange(6)
            .updateInterval(SharedConstants.TICKS_PER_SECOND)
    );

    public static final ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = REGISTREE.registerCreativeModeTab("dice", DICE_ITEM::toStack, (parameters, output) -> {
        for(var material : DEFAULT_MATERIALS) {
            for(var side : DEFAULT_SIDES) {
                output.accept(DiceItem.create(side, material));
            }
        }
    });

    public FantasyDice(IEventBus modBus) {
        REGISTREE.registerEvents(modBus);
    }

    private static boolean isValidMaterial(String material) {
        for(var c : material.toCharArray()) {
            if(!isValidMaterialChar(c))
                return false;
        }

        return true;
    }

    private static boolean isValidMaterialChar(char c) {
        return c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    public static ResourceLocation identifier(String identifier) {
        return REGISTREE.registryName(identifier);
    }
}
