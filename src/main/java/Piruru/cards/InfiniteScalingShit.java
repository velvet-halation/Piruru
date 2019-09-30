package Piruru.cards;

import Piruru.Piruru;
import Piruru.abstracts.PiruruCard;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.blue.Hologram;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Piruru.Piruru.makeID;

public class InfiniteScalingShit extends PiruruCard {
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(
            makeID(InfiniteScalingShit.class.getSimpleName()));
    private static final int COST = 0;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int PULL_AMT = 1;
    private static final int PULL_UP = 2;

    public InfiniteScalingShit() {
        super(cardStrings, COST, TYPE, RARITY, TARGET, 0, 0, PULL_UP, COST);
        baseMagicNumber = magicNumber = PULL_AMT;
        exhaust = true;
    }

    @Override
    public void upgrade(){
        timesUpgraded++;
        if (!upgraded) {
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
        upgraded = true;
        name = name + "+" + timesUpgraded;
        initializeTitle();
        upgradeMagicNumber(PULL_UP);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        act(new BetterDiscardPileToHandAction(magicNumber));
    }
}
