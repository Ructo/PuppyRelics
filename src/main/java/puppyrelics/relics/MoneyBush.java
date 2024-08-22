package puppyrelics.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.ModFile;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class MoneyBush extends AbstractEasyClickRelic {
    public static final String ID = makeID("MoneyBush");
    private static final int GOLD_PER_ROOM = 5;
    private static final float GOLD_REFUND_PERCENTAGE = 0.30f;
    private boolean isLegacyMode;  // Flag to track legacy mode
    private int totalGoldEarned = 0;
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 1000;

    public MoneyBush() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
        this.isLegacyMode = ModFile.moneyBushLegacyMode;  // Get initial legacy mode status
        updateDescription();
    }

    @Override
    public void atTurnStart() {
        if (isLegacyMode) {
            handleLegacyModeAtTurnStart();
        }
        updateDescription();  // Ensure description is updated
    }

    private void handleLegacyModeAtTurnStart() {
        flash();
        AbstractDungeon.player.gainGold(GOLD_PER_ROOM);  // Gain gold every turn in legacy mode
        totalGoldEarned += GOLD_PER_ROOM;  // Track total gold earned
    }

    // Logic for non-legacy mode to refund gold on loss
    public void onLoseGoldCustom(int goldLost) {
        if (!isLegacyMode && goldLost > 0) {
            flash();
            int goldRefund = Math.round(goldLost * GOLD_REFUND_PERCENTAGE);
            AbstractDungeon.player.gainGold(goldRefund);
            playAudio(ProAudio.cash);
        }
    }

    // Legacy mode: Gold per room
    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (isLegacyMode) {
            flash();
            AbstractDungeon.player.gainGold(GOLD_PER_ROOM);
            totalGoldEarned += GOLD_PER_ROOM;
            updateDescription();
        }
    }

    @Override
    public void onEquip() {
        if (isLegacyMode) {
            int roomsPassed = AbstractDungeon.floorNum;
            int initialGold = roomsPassed * GOLD_PER_ROOM;
            AbstractDungeon.player.gainGold(initialGold);
            totalGoldEarned += initialGold;
        }
    }

    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.cash);
            lastClickTime = currentTime;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return isLegacyMode ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    private void updateDescription() {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MoneyBush();
    }
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 40;
    }
    public void refreshDescription() {
        this.isLegacyMode = ModFile.moneyBushLegacyMode;  // Update legacy mode status
        updateDescription();
    }
}
