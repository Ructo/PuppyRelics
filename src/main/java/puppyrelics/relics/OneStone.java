package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class OneStone extends AbstractEasyClickRelic {
    public static final String ID = makeID("OneStone");
    private boolean effectTriggered = false; // Flag to track if the effect was triggered

    public OneStone() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.hand.size() == 1) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new EnergizedBluePower(AbstractDungeon.player, 1), 1
            ));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player, 1), 1
            ));
            effectTriggered = true; // Set the flag when the effect is triggered
        }
    }

    @Override
    public void atTurnStart() {
        if (effectTriggered) {
            flash(); // Flash at the start of the turn to indicate benefits were gained
            effectTriggered = false; // Reset the flag after flashing
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

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }
}
