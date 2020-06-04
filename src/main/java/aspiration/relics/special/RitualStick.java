package aspiration.relics.special;

import aspiration.Utility.RelicStatsHelper;
import aspiration.powers.P_RitualPower;
import aspiration.relics.abstracts.StatRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class RitualStick extends StatRelic implements CustomSavable<Integer> {
    public static final String ID = "aspiration:RitualStick";
    private boolean wasApplied = false;
    private int ritualAmount = 1;
    private boolean hasCM = false;

    private static final String STAT1 = "Times triggered: ";

    public RitualStick() {
        super(ID, "RitualStick.png", RelicTier.SPECIAL, LandingSound.FLAT);
        updateTip();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ritualAmount + DESCRIPTIONS[1];
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (damageAmount > 0 && !wasApplied) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new P_RitualPower(AbstractDungeon.player, ritualAmount), ritualAmount));
                stopPulse();
                wasApplied = true;
                RelicStatsHelper.incrementStat(this, STAT1);
            }

            if (hasCM) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("VO_CULTIST_1A"));
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.animations.TalkAction(true, DESCRIPTIONS[2], 1.0F, 2.0F));
            }
        }
    }

    @Override
    public void onRelicGet(AbstractRelic abstractRelic) {
        if(abstractRelic.relicId.equals(CultistMask.ID)) {
            ritualEasteregg();
        }
    }

    @Override
    public void onEquip() {
        this.description = getUpdatedDescription();
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            ritualEasteregg();
        }
    }

    private void ritualEasteregg() {
        this.ritualAmount = 3;
        this.hasCM = true;
        this.description = getUpdatedDescription();
        updateTip();
        flash();
    }

    @Override
    public void atPreBattle() {
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        wasApplied = false;
        stopPulse();
    }

    @Override
    public Integer onSave() {
        return ritualAmount;
    }

    @Override
    public void onLoad(Integer ints) {
        ritualAmount = ints;
        if(ints == 1) {
            hasCM = false;
        } else {
            hasCM = true;
        }
    }

    private void updateTip() {
        this.initializeTips();
        this.tips.clear();
        this.tips.add(new PowerTip(name, DESCRIPTIONS[0] + ritualAmount + DESCRIPTIONS[1]));
        this.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public void statsInit() {
        stats.put(STAT1, 0);
    }
}