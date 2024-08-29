package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import puppyrelics.relics.AbstractEasyRelic;
import puppyrelics.util.ProAudio;

import java.util.Iterator;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class ToughCookie extends AbstractEasyClickRelic {
    public static final String ID = makeID("ToughCookie");
    private static final int HEAL_AMT = 15;

    public ToughCookie() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 10000;

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom().event != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m.type == EnemyType.ELITE) {
                    this.flash();
                    this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_AMT, 0.0F));
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    break; // Trigger only once per battle, no need to continue checking
                }
            }
        }
    }


    @Override
    public AbstractRelic makeCopy() {
        return new ToughCookie();
    }
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 40;
    }
    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.tough);
            lastClickTime = currentTime;
        }
    }
}

