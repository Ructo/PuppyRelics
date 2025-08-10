package puppyrelics.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch2(
        clz = com.megacrit.cardcrawl.ui.panels.PotionPopUp.class,
        method = "updateInput"
)
public class PotionDiscardPatch {

    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals("com.megacrit.cardcrawl.audio.SoundMaster")
                        && m.getMethodName().equals("play")) {
                    m.replace(
                            "{ " +
                                    "  $_ = $proceed($$); " + // play the sound as normal
                                    "  if (\"POTION_DROP_2\".equals($1)) { " + // only the discard branch
                                    "    if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null " +
                                    "        && com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasRelic(puppyrelics.relics.IcingOnThePotion.ID)) { " +
                                    "      com.megacrit.cardcrawl.potions.AbstractPotion __p = " +
                                    "          (com.megacrit.cardcrawl.potions.AbstractPotion) " +
                                    "          basemod.ReflectionHacks.getPrivate(this, " +
                                    "              com.megacrit.cardcrawl.ui.panels.PotionPopUp.class, \"potion\"); " +
                                    "      if (__p != null) { " +
                                    "        ((puppyrelics.relics.IcingOnThePotion) " +
                                    "            com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.getRelic(puppyrelics.relics.IcingOnThePotion.ID))" +
                                    "            .betterOnUsePotion(__p);" +
                                    "      } " +
                                    "    } " +
                                    "  } " +
                                    "}"
                    );
                }
            }
        };
    }
}
