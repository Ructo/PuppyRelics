package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class NinjaPuppysMask extends AbstractEasyClickRelic {
    public static final String ID = makeID("NinjaPuppysMask");
    private int attackCounter = 0;

    public NinjaPuppysMask() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        attackCounter = 0; // Reset the counter at the start of each turn
        setCounter(0); // Reset the displayed counter as well
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            attackCounter++;
            setCounter(attackCounter % 3);
            if (attackCounter % 3 == 0) {
                flash();
                AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (m != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new ConstrictedPower(m, AbstractDungeon.player, 1), 1));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new ConstrictedPower(m, AbstractDungeon.player, 1), 1));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new ConstrictedPower(m, AbstractDungeon.player, 1), 1));
                }
            }
        }
    }
    @Override
    public void onRightClick() {
        playAudio(ProAudio.ninja);
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
