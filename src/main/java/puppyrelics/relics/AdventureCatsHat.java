package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class AdventureCatsHat extends AbstractEasyClickRelic {
    public static final String ID = makeID("AdventureCatsHat");
    private int blockAmount = 0;
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 200;
    public AdventureCatsHat() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        blockAmount = 0;
        this.setCounter(blockAmount);
    }

    @Override
    public void atTurnStart() {
        blockAmount = 0; // Reset the block amount at the start of each turn
        this.setCounter(blockAmount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            blockAmount++;
            flash();
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, blockAmount));
            this.setCounter(blockAmount); // Update the counter
        }
    }

    @Override
    public void onVictory() {
        this.setCounter(-1); // Hide the counter when the battle is over
    }

    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.fairy);
            lastClickTime = currentTime;
        }
    }
}
