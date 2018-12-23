package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class VileToxins extends AspirationRelic {
	public static final String ID = "aspiration:VileToxins";
	
    private static final int DEBUFF_STACK = 1;

    public VileToxins() {
        super(ID, "VileToxins.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DEBUFF_STACK + DESCRIPTIONS[1];
    }

    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (p.ID.equals(PoisonPower.POWER_ID) && target != AbstractDungeon.player && !target.hasPower(ArtifactPower.POWER_ID) && source == AbstractDungeon.player) {
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new WeakPower(target, DEBUFF_STACK, false), DEBUFF_STACK));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new VulnerablePower(target, DEBUFF_STACK, false), DEBUFF_STACK));
        }
    }

    public AbstractRelic makeCopy() {
        return new VileToxins();
    }
}