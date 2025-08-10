package puppyrelics.relics;

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
    private boolean suppress = false;

    public BabyAndBathwater() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onExhaust(AbstractCard exhausted) {
        if (suppress) return;
        if (!isInCombat()) return;

        // Find a random curse/status from hand, draw, or discard
        AbstractCard target = findRandomCurseOrStatus();
        if (target == null) return;

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        // Exhaust it without re-triggering this relic
        suppress = true;
        CardGroup owner = ownerOf(target);
        if (owner != null) {
            owner.moveToExhaustPile(target);
        }
        suppress = false;
    }

    private boolean isInCombat() {
        return AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && AbstractDungeon.getMonsters() != null
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead();
    }

    private AbstractCard findRandomCurseOrStatus() {
        ArrayList<AbstractCard> pool = new ArrayList<>();

        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) pool.add(c);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) pool.add(c);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) pool.add(c);

        if (pool.isEmpty()) return null;
        int idx = AbstractDungeon.cardRandomRng.random(pool.size() - 1);
        return pool.get(idx);
    }

    private CardGroup ownerOf(AbstractCard c) {
        if (AbstractDungeon.player.hand.group.contains(c)) return AbstractDungeon.player.hand;
        if (AbstractDungeon.player.drawPile.group.contains(c)) return AbstractDungeon.player.drawPile;
        if (AbstractDungeon.player.discardPile.group.contains(c)) return AbstractDungeon.player.discardPile;
        return null;
    }

    @Override
    public void onVictory() { suppress = false; }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BabyAndBathwater();
    }
}
