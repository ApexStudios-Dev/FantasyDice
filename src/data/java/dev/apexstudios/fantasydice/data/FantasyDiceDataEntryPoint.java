package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.data.ProviderTypes;
import dev.apexstudios.apexcore.api.data.ResourceGenerator;
import dev.apexstudios.apexcore.api.util.StringHelper;
import dev.apexstudios.fantasydice.common.Die;
import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceDataEntryPoint {
    public FantasyDiceDataEntryPoint(IEventBus modBus) {
        ResourceGenerator.of(modBus, generator -> {
            generator.pack()
                    .providing(ProviderTypes.LANGUAGE, (context, provider) -> {
                        provider.addCreativeModeTab(FantasyDice.CREATIVE_MODE_TAB, "Fantasy's Dice");
                        provider.addEntityType(FantasyDice.DICE_ENTITY, "Dice");
                        provider.addGameRule(FantasyDice.RULE_DICE_LIFETIME, "Dice entity lifetime", "How long thrown dice should persist in world (in seconds)");
                        provider.addItem(FantasyDice.DICE_ITEM, "Dice");
                        provider.add(Die.DESCRIPTION_ID, "%s d%s");

                        for(var material : Die.DEFAULT_MATERIALS) {
                            provider.add(DieDataUtils.getMaterialKey(material), StringHelper.toEnglishName(material));
                        }
                    })
                    .providing(ProviderTypes.MODELS, (context, provider) -> DieDataUtils.createDiceModel(FantasyDice.DICE_ITEM, provider.itemModels()))
                    .providing(ProviderTypes.RECIPES, (context, provider) -> {
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM,"wooden", ItemTags.PLANKS, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "stone", Tags.Items.STONES, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "bone", Tags.Items.BONES, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "iron", Tags.Items.INGOTS_IRON, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "golden", Tags.Items.INGOTS_GOLD, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "diamond", Tags.Items.GEMS_DIAMOND, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "emerald", Tags.Items.GEMS_EMERALD, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "netherite", Tags.Items.INGOTS_NETHERITE, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "copper", Tags.Items.INGOTS_COPPER, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "ender", Tags.Items.ENDER_PEARLS, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "frozen", Items.ICE, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "slime", Tags.Items.SLIME_BALLS, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "redstone", Tags.Items.DUSTS_REDSTONE, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "paper", Items.PAPER, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "amethyst", Tags.Items.GEMS_AMETHYST, provider);
                        DieDataUtils.recipe(FantasyDice.DICE_ITEM, "chocolate", Tags.Items.CROPS_COCOA_BEAN, provider);
                    })
                    .providing(ProviderTypes.ENTITY_TYPE_TAGS, (context, provider) -> provider
                            .tag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED).withElement(FantasyDice.DICE_ENTITY)
                    );
        });
    }
}
