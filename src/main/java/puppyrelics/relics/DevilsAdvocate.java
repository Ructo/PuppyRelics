package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class DevilsAdvocate extends AbstractEasyRelic {
    public static final String ID = makeID("DevilsAdvocate");

    private boolean readyToTrigger = false; // Flag to indicate if the relic is ready to grant temporary HP

    public DevilsAdvocate() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        readyToTrigger = false; // Reset at the start of each battle
    }

    @Override
    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            readyToTrigger = true; // Set the flag to indicate the relic is ready to trigger on the next attack
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (readyToTrigger && damageAmount > 0 && info.owner == AbstractDungeon.player) {
            int unblockedDamage = Math.max(0, damageAmount - target.currentBlock); // Calculate unblocked damage
            if (unblockedDamage > 0) {
                int tempHPAmount = (int) (unblockedDamage * 0.5);
                flash();
                AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        tempHPAmount
                ));
                readyToTrigger = false; // Reset the flag after granting temporary HP
            }
        }
    }

    @Override
    public void onVictory() {
        readyToTrigger = false; // Reset after combat ends
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DevilsAdvocate();
    }
}
