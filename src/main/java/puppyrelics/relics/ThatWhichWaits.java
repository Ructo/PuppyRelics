package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;
import puppyrelics.util.ProAudio;

public class ThatWhichWaits extends AbstractEasyClickRelic {
    public static final String ID = makeID("ThatWhichWaits");

    private boolean playedAttackThisTurn = false;

    public ThatWhichWaits() {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        playedAttackThisTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            playedAttackThisTurn = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (!playedAttackThisTurn
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && AbstractDungeon.getMonsters() != null
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {

            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DamageRandomEnemyAction(
                    new DamageInfo(AbstractDungeon.player, 7, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }

    @Override
    public void onVictory() {
        playedAttackThisTurn = false;
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // "If you end your turn without playing an Attack, deal 7 damage to a random enemy."
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ThatWhichWaits();
    }
}
