package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomSavable;
import puppyrelics.util.ProAudio;
import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class CatsPajamas extends AbstractEasyClickRelic {
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
    public void onRightClick() {
        playRandomSound();
    }

    private void playRandomSound() {
        double chance = Math.random(); // Generates a random number between 0.0 and 1.0

        if (chance < 0.05) { // 5% chance
            playAudio(ProAudio.meowman); // Play the rare sound
        } else {
            // 95% chance, split between the other three sounds
            int pick = (int) (Math.random() * 3); // Generates a random integer 0, 1, or 2
            switch (pick) {
                case 0:
                    playAudio(ProAudio.cat1); // Replace with actual sound key
                    break;
                case 1:
                    playAudio(ProAudio.cat2); // Replace with actual sound key
                    break;
                case 2:
                    playAudio(ProAudio.cat3); // Replace with actual sound key
                    break;
                default:
                    break;
            }
        }

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
