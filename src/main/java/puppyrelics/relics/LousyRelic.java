package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class LousyRelic extends AbstractEasyClickRelic {
    public static final String ID = makeID("LousyRelic");
    private boolean usedThisCombat = false;

    public LousyRelic() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        usedThisCombat = false; // Reset the flag at the start of each combat
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (!usedThisCombat && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            flash();
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 12));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 1), 1));
            usedThisCombat = true; // Set the flag to true after triggering the effect
        }
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LousyRelic();
    }
}
