package dev.apexstudios.fantasydice.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.apexstudios.fantasydice.util.DiceCodecs;
import dev.apexstudios.fantasydice.util.DiceRegistries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class DiceSidesSelectModelProperty implements SelectItemModelProperty<Integer> {
    public static final DiceSidesSelectModelProperty INSTANCE = new DiceSidesSelectModelProperty();
    public static final Type<DiceSidesSelectModelProperty, Integer> TYPE = Type.create(MapCodec.unit(INSTANCE), DiceCodecs.SIDES_CODEC);

    private DiceSidesSelectModelProperty() { }

    @Override
    @Nullable
    public Integer get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return stack.get(DiceRegistries.SIDES_COMPONENT);
    }

    @Override
    public Codec<Integer> valueCodec() {
        return DiceCodecs.SIDES_CODEC;
    }

    @Override
    public Type<DiceSidesSelectModelProperty, Integer> type() {
        return TYPE;
    }
}
