package puppyrelics.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static puppyrelics.ModFile.makeID;

public class Hyperactivity extends AbstractEasyRelic {
    public static final String ID = makeID("Hyperactivity");
    private int exhaustCounter = 0;

    public Hyperactivity() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        exhaustCounter++;
        if (exhaustCounter % 4 == 0) {
            flash();
            AbstractDungeon.player.gainEnergy(1);
        }
    }
}
