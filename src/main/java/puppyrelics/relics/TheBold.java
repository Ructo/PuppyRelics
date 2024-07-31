package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class TheBold extends AbstractEasyRelic {
    public static final String ID = makeID("TheBold");

    public TheBold() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom) {
            flash();
            AbstractDungeon.player.increaseMaxHp(2, true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheBold();
    }
}
