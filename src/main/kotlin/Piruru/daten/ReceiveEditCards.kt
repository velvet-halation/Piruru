package Piruru.daten

import basemod.BaseMod
import Piruru.cards.*

/**
 * But Reina, I can hear you ask, why aren't you using autoAddCards from Kio? Well the answer is simple
 * I'm too lazy to copy paste all that stuff and make like 90 classes and make.sh does this for me.
 */
class ReceiveEditCards {

    init {
		BaseMod.addCard(AddColdARTS());//delete
		BaseMod.addCard(AddDamageARTS());//delete
		BaseMod.addCard(AddDrawARTS());//delete
		BaseMod.addCard(AllForOneButSkills());//delete
		BaseMod.addCard(AoECold());//delete
		BaseMod.addCard(AoEFreeze());//delete
		BaseMod.addCard(AoEMill());//delete
		BaseMod.addCard(AttackAndMill());//delete
		BaseMod.addCard(AttackAndScry());//delete
		BaseMod.addCard(AttackBanishAttack());//delete
		BaseMod.addCard(AttackBlock());//delete
		BaseMod.addCard(AttackDiscard());//delete
		BaseMod.addCard(AttackExhume());//delete
		BaseMod.addCard(AttackRecoverRandom());//delete
		BaseMod.addCard(BanishExhume());//delete
		BaseMod.addCard(BanishThenRecover());//delete
		BaseMod.addCard(BigDickARTSAttack());//delete
		BaseMod.addCard(BlockDrawMill());//delete
		BaseMod.addCard(ColdARTS());//delete
		BaseMod.addCard(ColdBane());//delete
		BaseMod.addCard(ColdDraw());//delete
		BaseMod.addCard(DamageAndCold());//delete
		BaseMod.addCard(DamageARTS());//delete
		BaseMod.addCard(Defend());//delete
		BaseMod.addCard(DiscardAndGainEnergy());//delete
		BaseMod.addCard(DiscardAnyAmountAndGainEnergy());//delete
		BaseMod.addCard(DiscardChain());//delete
		BaseMod.addCard(DiscardForBlock());//delete
		BaseMod.addCard(DiscardRandomCold());//delete
		BaseMod.addCard(DrawARTS());//delete
		BaseMod.addCard(EnterACRO());//delete
		BaseMod.addCard(EnterAllos());//delete
		BaseMod.addCard(EnterApexForm());//delete
		BaseMod.addCard(EnterRemember());//delete
		BaseMod.addCard(ExcessBlock());//delete
		BaseMod.addCard(ExcessCardsDoesMoreDamage());//delete
		BaseMod.addCard(FlameBarrierButCold());//delete
		BaseMod.addCard(FreezeEnemy());//delete
		BaseMod.addCard(InfiniteScalingShit());//delete
		BaseMod.addCard(MillChainCopy());//delete
		BaseMod.addCard(MillForPowers());//delete
		BaseMod.addCard(Mulligan());//delete
		BaseMod.addCard(NoCardsBlock());//delete
		BaseMod.addCard(RecoverDamage());//delete
		BaseMod.addCard(ScoutAttacks());//delete
		BaseMod.addCard(SpreadCold());//delete
		BaseMod.addCard(Strike());//delete
		BaseMod.addCard(UnceasingTopPower());//delete
		BaseMod.addCard(YeetNCostCardToDiscard());//delete
		//autoAddCards
    }

}