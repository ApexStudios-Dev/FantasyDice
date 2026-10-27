package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.data.DataGenUtil;
import dev.apexstudios.fantasydice.api.FantasyDice;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceDataEntryPoint {
    static final Component PACK_DESC = DataGenUtil.packDescription(FantasyDice.ID);

    public FantasyDiceDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createProvider(output -> DataGenUtil.metadata(output, PACK_DESC, PackType.SERVER_DATA));
            event.createProvider(LangProvider::new);
            event.createProvider(ModelProvider::new);
            event.createProvider(ItemTagProvider::new);
        });

        modBus.addListener(GatherDataRegistryEntriesEvent.class, event -> event
                .recipe(RecipeProvider::new)
        );
    }
}
