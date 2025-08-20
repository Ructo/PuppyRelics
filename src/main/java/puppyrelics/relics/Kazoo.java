package puppyrelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puppyrelics.ModFile;
import puppyrelics.util.ProAudio;

import java.util.ArrayList;
import java.util.Arrays;

import static puppyrelics.ModFile.SoundMode.*;   // SPECIFIC, RANDOM_CLICK, RANDOM_ROOM, RANDOM_ACT, RANDOM_RUN
import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class Kazoo extends AbstractEasyClickRelic {
    public static final String ID = makeID("Kazoo");

    private ProAudio current;
    private final ArrayList<ProAudio> pool = new ArrayList<>();
    private int cachedAct = -1; // detect NEW ACT transitions

    public Kazoo() {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
        pool.addAll(Arrays.asList(ProAudio.values()));
    }

    @Override
    public boolean canSpawn() { return false; } // config-only

    @Override
    public void onEquip() {
        switch (ModFile.soundRelicMode) {
            case SPECIFIC:
                current = parse(ModFile.soundRelicPick);
                break;
            case RANDOM_CLICK:
            case RANDOM_ROOM:
            case RANDOM_ACT:
            case RANDOM_RUN:
                rollRandomIfNull();
                break;
        }
        cachedAct = safeActNum();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        // Random per room
        if (ModFile.soundRelicMode == RANDOM_ROOM) {
            rollRandom();
        }
        // Random per act (reroll when act number changes)
        if (ModFile.soundRelicMode == RANDOM_ACT) {
            int actNow = safeActNum();
            if (actNow != cachedAct) {
                rollRandom();
                cachedAct = actNow;
            }
        }
    }

    @Override
    public void onRightClick() {
        switch (ModFile.soundRelicMode) {
            case SPECIFIC:
                current = parse(ModFile.soundRelicPick);
                break;
            case RANDOM_CLICK:
                rollRandom();
                break;
            case RANDOM_ROOM:
            case RANDOM_ACT:
            case RANDOM_RUN:
                // keep current selection
                break;
        }
        if (current != null) {
            flash();
            playAudio(current);
        }
    }

    // --- helpers ---

    public void rollRandom() {
        if (pool.isEmpty()) { current = null; return; }
        int idx = AbstractDungeon.miscRng.random(pool.size() - 1);
        current = pool.get(idx);
    }

    private void rollRandomIfNull() { if (current == null) rollRandom(); }

    private ProAudio parse(String name) {
        try { return ProAudio.valueOf(name); } catch (Exception e) { return null; }
    }

    private int safeActNum() {
        return (AbstractDungeon.player != null) ? AbstractDungeon.actNum : -1;
    }

    @Override public String getUpdatedDescription() { return DESCRIPTIONS[0]; }
    @Override public AbstractRelic makeCopy() { return new Kazoo(); }
}
