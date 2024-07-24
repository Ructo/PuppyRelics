package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class OneStone extends AbstractEasyRelic {
    public static final String ID = makeID("OneStone");

    public OneStone() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player.hand.size() == 1) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OneStone();
    }
}
