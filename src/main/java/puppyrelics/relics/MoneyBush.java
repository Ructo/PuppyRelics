package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class MoneyBush extends AbstractEasyClickRelic {
    public static final String ID = makeID("MoneyBush");
    private static final int GOLD_PER_ROOM = 5;
    private int totalGoldEarned = 0;

    public MoneyBush() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        int roomsPassed = AbstractDungeon.floorNum; // Floors passed including the current floor
        int initialGold = roomsPassed * GOLD_PER_ROOM; // Gold based on the number of rooms passed

        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(initialGold);
        totalGoldEarned += initialGold;
        updateDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        flash();
        AbstractDungeon.player.gainGold(GOLD_PER_ROOM);
        totalGoldEarned += GOLD_PER_ROOM;
        updateDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (totalGoldEarned == 0) {
            int roomsPassed = AbstractDungeon.floorNum; // Floors passed including the current floor
            int initialGold = roomsPassed * GOLD_PER_ROOM; // Gold based on the number of rooms passed
            return "Whenever you climb a floor, gain [#FFD700]5[] gold. Upon pickup, this benefit is retroactive. ([#FFD700]" + initialGold + "[] gold)";
        } else {
            return "Whenever you climb a floor, gain [#FFD700]5[] gold. You have gained [#FFD700]" + totalGoldEarned + "[] gold.";
        }
    }

    private void updateDescription() {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
    @Override
    public void onRightClick() {
        playAudio(ProAudio.cash);
    }
}
