package dev.apexstudios.fantasydice.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.apexstudios.fantasydice.DiceItem;
import dev.apexstudios.fantasydice.FantasyDice;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class DiceSidesSelectModelProperty implements SelectItemModelProperty<Integer> {
    public static final DiceSidesSelectModelProperty INSTANCE = new DiceSidesSelectModelProperty();
    public static final Type<DiceSidesSelectModelProperty, Integer> TYPE = Type.create(MapCodec.unit(INSTANCE), FantasyDice.SIDES_CODEC);

    private DiceSidesSelectModelProperty() { }

    @Override
    public Integer get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return DiceItem.getSides(stack);
    }

    @Override
    public Codec<Integer> valueCodec() {
        return FantasyDice.SIDES_CODEC;
    }

    @Override
    public Type<DiceSidesSelectModelProperty, Integer> type() {
        return TYPE;
    }
}
