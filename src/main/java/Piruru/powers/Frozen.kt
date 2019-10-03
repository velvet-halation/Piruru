package Piruru.powers

import Piruru.Piruluk
import Piruru.Piruluk.Statics.makeID
import Piruru.abstracts.PiruruPower
import Piruru.patches.FrozenIntentPatches
import Piruru.patches.FrozenIntentPatches.Enum.FrozenIntentEnum.FROZEN
import basemod.ReflectionHacks
import basemod.interfaces.CloneablePowerInterface
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

import java.lang.reflect.Field


/**
 * Basically stolen from
 * @see com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower
 */
public class Frozen : PiruruPower, CloneablePowerInterface, InvisiblePower {
    public var shaderTimer: Float = 0.toFloat()
    private var moveByte: Byte = 0
    private var moveIntent: AbstractMonster.Intent? = null
    private var move: EnemyMoveInfo? = null

    constructor(owner: AbstractCreature) {
        this.owner = owner
    }

    constructor(owner: AbstractMonster) : super() {
        this.owner = owner
        type = AbstractPower.PowerType.DEBUFF
        shaderTimer = 0.0f
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0]
    }

    companion object {
        val POWER_ID : String = Piruluk.makeID(this::class.java.simpleName)
        internal var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String? = null
        var DESCRIPTION: String? = null
    }

    init {
        NAME = powerStrings.NAME
        DESCRIPTIONS = powerStrings.DESCRIPTIONS
        name = NAME
        ID = POWER_ID
        updateDescription()
        img = Piruluk.textureLoader.getTexture(Piruluk.makePowerPath(this.javaClass.simpleName))
    }

    override fun onDeath() {
        ReflectionHacks.setPrivate(owner, AbstractCreature::class.java, "animationTimer", 0.0f)
    }

    override fun atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(RemoveSpecificPowerAction(owner, owner, this))
    }

    override fun update(slot: Int) {
        super.update(slot)

        if (shaderTimer < 1.0f) {
            shaderTimer += Gdx.graphics.deltaTime
            if (shaderTimer > 1.0f) {
                shaderTimer = 1.0f
            }
        }

        if (owner is AbstractMonster && (owner as AbstractMonster).state != null) {
            (owner as AbstractMonster).state.timeScale = 0f
        }
    }

    override fun onInitialApplication() {
        // Dumb action to delay grabbing monster's intent until after it's actually set
        AbstractDungeon.actionManager.addToBottom(object : AbstractGameAction() {
            override fun update() {
                if (owner is AbstractMonster) {
                    moveByte = (owner as AbstractMonster).nextMove
                    moveIntent = (owner as AbstractMonster).intent
                    try {
                        val f = AbstractMonster::class.java.getDeclaredField("move")
                        f.isAccessible = true
                        move = f.get(owner) as EnemyMoveInfo
                        move!!.intent = FROZEN
                        (owner as AbstractMonster).createIntent()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    }

                }
                isDone = true
            }
        })
    }

    override fun onRemove() {
        if (owner is AbstractMonster) {
            val m = owner as AbstractMonster
            if (move != null) {
                m.setMove(moveByte, moveIntent, move!!.baseDamage, move!!.multiplier, move!!.isMultiDamage)
            } else {
                m.setMove(moveByte, moveIntent)
            }
            m.createIntent()
            m.applyPowers()
        }
    }

    override fun makeCopy(): AbstractPower {
        return Frozen(owner)
    }

    /**
     * Straight nabbed and improved from
     * @see com.evacipated.cardcrawl.mod.stslib.patches.StunMonsterPatch
     * Ty Papa Kio!
     */
    @SpirePatch(clz = GameActionManager::class, method = "getNextAction")
    object FrozenPowerPatch {
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.className == AbstractMonster::class.java.name && m.methodName == "takeTurn") {

                    }
                }
            }
        }
    }


}
