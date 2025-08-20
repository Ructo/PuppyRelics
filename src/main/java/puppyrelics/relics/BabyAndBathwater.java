package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;
import puppyrelics.util.ProAudio;

import java.util.ArrayList;

public class BabyAndBathwater extends AbstractEasyClickRelic {
    public static final String ID = makeID("BabyAndBathwater");

    // Prevents feedback loop when this relic exhausts a card itself
    private int exhaustsThisTurn = 0;   // cap: 1 per turn
    private boolean suppress = false;   // prevent recursion when we exhaust a Status


    public BabyAndBathwater() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    @Override
    public void onExhaust(AbstractCard card) {
        // Do nothing if we're in our own exhaust chain or we've already fired this turn
        if (suppress || exhaustsThisTurn >= 1) return;

        // Trigger only when a NON-curse and NON-status card is exhausted
        if (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) return;

        // Find a random STATUS card from hand, draw, or discard
        Target pick = pickRandomStatusFromAnyPile();
        if (pick == null) return;

        // Flash/above-creature (keep your existing visuals/audio if you have them)
        this.flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        // Exhaust the chosen Status without re-triggering the relic
        suppress = true;
        addToTop(new ExhaustSpecificCardAction(pick.card, pick.group));
        suppress = false;

        exhaustsThisTurn++;
        this.grayscale = true;
    }
    private static class Target {
        final AbstractCard card;
        final CardGroup group;
        Target(AbstractCard c, CardGroup g) { this.card = c; this.group = g; }
    }

    private Target pickRandomStatusFromAnyPile() {
        CardGroup hand = AbstractDungeon.player.hand;
        CardGroup draw = AbstractDungeon.player.drawPile;
        CardGroup discard = AbstractDungeon.player.discardPile;

        ArrayList<Target> candidates = new ArrayList<>();

        for (AbstractCard c : hand.group)    if (c.type == AbstractCard.CardType.STATUS) candidates.add(new Target(c, hand));
        for (AbstractCard c : draw.group)    if (c.type == AbstractCard.CardType.STATUS) candidates.add(new Target(c, draw));
        for (AbstractCard c : discard.group) if (c.type == AbstractCard.CardType.STATUS) candidates.add(new Target(c, discard));

        if (candidates.isEmpty()) return null;
        int idx = AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
        return candidates.get(idx);
    }

    @Override
    public void atTurnStart() {
        exhaustsThisTurn = 0;
        this.grayscale = false;
    }
    @Override
    public void onVictory() {
        exhaustsThisTurn = 0;
        suppress = false;
        this.grayscale = false;
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.splash);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BabyAndBathwater();
    }
}
