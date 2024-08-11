package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class KnuckleDown extends AbstractEasyClickRelic {
    public static final String ID = makeID("KnuckleDown");

    private int skillCount = 0;

    public KnuckleDown() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            skillCount++;
            if (skillCount == 3) {
                removeRandomDebuff();
                skillCount = 0;
            }
        }
    }

    @Override
    public void atTurnStart() {
        skillCount = 0;
    }

    private void removeRandomDebuff() {
        if (AbstractDungeon.player.powers.isEmpty()) {
            return;
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
                return;
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KnuckleDown();
    }
    @Override
    public void onRightClick() {
        playAudio(ProAudio.heavyblunt);
    }
}
