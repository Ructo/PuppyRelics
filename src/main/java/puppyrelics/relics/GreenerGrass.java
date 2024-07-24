package puppyrelics.relics;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static puppyrelics.ModFile.makeID;

public class GreenerGrass extends AbstractEasyRelic {
    public static final String ID = makeID("GreenerGrass");
    private boolean triggeredThisTurn = false;

    public GreenerGrass() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        triggeredThisTurn = false; // Reset the flag at the start of each turn
    }


    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!triggeredThisTurn && target == AbstractDungeon.player && power.type == AbstractPower.PowerType.BUFF) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, power, power.amount));
            triggeredThisTurn = true; // Set the flag to true after triggering the effect
        }
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
