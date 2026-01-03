package dev.apexstudios.fantasydice.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.apexstudios.fantasydice.common.util.Dice;
import dev.apexstudios.fantasydice.common.util.DiceCodecs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class DiceMaterialSelectModelProperty implements SelectItemModelProperty<String> {
    public static final DiceMaterialSelectModelProperty INSTANCE = new DiceMaterialSelectModelProperty();
    public static final Type<DiceMaterialSelectModelProperty, String> TYPE = Type.create(MapCodec.unit(INSTANCE), DiceCodecs.MATERIAL_CODEC);

    private DiceMaterialSelectModelProperty() { }

    @Override
    @Nullable
    public String get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return Dice.getMaterial(stack);
    }

    @Override
    public Codec<String> valueCodec() {
        return DiceCodecs.MATERIAL_CODEC;
    }

    @Override
    public Type<DiceMaterialSelectModelProperty, String> type() {
        return TYPE;
    }
}
