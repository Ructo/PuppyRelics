package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomSavable;

import static puppyrelics.ModFile.makeID;

public class CatsPajamas extends AbstractEasyRelic {
    public static final String ID = makeID("CatsPajamas");
    private boolean isProcessing = false; // Flag to prevent infinite loops

    public CatsPajamas() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            flash();
            int healAmount = 9;
            AbstractDungeon.player.heal(healAmount);
        }
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        if (!isProcessing) {
            isProcessing = true; // Set flag to indicate processing
            flash();
            AbstractDungeon.player.gainGold(healAmount);
            isProcessing = false; // Reset flag after processing
        }
        return healAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CatsPajamas();
    }
}
