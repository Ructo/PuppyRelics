package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class WeatheredUmbrella extends AbstractEasyClickRelic implements OnApplyPowerRelic {
    public static final String ID = makeID("WeatheredUmbrella");
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 4000;
    private boolean blockNext = false;

    public WeatheredUmbrella() {
        super(ID,RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        blockNext = false; // reset each combat
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        blockNext = false;
        this.grayscale = false;
    }
    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target != AbstractDungeon.player) return true;
        if (power.type != AbstractPower.PowerType.DEBUFF) return true;
        if (ArtifactPower.POWER_ID.equals(power.ID)) return true;

        if (!blockNext) {
            flash();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new ArtifactPower(AbstractDungeon.player, 1), 1)); // arm for next debuff
        }

        blockNext = !blockNext;
        this.grayscale = !blockNext;  // gray = not armed, color = armed

        return true; // don’t cancel the incoming power
    }

    @Override
    public void atTurnStart() {
        // Clear gray if last turn was used
        this.grayscale = false;
    }

    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.rain);
            lastClickTime = currentTime;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WeatheredUmbrella();
    }

}
