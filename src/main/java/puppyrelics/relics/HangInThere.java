package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.actions.common.HealAction;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class HangInThere extends AbstractEasyClickRelic {
    public static final String ID = makeID("HangInThere");

    public HangInThere() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom) {
            flash();
            AbstractDungeon.player.heal(4);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HangInThere();
    }
    @Override
    public void onRightClick() {
        playAudio(ProAudio.cat1);
    }
}
