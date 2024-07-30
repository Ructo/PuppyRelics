package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static puppyrelics.ModFile.makeID;

public class DevilsAdvocate extends AbstractEasyRelic {
    public static final String ID = makeID("DevilsAdvocate");

    private int damageTaken = 0;

    public DevilsAdvocate() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        damageTaken = 0; // Reset at the start of each battle
    }

    @Override
    public void onLoseHp(int damageAmount) {
        damageTaken += damageAmount; // Accumulate the damage taken
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageTaken > 0 && damageAmount > 0 && info.owner == AbstractDungeon.player) {
            int healAmount = (int) (damageAmount * 0.25);
            flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmount));
            damageTaken = 0; // Reset the damage taken after applying the healing
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DevilsAdvocate();
    }
}
