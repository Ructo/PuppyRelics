package puppyrelics.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractUniqueClickRelic extends CustomRelic {
    public AbstractCard.CardColor color;

    public AbstractUniqueClickRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(setId, (String) null, tier, sfx);
    }

    public AbstractUniqueClickRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color) {
        super(setId, (String) null, tier, sfx);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
        if (CardCrawlGame.isInARun()) {
            clickUpdate();
        }
    }

    private void clickUpdate() {
        if (!AbstractDungeon.isScreenUp && HitboxRightClick.rightClicked.get(this.hb)) {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                onRightClick();
            }
        }
    }

    public abstract void onRightClick();

    public abstract void loadImages(int stage);

    @Override
    public abstract String getUpdatedDescription();
}
