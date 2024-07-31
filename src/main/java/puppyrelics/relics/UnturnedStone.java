package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static puppyrelics.ModFile.makeID;

public class UnturnedStone extends AbstractEasyRelic {
    public static final String ID = makeID("UnturnedStone");

    private boolean handTriggered = false;
    private boolean drawPileTriggered = false;
    private boolean hasDrawnInitialHand = false;

    public UnturnedStone() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        handTriggered = false;
        drawPileTriggered = false;
        hasDrawnInitialHand = false;
    }

    public void onPlayerDraw() {
        hasDrawnInitialHand = true;
    }

    @Override
    public void update() {
        super.update();
        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.actionManager.turnHasEnded) {
            if (hasDrawnInitialHand && !handTriggered && AbstractDungeon.player.hand.isEmpty()) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, 7, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                handTriggered = true; // Set the flag to true after triggering the effect
            }
            if (!drawPileTriggered && AbstractDungeon.player.drawPile.isEmpty()) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, 7, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                drawPileTriggered = true; // Set the flag to true after triggering the effect
            }
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
}
