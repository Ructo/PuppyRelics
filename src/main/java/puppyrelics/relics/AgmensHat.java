package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
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
        addToBot(new DamageAllEnemiesAction(
                AbstractDungeon.player,
                DamageInfo.createDamageMatrix(DAMAGE, true),
                DamageInfo.DamageType.THORNS,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
            flash();
            AbstractDungeon.player.gainGold(GOLD_REWARD);
        }
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