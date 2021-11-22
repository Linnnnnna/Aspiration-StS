package aspiration.relics.boss;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.SnakeRing;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.BaseMod;

public class RingOfOuroboros extends AspirationRelic implements ClickableRelic {
	public static final String ID = "aspiration:RingOfOuroboros";
	
	private static final int DAMAGE_AMOUNT = 3;
	private static final int CARD_DRAW_AMOUNT = 1;
	private boolean duringTurn = true;
	
    public RingOfOuroboros() {
        super(ID, "RingOfOuroboros.png", RelicTier.BOSS, LandingSound.CLINK);
    }
    
    @Override
	public void onEquip() {
    	this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[4] + DESCRIPTIONS[0] + DAMAGE_AMOUNT + DESCRIPTIONS[1] + CARD_DRAW_AMOUNT + DESCRIPTIONS[2]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[3] + CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[4] + DESCRIPTIONS[0] + DAMAGE_AMOUNT + DESCRIPTIONS[1] + CARD_DRAW_AMOUNT + DESCRIPTIONS[2];
    }
    
    @Override
    public boolean canSpawn()
    {
    	return AbstractDungeon.player.hasRelic(SnakeRing.ID);
    }
    
    @Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(SnakeRing.ID)) {
			 for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
	                if (AbstractDungeon.player.relics.get(i).relicId.equals(SnakeRing.ID)) {
	                    instantObtain(AbstractDungeon.player, i, true);
	                    break;
	                }
			 }
		}
		else {
			super.obtain();
		}
	}

	@Override
	public void onRightClick() {
		if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && BaseMod.MAX_HAND_SIZE != AbstractDungeon.player.hand.size() && !AbstractDungeon.player.isDead && duringTurn) {
			AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FAST"));
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
			flash();
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, CARD_DRAW_AMOUNT));
		}
	}

	@Override
	public void atTurnStart() {
		duringTurn = true;
	}

	@Override
	public void onPlayerEndTurn()
	{
		duringTurn = false;
	}
}