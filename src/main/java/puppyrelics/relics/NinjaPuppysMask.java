package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

import static puppyrelics.ModFile.makeID;

public class NinjaPuppysMask extends AbstractEasyRelic {
    public static final String ID = makeID("NinjaPuppysMask");
    private int attackCounter = 0;

    public NinjaPuppysMask() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        attackCounter = 0; // Reset the counter at the start of each turn
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            attackCounter++;
            if (attackCounter % 3 == 0) {
                flash();
                AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (m != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new ConstrictedPower(m, AbstractDungeon.player, 2), 2));
                }
            }
        }
    }
}
