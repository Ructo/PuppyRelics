package puppyrelics.relics;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

import puppyrelics.util.HoopHintMod;
import puppyrelics.util.ProAudio;

import java.util.List;

public class HoopJumper extends AbstractEasyClickRelic implements CustomSavable<int[]> {
    public static final String ID = makeID("HoopJumper");
    private static com.badlogic.gdx.graphics.Texture RELIC_TEX;
    private static com.badlogic.gdx.graphics.Texture getRelicTex() {
        if (RELIC_TEX == null) {
            RELIC_TEX = puppyrelics.util.TexLoader.getTexture(
                    puppyrelics.ModFile.makeRelicPath("HoopJumper.png")
            );
            if (RELIC_TEX != null) {
                RELIC_TEX.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear,
                        com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
            }
        }
        return RELIC_TEX;
    }
    public com.badlogic.gdx.graphics.Color colorForType(AbstractCard.CardType t) {
        // “done” means the whole requirement of that type is satisfied
        boolean done = (t == AbstractCard.CardType.ATTACK && doneAtk >= reqAtk)
                || (t == AbstractCard.CardType.SKILL  && doneSkl >= reqSkl)
                || (t == AbstractCard.CardType.POWER  && donePow >= reqPow);

        if (done)  return new com.badlogic.gdx.graphics.Color(1f, 0.85f, 0.20f, 0.85f); // gold

        switch (t) {
            case ATTACK: return new com.badlogic.gdx.graphics.Color(1f, 0.25f, 0.25f, 0.80f);
            case SKILL:  return new com.badlogic.gdx.graphics.Color(0.25f, 1f, 0.40f, 0.80f);
            case POWER:  return new com.badlogic.gdx.graphics.Color(0.35f, 0.60f, 1f, 0.80f);
            default:     return com.badlogic.gdx.graphics.Color.WHITE;
        }
    }
    private java.util.List<AbstractCard.CardType> buildRingSequence() {
        java.util.ArrayList<AbstractCard.CardType> seq = new java.util.ArrayList<>(3);
        for (int i = 0; i < reqAtk; i++) seq.add(AbstractCard.CardType.ATTACK);
        for (int i = 0; i < reqSkl; i++) seq.add(AbstractCard.CardType.SKILL);
        for (int i = 0; i < reqPow; i++) seq.add(AbstractCard.CardType.POWER);
        // seq size should be 3; if not, pad defensively
        while (seq.size() < 3) seq.add(AbstractCard.CardType.SKILL);
        return seq;
    }

    private int displayLeft = 0;

    private void renderStackedRings(SpriteBatch sb, float cx, float cy, float scale) {
        Texture tex = getRelicTex();
        if (tex == null) return;

        // spacing
        float dx = 9.0f * scale * Settings.scale;
        float dy = 1.5f * scale * Settings.scale;

        // Logical positions: 0=LEFT, 1=CENTER, 2=RIGHT  (progress order)
        float[] ox = new float[] { -dx, 0f, +dx };
        float[] oy = new float[] { +dy, 0f, -dy }; // left slightly "higher", right slightly "lower"

        // Build types in LEFT, CENTER, RIGHT order
        List<AbstractCard.CardType> seq = buildRingSequence();      // still size 3 (types A/S/P with dupes)
        // If your buildRingSequence() currently returns middle-first, swap to left->center->right mapping instead:
        // e.g., ensure seq[0] is for LEFT, seq[1] for CENTER, seq[2] for RIGHT.

        // Decide gold/need per slice using LEFT->CENTER->RIGHT counting
        com.badlogic.gdx.graphics.Color[] sliceColor = new com.badlogic.gdx.graphics.Color[3];
        int seenAtk = 0, seenSkl = 0, seenPow = 0;

        for (int pos = 0; pos < 3; pos++) { // left->center->right
            AbstractCard.CardType t = seq.get(pos);
            int seenOfType = (t == AbstractCard.CardType.ATTACK) ? seenAtk
                    : (t == AbstractCard.CardType.SKILL)  ? seenSkl
                    : (t == AbstractCard.CardType.POWER)  ? seenPow : 0;

            boolean sliceDone = seenOfType < doneForType(t); // per-slice gold if below done-count
            sliceColor[pos] = sliceDone
                    ? new com.badlogic.gdx.graphics.Color(1f, 0.85f, 0.20f, 0.85f) // gold
                    : needColorFor(t);

            if (t == AbstractCard.CardType.ATTACK)      seenAtk++;
            else if (t == AbstractCard.CardType.SKILL)  seenSkl++;
            else if (t == AbstractCard.CardType.POWER)  seenPow++;
        }

        float drawW = tex.getWidth()  * scale;
        float drawH = tex.getHeight() * scale;

        float pr = sb.getColor().r, pg = sb.getColor().g, pb = sb.getColor().b, pa = sb.getColor().a;

        // Draw order BACK->FRONT so left ends up on top: RIGHT (2), CENTER (1), LEFT (0)
        int[] drawOrder = new int[] { 2, 1, 0 };
        for (int i = 0; i < 3; i++) {
            int pos = drawOrder[i];
            sb.setColor(sliceColor[pos]);
            sb.draw(tex,
                    cx - drawW/2f + ox[pos], cy - drawH/2f + oy[pos],
                    drawW/2f, drawH/2f, drawW, drawH,
                    1f, 1f, 0f,
                    0, 0, tex.getWidth(), tex.getHeight(), false, false);
        }
        sb.setColor(pr, pg, pb, pa);

        // Number on top
        if (displayLeft > 0) {
            float numScale = 0.56f * scale;
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                    Integer.toString(displayLeft), cx, cy - 2f * scale, Color.WHITE, numScale);
        }
    }

    // helpers used above
    private int doneForType(AbstractCard.CardType t) {
        switch (t) {
            case ATTACK: return doneAtk;
            case SKILL:  return doneSkl;
            case POWER:  return donePow;
            default:     return 0;
        }
    }
    private com.badlogic.gdx.graphics.Color needColorFor(AbstractCard.CardType t) {
        switch (t) {
            case ATTACK: return new com.badlogic.gdx.graphics.Color(1f, 0.25f, 0.25f, 0.80f);
            case SKILL:  return new com.badlogic.gdx.graphics.Color(0.25f, 1f, 0.40f, 0.80f);
            case POWER:  return new com.badlogic.gdx.graphics.Color(0.35f, 0.60f, 1f, 0.80f);
            default:     return com.badlogic.gdx.graphics.Color.WHITE;
        }
    }

    private int reqAtk, reqSkl, reqPow;
    // Progress (persists across combats)
    private int doneAtk, doneSkl, donePow;
    private boolean needsRoll = true;

    public HoopJumper() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }
    @Override
    public void onEquip() {ensureRolled();}
    private void refreshHoopHintsInHand() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.hand == null) return;

        boolean needAtk = (reqAtk - doneAtk) > 0;
        boolean needSkl = (reqSkl - doneSkl) > 0;
        boolean needPow = (reqPow - donePow) > 0;

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            // clear old hints first
            CardModifierManager.removeModifiersById(c, HoopHintMod.ID, true);

            // add the proper hint if this card type still helps
            if (needAtk && c.type == AbstractCard.CardType.ATTACK) {
                CardModifierManager.addModifier(c, new HoopHintMod(AbstractCard.CardType.ATTACK));
            } else if (needSkl && c.type == AbstractCard.CardType.SKILL) {
                CardModifierManager.addModifier(c, new HoopHintMod(AbstractCard.CardType.SKILL));
            } else if (needPow && c.type == AbstractCard.CardType.POWER) {
                CardModifierManager.addModifier(c, new HoopHintMod(AbstractCard.CardType.POWER));
            }
        }
    }
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!isInCombat()) return;

        boolean progressed = false;
        switch (card.type) {
            case ATTACK: if (doneAtk < reqAtk) { doneAtk++; progressed = true; } break;
            case SKILL:  if (doneSkl < reqSkl) { doneSkl++; progressed = true; } break;
            case POWER:  if (donePow < reqPow) { donePow++; progressed = true; } break;
        }

        if (progressed) {
            updateCounterBubble();
            refreshHoopHintsInHand(); // <-- refresh immediately after any progress

            if (isComplete()) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new GainEnergyAction(1));
                needsRoll = true;
                ensureRolled();          // new set
                updateCounterBubble();
                refreshHoopHintsInHand(); // and refresh once more for the new set
            }
        }
    }
    @Override
    public void onVictory() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null) {
            AbstractDungeon.player.hand.group.forEach(
                    c -> CardModifierManager.removeModifiersById(c, HoopHintMod.ID, true)
            );
        }
        // Keep progress; no resets
    }
    @Override
    public void atTurnStart() {
        refreshHoopHintsInHand();
    }
    @Override
    public void atPreBattle() {
        updateCounterBubble();
        ensureRolled();
        refreshHoopHintsInHand();
    }
    @Override
    public void onDrawOrDiscard() {
        refreshHoopHintsInHand();
    }

    private boolean isInCombat() {
        return AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && AbstractDungeon.getMonsters() != null
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead();
    }

    private boolean isComplete() {
        return doneAtk >= reqAtk && doneSkl >= reqSkl && donePow >= reqPow;
    }

    private void updateCounterBubble() {
        displayLeft = Math.max(0, reqAtk - doneAtk)
                + Math.max(0, reqSkl - doneSkl)
                + Math.max(0, reqPow - donePow);
        this.counter = -1; // always suppress vanilla counter bubble
    }
    // --- Save/Load (BaseMod) ---
    // Order: reqAtk, reqSkl, reqPow, doneAtk, doneSkl, donePow
    @Override
    public int[] onSave() {
        return new int[] { reqAtk, reqSkl, reqPow, doneAtk, doneSkl, donePow };
    }
    @Override
    public void onLoad(int[] data) {
        if (data != null && data.length == 6) {
            reqAtk = data[0]; reqSkl = data[1]; reqPow = data[2];
            doneAtk = data[3]; doneSkl = data[4]; donePow = data[5];
            needsRoll = false; // restored from save
        } else {
            needsRoll = true;  // roll later when safe
        }
        updateCounterBubble();
        refreshHoopHintsInHand(); // already null-guarded
    }
    private void ensureRolled() {
        if (!needsRoll) return;
        java.util.Random fallback = new java.util.Random(System.nanoTime());

        reqAtk = reqSkl = reqPow = 0;
        doneAtk = doneSkl = donePow = 0;

        for (int i = 0; i < 3; i++) {
            int r = (AbstractDungeon.miscRng != null)
                    ? AbstractDungeon.miscRng.random(99)
                    : fallback.nextInt(100);
            // ~45% Attack, ~45% Skill, ~10% Power
            if (r < 45) reqAtk++;
            else if (r < 90) reqSkl++;
            else reqPow++;
        }
        needsRoll = false;
        updateCounterBubble();
        refreshHoopHintsInHand();
    }

    // --- Overlay: thirds (A/S/P) -> gold when completed, gray if not required ---
    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        super.renderInTopPanel(sb);
        renderStackedRings(sb, this.currentX, this.currentY, this.scale);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        renderStackedRings(sb, this.currentX, this.currentY, this.scale);
    }

    // --- UX bits ---
    @Override
    public void onRightClick() {
        playAudio(ProAudio.yippee);
    }

    @Override
    public String getUpdatedDescription() {
        // e.g. "Complete a random set of 3 actions (Attack/Skill/Power). When completed, gain [E] and a new set is chosen. Progress persists across combats."
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() { return new HoopJumper(); }
}
