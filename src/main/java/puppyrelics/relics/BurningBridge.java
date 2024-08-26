package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.*;

public class BurningBridge extends AbstractEasyClickRelic implements AlternateCardCostModifier {
    public static final String ID = makeID("BurningBridge");
    private static final int HP_PER_ENERGY = 1; // Amount of HP lost per missing energy

    public BurningBridge() {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public int getAlternateResource(AbstractCard card) {
        return 999;
    }

    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend) {
        AbstractPlayer player = AbstractDungeon.player;

        if (card.costForTurn >= 0 && card.cost >= 0) {
            this.flash();
            atb(new LoseHPAction(adp(), adp(), costToSpend * HP_PER_ENERGY));
        }
        return costToSpend;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // Check if the alternate cost (HP) was applied
        if (card.costForTurn > EnergyPanel.totalCount && !card.freeToPlay()) {
            this.flash();
            // Exhaust the card after use if the alternate cost was applied
            action.exhaustCard = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BurningBridge();
    }

    @Override
    public void onRightClick() {
        // Play the squeak sound when the relic is right-clicked
        playAudio(ProAudio.sureaboutthat);
    }
}
