package puppyrelics.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.RelicWithButton;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import puppyrelics.ModFile;

import java.util.ArrayList;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.ModFile.legacyMode;

public class NikTheGOAT extends AbstractEasyClickRelic implements RelicWithButton {
    public static final String ID = makeID("NikTheGOAT");
    private static final String textureString = "puppyrelicsResources/images/relics/NikTheGOATButton.png";
    private boolean usedThisTurn = false;

    public NikTheGOAT() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
        updateDescription(); // Ensure description is updated according to the mode
    }

    @Override
    public void atTurnStart() {
        usedThisTurn = false;
        grayscale = false;

        if (!legacyMode) {
            // New mode: Exhaust a card at the start of each turn
            flash();
            ArrayList<AbstractCard> handCards = AbstractDungeon.player.hand.group;
            if (!handCards.isEmpty()) {
                AbstractCard cardToExhaust = handCards.get(AbstractDungeon.cardRandomRng.random(handCards.size() - 1));
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(cardToExhaust, AbstractDungeon.player.hand));
            }
        }

        updateDescription();
    }

    @Override
    public Texture getTexture() {
        return ImageMaster.loadImage(textureString);
    }

    @Override
    public ArrayList<PowerTip> getHoverTips() {
        ArrayList<PowerTip> tips = new ArrayList<>();
        tips.add(new PowerTip(this.name, this.description));
        return tips;
    }

    @Override
    public void onButtonPress() {
        if (legacyMode && !usedThisTurn && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // Legacy mode: Exhaust a card when the button is pressed
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                        AbstractDungeon.actionManager.addToBottom(new RecycleAction());
                    }
                    isDone = true;
                }
            });

            usedThisTurn = true;
            grayscale = true;
            updateDescription();
        }
    }

    @Override
    public boolean isButtonDisabled() {
        return !legacyMode || grayscale;
    }


    public boolean isButtonVisible() {
        return legacyMode;
    }

    @Override
    public void onRightClick() {
        if (legacyMode && !usedThisTurn && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // Legacy mode: Exhaust a card when the relic is right-clicked
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                        AbstractDungeon.actionManager.addToBottom(new RecycleAction());
                    }
                    isDone = true;
                }
            });

            usedThisTurn = true;
            grayscale = true;
            updateDescription();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return legacyMode ? DESCRIPTIONS[0] : DESCRIPTIONS[1];
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
