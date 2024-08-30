package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.*;

public class BurningBridge extends AbstractEasyClickRelic implements AlternateCardCostModifier {
    public static final String ID = makeID("BurningBridge");

    public BurningBridge() {
        super(ID, RelicTier.SHOP, LandingSound.HEAVY);
    }
    @Override
    public void atBattleStart() {
        this.counter = 0; // Reset the counter at the start of each combat
    }
    @Override
    public int getAlternateResource(AbstractCard card) {
        // For X-cost cards, do not override the normal resource behavior
        if (card.costForTurn == -1) {
            return 0; // Do not provide the alternate resource for X-cost cards
        }

        // Provide an infinite alternate resource for non-X-cost cards
        return 999;
    }

    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend) {
        AbstractPlayer player = AbstractDungeon.player;

        // Only apply the alternate cost if the card has a valid cost and is not an X-cost card
        if (card.costForTurn != -1 && card.costForTurn >= 0 && card.cost >= 0) {
            // Calculate the energy deficit for the card
            int energyDeficit = costToSpend;

            // Take damage equal to the current counter value + the energy deficit for this card
            int totalDamage = this.counter + energyDeficit;

            if (totalDamage > 0) {
                this.flash();
                atb(new LoseHPAction(adp(), adp(), totalDamage));
            }

            // Update the relic counter by adding the energy deficit for this card
            this.counter += energyDeficit;
        }

        return costToSpend;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.counter = 0; // Initialize the counter to 0 when the relic is equipped
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
