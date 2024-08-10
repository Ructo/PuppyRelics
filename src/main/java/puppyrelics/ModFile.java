package puppyrelics;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import puppyrelics.cards.RatRaceCard;
import puppyrelics.relics.AbstractEasyRelic;
import puppyrelics.relics.DarklightsStone;
import puppyrelics.relics.NikTheGOAT;
import puppyrelics.relics.RatRace;
import puppyrelics.util.ProAudio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ModFile implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber {

    public static final String modID = "puppyrelics";
    public static final String FILE_NAME = "NinjaPuppyConfig";

    public static final String ENABLE_MOD = "enableMod";
    public static boolean modEnabled = true;

    public static final String LEGACY_MODE = "legacyMode";
    public static boolean legacyMode = false;

    public static final String MOUSE_RADIUS = "mouseRadius";
    public static int mouseRadius = 360;

    public static SpireConfig LOConfig;

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static Color characterColor = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);

    public static final String SHOULDER1 = makeCharacterPath("mainChar/shoulder.png");
    public static final String SHOULDER2 = makeCharacterPath("mainChar/shoulder2.png");
    public static final String CORPSE = makeCharacterPath("mainChar/corpse.png");
    private static final String ATTACK_S_ART = makeImagePath("512/attack.png");
    private static final String SKILL_S_ART = makeImagePath("512/skill.png");
    private static final String POWER_S_ART = makeImagePath("512/power.png");
    private static final String CARD_ENERGY_S = makeImagePath("512/energy.png");
    private static final String TEXT_ENERGY = makeImagePath("512/text_energy.png");
    private static final String ATTACK_L_ART = makeImagePath("1024/attack.png");
    private static final String SKILL_L_ART = makeImagePath("1024/skill.png");
    private static final String POWER_L_ART = makeImagePath("1024/power.png");
    private static final String CARD_ENERGY_L = makeImagePath("1024/energy.png");
    private static final String CHARSELECT_BUTTON = makeImagePath("charSelect/charButton.png");
    private static final String CHARSELECT_PORTRAIT = makeImagePath("charSelect/charBG.png");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public ModFile() {
        BaseMod.subscribe(this);
        Properties defaultSettings = new Properties();
        defaultSettings.setProperty(ENABLE_MOD, Boolean.toString(modEnabled));
        defaultSettings.setProperty(LEGACY_MODE, Boolean.toString(legacyMode));
        defaultSettings.setProperty(MOUSE_RADIUS, String.valueOf(mouseRadius));

        try {
            LOConfig = new SpireConfig(modID, FILE_NAME, defaultSettings);
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadConfig() {
        try {
            modEnabled = LOConfig.getBool(ENABLE_MOD);
            legacyMode = LOConfig.getBool(LEGACY_MODE);
            mouseRadius = LOConfig.getInt(MOUSE_RADIUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            LOConfig.setBool(ENABLE_MOD, modEnabled);
            LOConfig.setBool(LEGACY_MODE, legacyMode);
            LOConfig.setInt(MOUSE_RADIUS, mouseRadius);
            LOConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath) {
        return modID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
        ModFile thismod = new ModFile();
    }

    @Override
    public void receiveEditCharacters() {
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyRelic.class)
                .any(AbstractEasyRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
        BaseMod.addRelic(new RatRace(), RelicType.SHARED);
        UnlockTracker.markRelicAsSeen(RatRace.ID);
        BaseMod.addRelic(new DarklightsStone(), RelicType.SHARED);
        UnlockTracker.markRelicAsSeen(DarklightsStone.ID);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new RatRaceCard());
        UnlockTracker.unlockCard(RatRaceCard.ID);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, makePath("localization/" + getLangString() + "/Cardstrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makePath("localization/" + getLangString() + "/Relicstrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makePath("localization/" + getLangString() + "/Charstrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makePath("localization/" + getLangString() + "/Powerstrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makePath("localization/" + getLangString() + "/UIstrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class, makePath("localization/" + getLangString() + "/Orbstrings.json"));
        BaseMod.loadCustomStringsFile(StanceStrings.class, makePath("localization/" + getLangString() + "/Stancestrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, makePath("localization/" + getLangString() + "/Potionstrings.json"));
    }

    @Override
    public void receiveAddAudio() {
        for (ProAudio a : ProAudio.values())
            BaseMod.addAudio(makeID(a.name()), makePath("audio/" + a.name().toLowerCase() + ".ogg"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(makePath("localization/" + getLangString() + "/Keywordstrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public void receivePostInitialize() {
        // Ensure configuration is loaded before initializing the UI
        loadConfig();
        try {
            legacyMode = LOConfig.getBool(LEGACY_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton legacyToggleButton = new ModLabeledToggleButton(
                "Enable Legacy GOAT Mode",
                350.0f,
                750.0f,
                Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                legacyMode,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    legacyMode = button.enabled;

                    // Check the state of legacyMode
                    if (legacyMode) {
                        NikTheGOAT compendiumRelic = (NikTheGOAT) RelicLibrary.getRelic(NikTheGOAT.ID);
                        if (compendiumRelic != null) {
                            compendiumRelic.refreshDescription();
                        }
                    } else {
                        NikTheGOAT compendiumRelic = (NikTheGOAT) RelicLibrary.getRelic(NikTheGOAT.ID);
                        if (compendiumRelic != null) {
                            compendiumRelic.refreshDescription();
                        }
                    }

                    try {
                        LOConfig.setBool(LEGACY_MODE, legacyMode);
                        LOConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        settingsPanel.addUIElement(legacyToggleButton);


        Texture badgeTexture = new Texture(Gdx.files.internal("puppyrelicsResources/images/ui/badge.png"));
        BaseMod.registerModBadge(badgeTexture, "Puppy Relics", "Ninja Puppy", "A collection of relics by NinjaPuppy, some are based on friends, some are idioms. I like to jokingly call it: Idioms and Idiots, with love.", settingsPanel);
    }
}
