package cirno.cards

import cirno.Cirno.Statics.makeID
import cirno.abstracts.CirnoCard
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import cirno.interfaces.Helper
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.helpers.ImageMaster
import cirno.vfx.MoveImageWithSparkleEffect
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.badlogic.gdx.utils.IntArray
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect
import javazoom.jl.decoder.LayerIIIDecoder.io




class SwapPilesGainBlock : CirnoCard(cardStrings, COST, TYPE, RARITY, TARGET, DAMAGE_UP, BLOCK_UP, MAGIC_UP, 0), Helper {

    init {
        baseDamage = DAMAGE
        baseBlock = BLOCK
        magicNumber = MAGIC
        baseMagicNumber = magicNumber
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        block(defaultSource, discardPile.size())
        discardPile.group = drawPile.group.also { drawPile.group = discardPile.group }
        AbstractDungeon.effectsQueue.add(MoveImageWithSparkleEffect(CardGroup.DRAW_PILE_X,
                CardGroup.DRAW_PILE_Y,
                CardGroup.DISCARD_PILE_X.toFloat(),
                CardGroup.DISCARD_PILE_Y.toFloat(),
                ImageMaster.DECK_BTN_BASE,
                Color.ORANGE.cpy(),
                "POWER_TIME_WARP",
                FrostOrbPassiveEffect(CardGroup.DISCARD_PILE_X.toFloat(), CardGroup.DISCARD_PILE_Y.toFloat())
        ))
    }

    companion object {
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(
                makeID(SwapPilesGainBlock::class.java.simpleName))
        private val COST = 1
        private val TYPE = CardType.SKILL
        private val RARITY = AbstractCard.CardRarity.UNCOMMON
        private val TARGET = AbstractCard.CardTarget.SELF
        private val DAMAGE_UP = 0
        private val BLOCK_UP = 0
        private val MAGIC_UP = 0
        private val DAMAGE = 0
        private val BLOCK = 0
        private val MAGIC = 0
    }
}
