package puppyrelics.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppyrelics.vfx.ShootAnythingEffect;

import static puppyrelics.ModFile.makeID;

public class PirateRaidCard extends CustomCard {
    public static final String ID = makeID("PirateRaidCard");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "puppyrelicsResources/images/512/AgmensHat.png"; // placeholder path if you have one
    public static final int COST = 1;
    private static final int DMG = 10;
    private static final int GOLD_PER_ENEMY = 5;

    public PirateRaidCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        baseDamage = DMG;
        isMultiDamage = true;
        isEthereal = true;
        exhaust = true;
    }
    private static com.badlogic.gdx.graphics.Texture CANNONBALL_TEX;
    private static com.badlogic.gdx.graphics.Texture getCannonballTex() {
        if (CANNONBALL_TEX == null) {
            CANNONBALL_TEX = puppyrelics.util.TexLoader.getTexture(
                    "puppyrelicsResources/images/vfx/Cannonball.png"
            );
            if (CANNONBALL_TEX != null) {
                CANNONBALL_TEX.setFilter(
                        com.badlogic.gdx.graphics.Texture.TextureFilter.Linear,
                        com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
                );
            }
        }
        return CANNONBALL_TEX;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // snapshot
        int aliveAtCast = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo != null && !mo.isDeadOrEscaped() && !mo.halfDead) aliveAtCast++;
        }
        final int goldToGrant = aliveAtCast * GOLD_PER_ENEMY;

        // pay first
        if (goldToGrant > 0) addToTop(new GainGoldAction(goldToGrant));
        addToTop(new AbstractGameAction() {
            @Override public void update() {
                AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.RainingGoldEffect(goldToGrant));
                isDone = true;
            }
        });


        // fire cannonballs
        com.badlogic.gdx.graphics.Texture tex = getCannonballTex();
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo != null && !mo.isDeadOrEscaped()) {
                // false = shoot from player to target; 1 = single projectile
                addToBot(new com.megacrit.cardcrawl.actions.animations.VFXAction(
                        new puppyrelics.vfx.ShootAnythingEffect(mo, tex, false, 1), 0.0f));
            }
        }

        // AoE damage without the default slash
        addToBot(new com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction(
                p, this.multiDamage, DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // Keeps multiDamage in sync
    }

    @Override
    public AbstractCard makeCopy() { return new PirateRaidCard(); }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            initializeDescription();
        }
    }
}
