package Cirno.cards

import Cirno.Cirno.Statics.makeID
import Cirno.abstracts.CirnoCard
import Cirno.actions.MillAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster


class AttackAndMill : CirnoCard(cardStrings, COST, TYPE, RARITY, TARGET, DAMAGE_UP, 0, MILL_UP, COST) {
    init {
        damage = DAMAGE
        baseDamage = damage
        magicNumber = MILL_AMT
        baseMagicNumber = magicNumber
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        damage(m!!)
        act(MillAction(magicNumber))
    }

    companion object {
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(
                makeID(AttackAndMill::class.java))
        private val COST = 1
        private val TYPE = AbstractCard.CardType.ATTACK
        private val RARITY = AbstractCard.CardRarity.COMMON
        private val TARGET = AbstractCard.CardTarget.ENEMY
        private val DAMAGE = 9
        private val DAMAGE_UP = 3
        private val MILL_AMT = 4
        private val MILL_UP = 4
    }
}
