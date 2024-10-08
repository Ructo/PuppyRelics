package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.StrengthPower;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class BubbleBurster extends AbstractEasyClickRelic {
    public static final String ID = makeID("BubbleBurster");

    public BubbleBurster() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 3000;
    @Override
    public void atBattleStart() {
        flash();
        int powerAmount = 1;

        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss ||
                (AbstractDungeon.getCurrRoom().event != null && isBossInEvent())) {
            // Double the power amount for boss rooms or event rooms with bosses
            powerAmount++;
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, powerAmount), powerAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, powerAmount), powerAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, powerAmount), powerAmount));
    }

    private boolean isBossInEvent() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BubbleBurster();
    }
    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.bubble);
            lastClickTime = currentTime;
        }
    }
}
