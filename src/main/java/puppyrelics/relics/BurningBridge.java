package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import basemod.interfaces.AlternateCardCostModifier;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
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
        if (card.costForTurn > EnergyPanel.totalCount) {
            this.flash();
            // Exhaust the card after use if the alternate cost was applied
            action.exhaustCard = true;
        }
    }

    @Override
    public void update() {
        super.update();

        // Ensure that energy is correctly calculated
        int availableEnergy = EnergyPanel.totalCount;

        // Loop through the player's hand to check for cards using the alternate cost
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (shouldGlowRed(card, availableEnergy)) {
                // Add the red glow to the card if the alternate cost will be applied
                CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
                    @Override
                    public boolean test(AbstractCard c) {
                        return shouldGlowRed(c, availableEnergy);
                    }

                    @Override
                    public Color getColor(AbstractCard c) {
                        return Color.RED.cpy();
                    }

                    @Override
                    public String glowID() {
                        return "puppyrelics:burningbridgeglow";
                    }
                });
            } else {
                // Remove the red glow if the card no longer needs the alternate cost
                CardBorderGlowManager.removeGlowInfo("puppyrelics:burningbridgeglow");
            }
        }
    }

    private boolean shouldGlowRed(AbstractCard card, int availableEnergy) {
        // Ensure we're checking against the finalized cost for this turn
        int energyDeficit = card.costForTurn - availableEnergy;
        return energyDeficit > 0 && card.costForTurn >= 0 && !card.freeToPlayOnce;
    }

    @Override
    public boolean canSplitCost(AbstractCard card) {
        return true;
    }

    @Override
    public boolean costEffectActive(AbstractCard card) {
        return true;
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
