package puppyrelics.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;

public class OnReceivePowerPatch {
    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPower {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"duration", "powerToApply"}
        )
        public static SpireReturn<Void> Insert(ApplyPowerAction __instance, @ByRef float[] duration, AbstractPower powerToApply) {
            AbstractCreature target = __instance.target;
            if (target != null && target.isPlayer) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof OnReceivePowerRelic) {
                        ((OnReceivePowerRelic) relic).onReceivePower(powerToApply, target);
                    }
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
