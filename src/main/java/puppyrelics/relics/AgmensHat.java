package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.GreedAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class AgmensHat extends AbstractEasyClickRelic {
    public static final String ID = makeID("AgmensHat");
    private static final int DAMAGE = 3;
    private static final int GOLD_REWARD = 10;

    public AgmensHat() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                // Apply damage and grant gold if the monster is killed
                addToBot(new GreedAction(m, new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS), GOLD_REWARD));
            }
        }
    }

    @Override
    public void onVictory() {
        // No need to manually clear or track killed monsters; handled by GreedAction
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
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
