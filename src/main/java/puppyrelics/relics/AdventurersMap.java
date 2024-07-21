package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

import static puppyrelics.ModFile.makeID;

public class AdventurersMap extends AbstractEasyRelic {
    public static final String ID = makeID("AdventurersMap");

    public AdventurersMap() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom) {
            flash();
            AbstractDungeon.cardRewardScreen.open(
                    AbstractDungeon.getRewardCards(),
                    null,
                    "Choose a Card"
            );
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
