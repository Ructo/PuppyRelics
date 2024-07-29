package puppyrelics.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.RelicWithButton;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

import static puppyrelics.ModFile.makeID;

public class NikTheGOAT extends AbstractEasyClickRelic implements RelicWithButton {
    public static final String ID = makeID("NikTheGOAT");
    private static final String textureString = "puppyrelicsResources/images/relics/NikTheGOATButton.png";
    private boolean usedThisTurn = false;
    private AbstractCard lastExhaustedCard = null;

    public NikTheGOAT() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        usedThisTurn = false; // Reset the flag at the start of each turn
        lastExhaustedCard = null; // Reset the last exhausted card
        grayscale = false; // Reset the grayscale
        updateDescription();
    }

    @Override
    public Texture getTexture() {
        return ImageMaster.loadImage(textureString);
    }

    @Override
    public void onButtonPress() {
        if (!usedThisTurn && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                        lastExhaustedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                    }
                    AbstractDungeon.actionManager.addToBottom(new RecycleAction());
                    isDone = true;
                }
            });

            usedThisTurn = true; // Mark the relic as used for this turn
            grayscale = true; // Turn the relic grayscale
            updateDescription();
        }
    }

    @Override
    public boolean isButtonDisabled() {
        return grayscale;
    }

    @Override
    public ArrayList<PowerTip> getHoverTips() {
        return tips;
    }

    @Override
    public void onRightClick() {
        if (!usedThisTurn && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                        lastExhaustedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                    }
                    AbstractDungeon.actionManager.addToBottom(new RecycleAction());
                    isDone = true;
                }
            });

            usedThisTurn = true; // Mark the relic as used for this turn
            grayscale = true; // Turn the relic grayscale
            updateDescription();
        }
    }
    @Override
    public String getUpdatedDescription() {
        if (usedThisTurn && lastExhaustedCard != null) {
            int energyGained = lastExhaustedCard.costForTurn == -1 ? EnergyPanel.getCurrentEnergy() : lastExhaustedCard.costForTurn;
            return DESCRIPTIONS[1] + lastExhaustedCard.name + DESCRIPTIONS[2] + energyGained + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0];
        }
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
}
