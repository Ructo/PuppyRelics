package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static puppyrelics.ModFile.makeID;

public class AdventureCatsHat extends AbstractEasyRelic {
    public static final String ID = makeID("AdventureCatsHat");
    private int blockAmount = 0;

    public AdventureCatsHat() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        blockAmount = 0;
        this.setCounter(blockAmount);
    }

    @Override
    public void atTurnStart() {
        blockAmount = 0; // Reset the block amount at the start of each turn
        this.setCounter(blockAmount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            blockAmount++;
            flash();
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, blockAmount));
            this.setCounter(blockAmount); // Update the counter
        }
    }

    @Override
    public void onVictory() {
        this.setCounter(-1); // Hide the counter when the battle is over
    }
}
