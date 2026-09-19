package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.util.StringHelper;
import dev.apexstudios.fantasydice.common.Die;
import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

final class FDLanguageProvider extends LanguageProvider {
    FDLanguageProvider(PackOutput output) {
        super(output, FantasyDice.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addKey(FantasyDice.CREATIVE_MODE_TAB, "itemGroup", "Fantasy's Dice");
        addEntityType(FantasyDice.DICE_ENTITY, "Dice");
        addGameRule(FantasyDice.RULE_DICE_LIFETIME, "Dice entity lifetime", "How long thrown dice should persist in world (in seconds)");
        addItem(FantasyDice.DICE_ITEM, "Dice");
        add(Die.DESCRIPTION_ID, "%s d%s");

        for(var material : Die.DEFAULT_MATERIALS) {
            add(DieDataUtils.getMaterialKey(material), StringHelper.toEnglishName(material));
        }
    }
}
