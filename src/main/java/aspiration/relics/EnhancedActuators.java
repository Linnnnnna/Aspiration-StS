package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.abstracts.OnOrbSlotChange;

public class EnhancedActuators extends AspirationRelic implements OnOrbSlotChange{
public static final String ID = "aspiration:EnhancedActuators";
	
	private static final int ORB_SLOT_THRESHOLD = 2;
	private static final int GAINZ = 1;
	private int current_strength_given = 0;
	
    public EnhancedActuators() {
        super(ID, "EnhancedActuators.png", RelicTier.COMMON, LandingSound.HEAVY);
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + GAINZ + DESCRIPTIONS[1] + ORB_SLOT_THRESHOLD + DESCRIPTIONS[2];
    }
    
    /*@Override
    public void atPreBattle() {
    	current_strength_given = (int) (Math.floor(AbstractDungeon.player.maxOrbs / ORB_SLOT_THRESHOLD));
    	if(current_strength_given > 0) {
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, current_strength_given), current_strength_given));
    	}
    }*/
    
    @Override
	public void onOrbSlotChange(int amount, boolean isReduce) {
		int tmp = 0;
		if (isReduce) {
			tmp = (int) Math.floor((AbstractDungeon.player.maxOrbs - amount) / ORB_SLOT_THRESHOLD);
		} else {
			tmp = (int) Math.floor((AbstractDungeon.player.maxOrbs + amount) / ORB_SLOT_THRESHOLD);
		}

		if (current_strength_given > tmp) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(StrengthPower.POWER_ID), current_strength_given - tmp));
		} else if (current_strength_given < tmp) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, tmp - current_strength_given), tmp - current_strength_given));
		}
		current_strength_given = tmp;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new EnhancedActuators();
	}
}
