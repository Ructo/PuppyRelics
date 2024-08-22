package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class GreenerGrass extends AbstractEasyClickRelic implements OnReceivePowerRelic {
    public static final String ID = makeID("GreenerGrass");
    private boolean doubledThisTurn = false;

    public GreenerGrass() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        doubledThisTurn = false;

        // Only start pulsing if in combat
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            beginLongPulse();
        } else {
            stopPulse(); // Ensure pulse is stopped when not in combat
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, int stackAmount) {
        if ((stackAmount > 0 || power.canGoNegative) && target == AbstractDungeon.player && power.type == AbstractPower.PowerType.BUFF && !doubledThisTurn) {
            flash();
            power.amount *= 2;
            power.updateDescription();
            stopPulse();  // Stop pulsing after effect is triggered
            doubledThisTurn = true;
            return stackAmount * 2;
        }

        return stackAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GreenerGrass();
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.grass);
    }
}
