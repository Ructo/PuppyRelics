package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class BiggerThumb extends AbstractEasyRelic {
    public static final String ID = makeID("BiggerThumb");

    public BiggerThumb() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        AbstractMonster target = null;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (target == null || m.currentHealth > target.currentHealth) {
                target = m;
            }
        }
        if (target != null) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, 1, false), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BiggerThumb();
    }
}
