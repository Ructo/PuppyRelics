package puppyrelics.relics;

import basemod.helpers.CardPowerTip;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.cards.PirateRaidCard;
import puppyrelics.cards.RatRaceCard;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class AgmensHat extends AbstractEasyClickRelic {
    public static final String ID = makeID("AgmensHat");

    private boolean gaveRaidThisCombat = false;

    public AgmensHat() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(new PirateRaidCard()));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        gaveRaidThisCombat = false;
    }

    @Override
    public void onShuffle() {
        if (isInCombat() && !gaveRaidThisCombat) {
            flash();
            PirateRaidCard raid = new PirateRaidCard();
            // Prefer hand; if full, send to discard
            if (AbstractDungeon.player.hand.size() < 10) {
                addToBot(new MakeTempCardInHandAction(raid, 1, false));
            } else {
                addToBot(new MakeTempCardInDiscardAction(raid, 1));
            }
            gaveRaidThisCombat = true;
        }
    }

    @Override
    public void onVictory() {
        gaveRaidThisCombat = false;
    }

    private boolean isInCombat() {
        return AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && AbstractDungeon.getMonsters() != null
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AgmensHat();
    }

@Override
    public void onRightClick() {
        playAudio(ProAudio.pirate);
    }
}
