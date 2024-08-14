package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class UnturnedStone extends AbstractEasyClickRelic {
    public static final String ID = makeID("UnturnedStone");

    private boolean drawPileTriggered = false;

    public UnturnedStone() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        drawPileTriggered = false;
    }

    @Override
    public void atTurnStart() {
        drawPileTriggered = false;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        checkHandEmpty();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        checkHandEmpty();
    }

    @Override
    public void onManualDiscard() {
        checkHandEmpty();
    }

    private void checkHandEmpty() {
        if (AbstractDungeon.player.hand.isEmpty()) {
            triggerEffect();
        }
    }

    @Override
    public void update() {
        super.update();


        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.actionManager.turnHasEnded) {


            if (!drawPileTriggered && AbstractDungeon.player.drawPile.isEmpty()) {
                triggerEffect();
                drawPileTriggered = true;
            }
        }
    }


    private void triggerEffect() {
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (target != null) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(AbstractDungeon.player, 5, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UnturnedStone();
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }
}
