package puppyrelics.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.evacipated.cardcrawl.modthespire.lib.*;

// Target the render(SpriteBatch) overload explicitly
@SpirePatch2(
        clz = AbstractCard.class,
        method = "render",
        paramtypez = { SpriteBatch.class }
)
public class HoopHintRenderPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractCard __instance, SpriteBatch sb) {
        // Only during combat, and only for cards actually in hand
        if (AbstractDungeon.player == null) return;
        if (AbstractDungeon.getCurrRoom() == null) return;
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return;
        if (AbstractDungeon.player.hand == null) return;
        if (!AbstractDungeon.player.hand.contains(__instance)) return;

        // Draw your overlay here (example placeholder)
        // sb.setColor(Color.WHITE);
        // sb.draw(yourTexture, __instance.current_x - 64f, __instance.current_y - 64f,
        //         64f, 64f, 128f, 128f, __instance.drawScale, __instance.drawScale,
        //         __instance.angle, 0, 0, 128, 128, false, false);
    }
}
