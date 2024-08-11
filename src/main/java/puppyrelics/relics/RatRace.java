package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.cards.RatRaceCard;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class RatRace extends AbstractEasyClickRelic {
    public static final String ID = makeID("RatRace");
    private boolean hasStatusCardInHand = false;

    public RatRace() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(new RatRaceCard()));
        initializeTips();
    }

    @Override
    public void atTurnStartPostDraw() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player.hand.group.stream().noneMatch(c -> c.type == AbstractCard.CardType.STATUS)) {
                    addToTop(new MakeTempCardInHandAction(new RatRaceCard()));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RatRace();
    }
    @Override
    public void onRightClick() {
        playAudio(ProAudio.squeak);
    }
}
