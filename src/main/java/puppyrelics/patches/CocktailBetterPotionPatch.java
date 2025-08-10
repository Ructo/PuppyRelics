package puppyrelics.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import puppyrelics.relics.IcingOnThePotion;

@SpirePatch2(
        cls = "AKDsMoreRelics.relics.Cocktail",
        method = "atTurnStartPostDraw",
        optional = true // skip if Cocktail mod isn't present
)
public class CocktailBetterPotionPatch {

    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                // Find the call to AbstractPotion.use(...)
                if (m.getClassName().equals("com.megacrit.cardcrawl.potions.AbstractPotion")
                        && m.getMethodName().equals("use")) {
                    // Replace with: call original, then notify our relic with the same 'po' (the call target)
                    // In MethodCall, $0 refers to the TARGET object of the call (i.e., the AbstractPotion instance).
                    m.replace(
                            "{ " +
                                    "  $_ = $proceed($$);" + // call original use(...)
                                    "  if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null " +
                                    "      && com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(puppyrelics.relics.IcingOnThePotion.ID)) {" +
                                    "    ((puppyrelics.relics.IcingOnThePotion) " +
                                    "        com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.getRelic(puppyrelics.relics.IcingOnThePotion.ID))" +
                                    "        .betterOnUsePotion((com.megacrit.cardcrawl.potions.AbstractPotion)$0);" +
                                    "  }" +
                                    "}"
                    );
                }
            }
        };
    }
}
