package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static puppyrelics.ModFile.makeID;

public class BubbleBurster extends AbstractEasyRelic {
    public static final String ID = makeID("BubbleBurster");

    public BubbleBurster() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        flash();
        int strengthAmount = 1;
        AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
        if (currRoom instanceof MonsterRoomBoss) {
            strengthAmount++;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, strengthAmount), strengthAmount));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BubbleBurster();
    }
}
