package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.ModFile;

import static puppyrelics.ModFile.makeID;

public class NikTheGOAT extends AbstractEasyClickRelic {
    public static final String ID = makeID("NikTheGOAT");
    private boolean usedThisTurn = false;
    private boolean isLegacyMode; // Stores the mode after initialization

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
