package cirno.interfaces

import cirno.characters.spellZones
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import javassist.CtBehavior
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.OverlayMenu
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect
import kotlin.random.Random

interface Shiver {

    companion object{
        val shiverEffect = RarePotionParticleEffect(AbstractDungeon.player.hb)
    }

    @SpirePatch(
            clz = AbstractPlayer::class,
            method = SpirePatch.CLASS
    )
    public class ShiverActive {
        public companion object {
            @JvmField
            public var isShivering =  SpireField<Boolean> {false}
            @JvmField
            public var amountOfShiveringCards = SpireField<Int> {0}
        }
    }

    @SpirePatch(
            clz = AbstractCard::class,
            method = SpirePatch.CLASS
    )
    public class ShiverCard {
        public companion object {
            @JvmField
            public var cardShivering = SpireField<Boolean> {false}
        }
    }

    @SpirePatch(
            clz = GameActionManager::class,
            method = SpirePatch.CLASS
    )
    public class CardsDrawnThisCombat {
        public companion object {
            @JvmField
            public var cardsDrawnThisCombat = SpireField<Int>{0}
        }
    }

    @SpirePatch(
            clz = GameActionManager::class,
            method = "getNextAction"
    )
    public class SetShivering {
        public companion object {
            @SpireInsertPatch(
                    locator = Locator::class
            )
            @JvmStatic
            fun Insert(__instance: GameActionManager) {
                AbstractDungeon.actionManager.addToBottom(object : AbstractGameAction() {
                    override fun update() {
                        AbstractDungeon.player.isShivering = true
                        isDone = true
                    }
                })
            }
        }
    }

    @SpirePatch(
            clz = AbstractRoom::class,
            method = "update"
    )
    public class ShiverAtStartOfCombat {
        public companion object {
            @SpireInsertPatch(
                    locator = StartOfCombatShiverLocator::class
            )
            @JvmStatic
            fun Insert(__instance: AbstractRoom) {
                AbstractDungeon.actionManager.addToBottom(object : AbstractGameAction() {
                    override fun update() {
                        AbstractDungeon.player.isShivering = true
                        isDone = true
                    }
                })
            }
        }
    }

    public class StartOfCombatShiverLocator : SpireInsertLocator() {
        override fun Locate(p0: CtBehavior?): IntArray {
            val matcher = Matcher.MethodCallMatcher(OverlayMenu::class.java, "showCombatPanels")
            return LineFinder.findInOrder(p0, matcher)
        }

    }

    public class Locator : SpireInsertLocator() {
        override fun Locate(p0: CtBehavior?): IntArray {
            val matcher = Matcher.MethodCallMatcher(AbstractPlayer::class.java, "applyStartOfTurnPostDrawRelics")
            return LineFinder.findInOrder(p0, matcher)
        }

    }

    @SpirePatch(
            clz = GameActionManager::class,
            method = "endTurn"
    )
    public class EndTurn {
        public companion object {
            @JvmStatic
            fun Prefix(__instance: GameActionManager) {
                AbstractDungeon.player.isShivering = false
                AbstractDungeon.player.shiveredCards = 0
                for (card in AbstractDungeon.player.hand.group) {
                    card.shivered = false
                }
                for (card in AbstractDungeon.player.drawPile.group) {
                    card.shivered = false
                }
                for (card in AbstractDungeon.player.discardPile.group) {
                    card.shivered = false
                }
                for (card in AbstractDungeon.player.exhaustPile.group) {
                    card.shivered = false
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer::class,
            method = "draw",
            paramtypez = [
                Int::class
            ]
    )
    public class DrawPatch {
        public companion object {
            @SpireInsertPatch(
                    locator = DrawLocator::class,
                    localvars = [
                        "c"
                    ]
            )
            @JvmStatic
            fun Insert(__instance: AbstractPlayer, numCards: Int, c: AbstractCard) {
                if (__instance.isShivering) {
                    AbstractDungeon.player.shiveredCards++
                    c.shivered = true
                }
                AbstractDungeon.player.spellZones.onDraw()
                AbstractDungeon.actionManager.cardsDrawnThisCombat++
            }
        }
    }

    fun onShiver()

    public class DrawLocator : SpireInsertLocator() {
        override fun Locate(p0: CtBehavior?): IntArray {
            val matcher = Matcher.MethodCallMatcher(AbstractCard::class.java, "triggerWhenDrawn")
            return LineFinder.findInOrder(p0, matcher)
        }
    }

    @SpirePatch(
            clz = GameActionManager::class,
            method = "clear"
    )
    public class ClearCardsDrawn {
        public companion object {
            fun Postfix(__instance: GameActionManager) {
                __instance.cardsDrawnThisCombat = 0
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer::class,
            method = "render"
    )
    public class RenderShiverEffect {
        public companion object {
            var sparkleTimer = 0f
            var x = 0f
            var y = 0f
            @JvmStatic
            fun Prefix(__instance: AbstractPlayer, sb: SpriteBatch) {
                if (__instance.isShivering) {
                    sparkleTimer -= Gdx.graphics.deltaTime
                    x = __instance.drawX + __instance.hb_w * Random.nextFloat()
                    y =  __instance.drawY + __instance.hb_h * Random.nextFloat()
                    if (sparkleTimer < 0.40f && Random.nextFloat() < 0.2f) {
                        AbstractDungeon.effectList.add(UncommonPotionParticleEffect(x - 120f * Settings.scale, y - 30f * Settings.scale))
                        sparkleTimer = MathUtils.random(0.30f, 0.50f)
                    }
                    if (sparkleTimer < 0.0f) {
                        AbstractDungeon.effectList.add(UncommonPotionParticleEffect(x - 40f * Settings.scale, y - 30f * Settings.scale))
                        sparkleTimer = MathUtils.random(0.30f, 1.0f)
                    }
                }
            }
        }
    }

}

var AbstractPlayer.isShivering: Boolean
    get() = Shiver.ShiverActive.isShivering.get(this)
    set(value) = Shiver.ShiverActive.isShivering.set(this, value)

var AbstractPlayer.shiveredCards: Int
    get() = Shiver.ShiverActive.amountOfShiveringCards.get(this)
    set(value) = Shiver.ShiverActive.amountOfShiveringCards.set(this, value)

var GameActionManager.cardsDrawnThisCombat
    get() = Shiver.CardsDrawnThisCombat.cardsDrawnThisCombat.get(this)
    set(value) = Shiver.CardsDrawnThisCombat.cardsDrawnThisCombat.set(this, value)

var AbstractCard.shivered: Boolean
    get() = Shiver.ShiverCard.cardShivering.get(this)
    set(value) = Shiver.ShiverCard.cardShivering.set(this, value)