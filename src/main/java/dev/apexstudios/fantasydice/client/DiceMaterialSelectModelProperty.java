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

public final class DiceMaterialSelectModelProperty implements SelectItemModelProperty<String> {
    public static final DiceMaterialSelectModelProperty INSTANCE = new DiceMaterialSelectModelProperty();
    public static final Type<DiceMaterialSelectModelProperty, String> TYPE = Type.create(MapCodec.unit(INSTANCE), FantasyDice.MATERIAL_CODEC);

    private DiceMaterialSelectModelProperty() { }

    @Override
    public String get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return DiceItem.getMaterial(stack);
    }

    @Override
    public Codec<String> valueCodec() {
        return Codec.STRING;
    }

    @Override
    public Type<DiceMaterialSelectModelProperty, String> type() {
        return TYPE;
    }
}
