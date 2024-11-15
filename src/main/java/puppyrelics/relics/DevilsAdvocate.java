package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class DevilsAdvocate extends AbstractEasyClickRelic {
    public static final String ID = makeID("DevilsAdvocate");

    private boolean readyToTrigger = false; // Flag to indicate if the relic is ready to grant temporary HP
    private boolean isAttackCard = false;
    private boolean alreadyFlashed = false;

    public DevilsAdvocate() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {

        readyToTrigger = false; // Reset at the start of each battle
        stopPulse();
    }

    @Override
    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            readyToTrigger = true; // Set the flag to indicate the relic is ready to trigger on the next attack
            beginLongPulse();
            if (!alreadyFlashed) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new ShockWaveEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));
                alreadyFlashed = true;
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        isAttackCard = card.type == AbstractCard.CardType.ATTACK;
        }


    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (readyToTrigger && damageAmount > 0 && info.owner == AbstractDungeon.player && isAttackCard) {
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
                alreadyFlashed = false;
                stopPulse();
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
    @Override
    public void onRightClick() {
        playAudio(ProAudio.devil);
    }
}
