package puppyrelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnSmithRelic;
import basemod.abstracts.CustomSavable;
import puppyrelics.util.ProAudio;

import static puppyrelics.ModFile.makeID;
import static puppyrelics.util.Wiz.playAudio;

public class DarklightsStone extends AbstractUniqueClickRelic implements BetterOnSmithRelic, CustomSavable<Integer> {
    public static final String ID = makeID("DarklightsStone");
    private static final String IMG_PATH = "puppyrelicsResources/images/relics/";
    private static final int MAX_STAGE = 4;
    private int currentStage = 0;
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 200;

    public DarklightsStone() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
        loadImages(0); // Initialize the default image
    }

    @Override
    public void betterOnSmith(AbstractCard card) {
        if (currentStage < MAX_STAGE) {
            currentStage++;
            evolveRelic(currentStage);
            updateDescription(currentStage);
            loadImages(currentStage);
        }
    }

    private void evolveRelic(int stage) {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (stage >= 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, 1));
        }
        if (stage >= 1) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, 1), 1));
        }
        if (stage >= 2) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player, 1), 1));
        }
        if (stage >= 3) {
            player.gainEnergy(2);
        }
        flash();
    }

    @Override
    public void atBattleStart() {
        evolveRelic(currentStage);
    }

    private void updateDescription(int upgradeCount) {
        int stage = upgradeCount + 1;

        switch (stage) {
            case 1:
                this.description = DESCRIPTIONS[1];
                break;
            case 2:
                this.description = DESCRIPTIONS[2];
                break;
            case 3:
                this.description = DESCRIPTIONS[3];
                break;
            case 4:
            case 5:
                this.description = DESCRIPTIONS[4];
                break;
            default:
                this.description = DESCRIPTIONS[0];
                break;
        }

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void loadImages(int stage) {
        String imgPath = IMG_PATH + "DarklightsStone_stage" + stage + ".png";
        this.img = ImageMaster.loadImage(imgPath);
        this.outlineImg = ImageMaster.loadImage(imgPath.replace(".png", "Outline.png"));
    }

    @Override
    public void onEquip() {
        updateDescription(currentStage);
        loadImages(currentStage);
    }

    @Override
    public Integer onSave() {
        return currentStage;
    }

    @Override
    public void onLoad(Integer stage) {
        if (stage != null) {
            currentStage = stage;
            loadImages(stage);
            updateDescription(stage);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DarklightsStone();
    }

    @Override
    public void onRightClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            playAudio(ProAudio.ufo);
            lastClickTime = currentTime;
        }
    }
}
