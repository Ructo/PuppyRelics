package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.ModFile;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class NikTheGOAT extends AbstractEasyClickRelic {
    public static final String ID = makeID("NikTheGOAT");
    private boolean usedThisTurn = false;
    private boolean isLegacyMode; // Stores the mode after initialization
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 1000;

    public NikTheGOAT() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
        this.isLegacyMode = ModFile.legacyMode;
        updateDescription();
    }

    @Override
    public void atTurnStartPostDraw() {
        if (isLegacyMode) {
            handleLegacyModeAtTurnStart();
        } else {
            handleNonLegacyModeAtTurnStart();
        }

        updateDescription();
    }

    private void handleLegacyModeAtTurnStart() {
        usedThisTurn = false;
        grayscale = false;
    }

    private void handleNonLegacyModeAtTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RecycleAction());
        updateDescription();
    }

    @Override
    public void onRightClick() {
        if (isLegacyMode) {
            handleLegacyModeRightClick();
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime >= COOLDOWN_MS) {
                playAudio(ProAudio.goat);
                lastClickTime = currentTime;
            }
        }
    }
    private void handleLegacyModeRightClick() {
        if (!usedThisTurn && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new RecycleAction());
            usedThisTurn = true;
            grayscale = true;
            updateDescription();
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
        return new NikTheGOAT();
    }
    public void refreshDescription() {
        this.isLegacyMode = ModFile.legacyMode;
        updateDescription();
    }
}
