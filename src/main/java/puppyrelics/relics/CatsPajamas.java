package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;

import static puppyrelics.ModFile.makeID;

public class CatsPajamas extends AbstractEasyRelic {
    public static final String ID = makeID("CatsPajamas");

    public CatsPajamas() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            flash();
            int healAmount = 9 * (AbstractDungeon.actNum);
            AbstractDungeon.player.heal(healAmount);
            AbstractDungeon.player.gainGold(healAmount);
        }
    }
}
