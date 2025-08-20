package puppyrelics.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.evacipated.cardcrawl.modthespire.lib.*;


@SpirePatch(clz = AbstractCard.class, method = "render",
        paramtypez = { SpriteBatch.class })
public class HoopHintRenderPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractCard __instance, SpriteBatch sb) {
        if (!shouldRenderHint()) return;
        AbstractRoom room = AbstractDungeon.currMapNode.room;
    }
    private static boolean shouldRenderHint() {
        // Not in a run? (e.g., main menu / character select / compendium / pack preview)
        if (!CardCrawlGame.isInARun()) return false;

        // No map node yet? Bail.
        if (AbstractDungeon.currMapNode == null) return false;

        // No room yet? Bail.
        AbstractRoom room = AbstractDungeon.currMapNode.room;
        if (room == null) return false;

        // Only show during combat
        if (room.phase != AbstractRoom.RoomPhase.COMBAT) return false;

        return true;
    }
}
