package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class MonsterDrops extends AbstractEasyClickRelic {
    public static final String ID = makeID("MonsterDrops");
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 200;
    public MonsterDrops() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom && !m.hasPower("Minion")) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 2));
            AbstractDungeon.player.gainGold(5);
        }
    }

    @Override
    public void onRightClick() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime >= COOLDOWN_MS) {
                playAudio(ProAudio.fairy);
                lastClickTime = currentTime;
            }
        }
    }

