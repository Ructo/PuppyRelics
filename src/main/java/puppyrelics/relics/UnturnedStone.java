package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;
import puppyrelics.util.ProAudio;

public class UnturnedStone extends AbstractEasyClickRelic {
    public static final String ID = makeID("UnturnedStone");

    public UnturnedStone() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onShuffle() {
        triggerEffect();
    }

    private void triggerEffect() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(
                AbstractDungeon.player, 8, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UnturnedStone();
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }
}
