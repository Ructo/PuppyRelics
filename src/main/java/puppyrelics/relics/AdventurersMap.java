package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomSavable;

import static puppyrelics.ModFile.makeID;

public class AdventurersMap extends AbstractEasyRelic implements CustomSavable<Integer> {
    public static final String ID = makeID("AdventurersMap");
    private static final int EVENT_THRESHOLD = 3;
    private int eventRoomCounter = 0;

    public AdventurersMap() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
        this.counter = eventRoomCounter;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom) {
            eventRoomCounter++;
            this.counter = eventRoomCounter;
            if (eventRoomCounter >= EVENT_THRESHOLD) {
                flash();
                AbstractDungeon.cardRewardScreen.open(
                        AbstractDungeon.getRewardCards(),
                        null,
                        "Choose a Card"
                );
                eventRoomCounter = 0; // Reset the counter
                this.counter = eventRoomCounter;
            }
        }
    }

    @Override
    public Integer onSave() {
        return eventRoomCounter;
    }

    @Override
    public void onLoad(Integer counter) {
        if (counter != null) {
            eventRoomCounter = counter;
            this.counter = eventRoomCounter;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AdventurersMap();
    }
}
