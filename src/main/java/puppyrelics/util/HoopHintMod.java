package puppyrelics.util;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import puppyrelics.util.TexLoader;


import static puppyrelics.ModFile.makeID;

public class HoopHintMod extends AbstractCardModifier {
    public static final String ID = makeID("HoopHintMod");

    private static Texture hoopTex;
    private static Texture getHoopTex() {
        if (hoopTex == null) {
            hoopTex = TexLoader.getTexture(
                    puppyrelics.ModFile.makeRelicPath("HoopJumper.png")
            );
            if (hoopTex != null) hoopTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        return hoopTex;
    }

    private final AbstractCard.CardType type;

    public HoopHintMod(AbstractCard.CardType type) {
        this.type = type;
    }
    private static Color tintForType(AbstractCard.CardType t) {
        // same palette you used for the relic overlays
        switch (t) {
            case ATTACK: return new Color(1f, 0.25f, 0.25f, 0.75f);
            case SKILL:  return new Color(0.25f, 1f, 0.40f, 0.75f);
            case POWER:  return new Color(0.35f, 0.60f, 1f, 0.75f);
            default:     return Color.WHITE;
        }
    }
    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture tex = getHoopTex();
        if (tex == null) return;

        // Pick tint first (your existing switch)
        sb.setColor(tintForType(type));  // your method

        // Scale a little smaller than the card art, then push upward
        float s  = card.drawScale * Settings.scale * 0.82f; // smaller
        float w  = tex.getWidth()  * s;
        float h  = tex.getHeight() * s;
        float oy = 210f * card.drawScale * Settings.scale;

        sb.draw(tex,
                card.current_x - w/2f,
                card.current_y - h/2 + oy,
                w/2f, h/2f,
                w, h,
                1f, 1f,
                0f,
                0, 0, tex.getWidth(), tex.getHeight(),
                false, false);
        sb.setColor(Color.WHITE);
    }


    @Override
    public AbstractCardModifier makeCopy() {
        return new HoopHintMod(type);
    }
}
