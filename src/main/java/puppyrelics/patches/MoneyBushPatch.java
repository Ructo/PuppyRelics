package puppyrelics.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppyrelics.relics.MoneyBush;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class MoneyBushPatch {

    @SpireInsertPatch(rloc = 0)
    public static void Insert(AbstractPlayer __instance, int goldAmount) {
        // Check if the player has the MoneyBush relic
        if (AbstractDungeon.player.hasRelic(MoneyBush.ID)) {
            MoneyBush moneyBush = (MoneyBush) AbstractDungeon.player.getRelic(MoneyBush.ID);
            moneyBush.onLoseGoldCustom(goldAmount); // Call the custom method to refund 30% of the lost gold
        }
    }
}
