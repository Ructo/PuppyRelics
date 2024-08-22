package puppyrelics.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class Hyperactivity extends AbstractEasyClickRelic {
    public static final String ID = makeID("Hyperactivity");
    private int exhaustCounter = 0;

    public Hyperactivity() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
        this.counter = exhaustCounter;
    }

    @Override
    public void onExhaust(AbstractCard card) {
        exhaustCounter++;
        this.counter = exhaustCounter; // Update the visible counter

        // Trigger when 4 cards have been exhausted
        if (exhaustCounter >= 4) {
            flash();
            AbstractDungeon.player.gainEnergy(1);
            exhaustCounter = 0;  // Reset the counter after triggering
            this.counter = exhaustCounter; // Update the visible counter
        }
    }

    @Override
    public void onEquip() {
        this.counter = exhaustCounter;
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.yippee);
    }
}
