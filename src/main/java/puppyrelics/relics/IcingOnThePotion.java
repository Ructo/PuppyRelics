package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import static puppyrelics.ModFile.makeID;

import com.megacrit.cardcrawl.potions.AbstractPotion;

public class IcingOnThePotion extends AbstractEasyRelic implements BetterOnUsePotionRelic{
    public static final String ID = makeID("IcingOnThePotion");
    private int cardsOwed = 0; // Track the number of cards owed

    public IcingOnThePotion() {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void betterOnUsePotion(AbstractPotion abstractPotion) {
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        } else {
            cardsOwed++; // Increment the owed card draws
            updateCounter();
        }
    }

    @Override
    public void atBattleStart() {
        if (cardsOwed > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, cardsOwed));
            cardsOwed = 0; // Reset the owed cards after drawing them
            updateCounter(); // Clear the counter display
        }
    }

    @Override
    public void onVictory() {
        // Do not reset cardsOwed here, it carries over to the next combat
    }

    private void updateCounter() {
        if (cardsOwed > 0) {
            this.counter = cardsOwed;
        } else {
            this.counter = -1; // Clear the counter display when no cards are owed
        }
    }

    @Override
    public void onEquip() {
        updateCounter(); // Initialize the counter when the relic is equipped
    }

    @Override
    public AbstractRelic makeCopy() {
        return new IcingOnThePotion();
    }
}
