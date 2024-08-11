package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;

import static com.badlogic.gdx.math.MathUtils.random;
import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

import com.megacrit.cardcrawl.potions.AbstractPotion;
import puppyrelics.util.ProAudio;

public class IcingOnThePotion extends AbstractEasyClickRelic implements BetterOnUsePotionRelic {
    public static final String ID = makeID("IcingOnThePotion");
    private int cardsOwed = 0; // Track the number of cards owed
    private int soundCounter = 0;

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

    @Override
    public void onRightClick() {
        switch (soundCounter) {
            case 0:
                playAudio(ProAudio.potion1); // Replace with your actual sound key
                break;
            case 1:
                playAudio(ProAudio.potion2); // Replace with your actual sound key
                break;
            case 2:
                playAudio(ProAudio.potion3); // Replace with your actual sound key
                break;
            default:
                break;
        }

        // Update the counter to cycle to the next sound
        soundCounter = (soundCounter + 1) % 3; // This will loop the counter between 0, 1, 2
    }
}
