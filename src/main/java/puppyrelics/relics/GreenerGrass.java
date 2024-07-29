package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static puppyrelics.ModFile.makeID;

public class GreenerGrass extends AbstractEasyRelic implements OnReceivePowerRelic {
    public static final String ID = makeID("GreenerGrass");
    private boolean doubledThisTurn = false;

    public GreenerGrass() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        doubledThisTurn = false;
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
}
