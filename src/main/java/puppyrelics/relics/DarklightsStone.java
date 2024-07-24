package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.helpers.PowerTip;

import static puppyrelics.ModFile.makeID;

public class DarklightsStone extends AbstractEasyRelic {
    public static final String ID = makeID("DarklightsStone");
    private static final int UPGRADE_THRESHOLD = 3;

    public DarklightsStone() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }


    public void onUpgradeCard(AbstractCard card) {
        int upgradeCount = getUpgradeCount();
        int stage = upgradeCount / UPGRADE_THRESHOLD;
        evolveRelic(stage);
        updateDescription(upgradeCount);
    }

    private int getUpgradeCount() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return 0;
        }
        return (int) AbstractDungeon.player.masterDeck.group.stream().filter(card -> card.upgraded).count();
    }

    private void evolveRelic(int stage) {
        AbstractPlayer player = AbstractDungeon.player;
        if (stage >= 1) {
            // Stage 1: Draw 1 additional card at the start of each combat
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, 1));
        }
        if (stage >= 2) {
            // Stage 2: Gain 1 Strength
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, 1), 1));
        }
        if (stage >= 3) {
            // Stage 3: Gain 1 Dexterity
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player, 1), 1));
        }
        if (stage >= 4) {
            // Stage 4: Gain 2 Energy at the start of each combat
            player.gainEnergy(2);
        }
        flash();
    }

    @Override
    public void atBattleStart() {
        int upgradeCount = getUpgradeCount();
        int stage = upgradeCount / UPGRADE_THRESHOLD;
        evolveRelic(stage);
        updateDescription(upgradeCount);
    }

    private void updateDescription(int upgradeCount) {
        int stage = upgradeCount / UPGRADE_THRESHOLD;
        if (stage < 1) {
            this.description = DESCRIPTIONS[0] + (UPGRADE_THRESHOLD - upgradeCount) + DESCRIPTIONS[5];
        } else if (stage == 1) {
            this.description = DESCRIPTIONS[1] + (UPGRADE_THRESHOLD * 2 - upgradeCount) + DESCRIPTIONS[5];
        } else if (stage == 2) {
            this.description = DESCRIPTIONS[2] + (UPGRADE_THRESHOLD * 3 - upgradeCount) + DESCRIPTIONS[5];
        } else if (stage == 3) {
            this.description = DESCRIPTIONS[3] + (UPGRADE_THRESHOLD * 4 - upgradeCount) + DESCRIPTIONS[5];
        } else {
            this.description = DESCRIPTIONS[4];
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onEquip() {
        int upgradeCount = getUpgradeCount();
        updateDescription(upgradeCount);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DarklightsStone();
    }
}
