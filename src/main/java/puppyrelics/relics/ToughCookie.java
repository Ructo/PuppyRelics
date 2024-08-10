package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import puppyrelics.relics.AbstractEasyRelic;

import java.util.Iterator;

import static puppyrelics.ModFile.makeID;

public class ToughCookie extends AbstractEasyRelic {
    public static final String ID = makeID("ToughCookie");
    private static final int HEAL_AMT = 15;

    public ToughCookie() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.type == EnemyType.ELITE) {
                this.flash();
                this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_AMT, 0.0F));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                break; // Trigger only once per battle, no need to continue checking
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ToughCookie();
    }
}
