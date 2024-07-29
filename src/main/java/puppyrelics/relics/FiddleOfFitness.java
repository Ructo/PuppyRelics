package puppyrelics.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;

import static puppyrelics.ModFile.makeID;

public class FiddleOfFitness extends AbstractEasyRelic {
    public static final String ID = makeID("FiddleOfFitness");
    private boolean firstTurn = true;

    public FiddleOfFitness() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            if (this.counter == -2) {
                this.pulse = false;
                this.counter = -1;
                this.flash();
                AbstractPlayer p = AbstractDungeon.player;
                AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(p, p, 12));
            }

            this.firstTurn = false;
        }
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            flash();
            this.counter = -2;
            this.pulse = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FiddleOfFitness();
    }
}
