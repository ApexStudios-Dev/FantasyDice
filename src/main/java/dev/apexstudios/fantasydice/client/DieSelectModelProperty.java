package dev.apexstudios.fantasydice.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.apexstudios.fantasydice.common.Die;
import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class DieSelectModelProperty implements SelectItemModelProperty<Die> {
    public static final DieSelectModelProperty INSTANCE = new DieSelectModelProperty();
    public static final Type<DieSelectModelProperty, Die> TYPE = Type.create(MapCodec.unit(INSTANCE), Die.CODEC);

    private DieSelectModelProperty() { }

    @Override
    @Nullable
    public Die get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return stack.get(FantasyDice.DIE_COMPONENT);
    }

    @Override
    public Codec<Die> valueCodec() {
        return Die.CODEC;
    }

    @Override
    public Type<DieSelectModelProperty, Die> type() {
        return TYPE;
    }
}
