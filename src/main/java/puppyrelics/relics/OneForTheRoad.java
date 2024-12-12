package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class OneForTheRoad extends AbstractEasyClickRelic {
    public static final String ID = makeID("OneForTheRoad");

    public OneForTheRoad() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.type == AbstractCard.CardType.POWER) {
            flash();
            addToBot(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OneForTheRoad();
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.welp); // Replace with the appropriate sound effect for OneForTheRoad
    }
}
