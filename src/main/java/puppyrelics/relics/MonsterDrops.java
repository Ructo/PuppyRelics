package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import static puppyrelics.ModFile.makeID;

public class MonsterDrops extends AbstractEasyRelic {
    public static final String ID = makeID("MonsterDrops");

    public MonsterDrops() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 2));
            AbstractDungeon.player.gainGold(5);
        }
    }
}
