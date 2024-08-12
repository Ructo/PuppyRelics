package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnSmithRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import puppyrelics.util.ProAudio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class BeesKnees extends AbstractEasyClickRelic implements BetterOnSmithRelic {
    public static final String ID = makeID("BeesKnees");

    public BeesKnees() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    public void betterOnSmith(AbstractCard abstractCard) {
        flash();
        upgradeSelectedCard(abstractCard);
        upgradeRandomCard(abstractCard);
    }

    private void upgradeSelectedCard(AbstractCard cardToUpgrade) {
        cardToUpgrade.upgrade();
        AbstractDungeon.player.bottledCardUpgradeCheck(cardToUpgrade);
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy()));
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(cardToUpgrade.current_x, cardToUpgrade.current_y));
    }

    private void upgradeRandomCard(AbstractCard selectedCard) {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.canUpgrade() && card != selectedCard) {
                upgradableCards.add(card);
            }
        }

        if (!upgradableCards.isEmpty()) {
            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
            AbstractCard randomCardToUpgrade = upgradableCards.get(0);
            randomCardToUpgrade.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(randomCardToUpgrade);

            float xOffset = AbstractCard.IMG_WIDTH + 300.0F * Settings.scale; // Adjust the offset for better spacing
            float yOffset = 0.0F; // Slight adjustment to y position
            float xPos = selectedCard.current_x + xOffset;
            float yPos = selectedCard.current_y + yOffset;

            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(randomCardToUpgrade.makeStatEquivalentCopy(), xPos, yPos));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(xPos, yPos));
        }
    }

    @Override
    public void onRightClick() {
        playAudio(ProAudio.yippee);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BeesKnees();
    }
}
