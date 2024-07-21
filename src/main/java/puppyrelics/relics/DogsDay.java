package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static puppyrelics.ModFile.makeID;

public class DogsDay extends AbstractEasyRelic {
    public static final String ID = makeID("DogsDay");
    private boolean usedThisCombat = false;

    public DogsDay() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false; // Reset the flag at the start of each combat
        checkAndTriggerEffect(); // Check health at the start of combat
    }

    @Override
    public void atTurnStart() {
        checkAndTriggerEffect(); // Check health at the start of each turn
    }

    private void checkAndTriggerEffect() {
        if (!usedThisCombat && AbstractDungeon.player.currentHealth <= (AbstractDungeon.player.maxHealth * 0.5)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 3));
            usedThisCombat = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
