package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class BlessingInDisguise extends AbstractEasyRelic {
    public static final String ID = makeID("BlessingInDisguise");
    private boolean triggeredThisTurn = false;

    public BlessingInDisguise() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        triggeredThisTurn = false; // Reset the flag at the start of each turn
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (!triggeredThisTurn && (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
            triggeredThisTurn = true; // Set the flag to true after triggering the effect
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlessingInDisguise();
    }
}
