package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class DogsDay extends AbstractEasyClickRelic {
    public static final String ID = makeID("DogsDay");
    private boolean usedThisCombat = false;
    private boolean toggle = false;

    public DogsDay() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false; // Reset the flag at the start of each combat
        checkAndTriggerEffect(); // Check health at the start of combat
        if (shouldFlash()) {
            flash(); // Flash at the start of combat if health is below threshold
            stopPulse(); // Stop pulsing after combat starts
        }
    }

    @Override
    public void atTurnStart() {
        checkAndTriggerEffect(); // Check health at the start of each turn
    }

    private void checkAndTriggerEffect() {
        if (!usedThisCombat && AbstractDungeon.player.currentHealth <= (AbstractDungeon.player.maxHealth * 0.5)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 3));
            usedThisCombat = true;
        }
    }

    @Override
    public void update() {
        super.update();

        // Ensure we're in a run before checking health and starting pulsing
        if (AbstractDungeon.isPlayerInDungeon()) {
            if (shouldFlash() && !this.pulse) {
                beginLongPulse(); // Start pulsing if health is below threshold outside of combat
            } else if (!shouldFlash() && this.pulse) {
                stopPulse(); // Stop pulsing when the condition no longer applies
            }
        }
    }

    private boolean shouldFlash() {
        // Ensure we're in a run before checking health
        return AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.currentHealth <= (AbstractDungeon.player.maxHealth * 0.5);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onRightClick() {
        if (toggle) {
            playAudio(ProAudio.bark1);
        } else {
            playAudio(ProAudio.bark2);
        }
        toggle = !toggle;
    }
}
