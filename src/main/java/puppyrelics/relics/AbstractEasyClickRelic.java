package puppyrelics.relics;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractEasyClickRelic extends AbstractEasyRelic {

    public AbstractEasyClickRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(setId, tier, sfx);
    }

    public AbstractEasyClickRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color) {
        super(setId, tier, sfx, color);

    }

    @Override
    public void update() {
        super.update();
        if (CardCrawlGame.isInARun()) {
            clickUpdate();
        }
    }

    private void clickUpdate() {
        if (HitboxRightClick.rightClicked.get(this.hb)) {
                onRightClick();
        }
    }

    public abstract void onRightClick();

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
