package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static puppyrelics.ModFile.makeID;

public class GreenerGrass extends AbstractEasyRelic implements OnReceivePowerRelic {
    public static final String ID = makeID("GreenerGrass");
    private boolean doubledThisTurn = false;
    private boolean energyReducedThisCombat = false;

    public GreenerGrass() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        doubledThisTurn = false;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !energyReducedThisCombat) {
            AbstractDungeon.player.energy.energy--;
        }
    }

    @Override
    public void atPreBattle() {
        if (!energyReducedThisCombat) {
            AbstractDungeon.player.energy.energyMaster--;
            energyReducedThisCombat = true;
        }
        // Set the current energy to max energy minus 1 for the first turn
        AbstractDungeon.player.energy.energy = AbstractDungeon.player.energy.energyMaster;
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
    public void onVictory() {
        if (energyReducedThisCombat) {
            AbstractDungeon.player.energy.energyMaster++;
            energyReducedThisCombat = false;
        }
    }

    @Override
    public void onEquip() {
        // Reduce energy master immediately when the relic is equipped
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void onUnequip() {
        // Restore energy master when the relic is unequipped
        AbstractDungeon.player.energy.energyMaster++;
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
