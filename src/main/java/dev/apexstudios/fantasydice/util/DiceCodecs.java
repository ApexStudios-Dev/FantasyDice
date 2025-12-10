package dev.apexstudios.fantasydice.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public interface DiceCodecs {
    Codec<Integer> SIDES_CODEC = ExtraCodecs.POSITIVE_INT;
    Codec<String> MATERIAL_CODEC = Codec.STRING.validate(str -> Identifier.isValidNamespace(str) ? DataResult.success(str) : DataResult.error(() -> "Invalid string for dice material: " + str));
}
