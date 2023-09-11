package v1

import v1.Table.Rank.*
import kotlin.properties.Delegates.notNull
import kotlin.random.Random

data class  NumberList(
    private val start: Int,
    private val end: Int,
    private var allNumber: MutableList<Int> = (start..end).mapTo(mutableListOf()) { it }
) {
    fun getRandomNumber(): Int {
        val randomInt = Random.nextInt(0, allNumber.size - 1)
        val result = this.allNumber[randomInt]
        this.allNumber -= this.allNumber[randomInt]
        return result
    }
}

class PowerBall {
    val balls: Set<Int>
    val bonusBall: Int

    init {
        val numberList = NumberList(1, 69)
        balls = (1..5).mapTo(mutableSetOf()) { numberList.getRandomNumber() }
        bonusBall = NumberList(1, 29).getRandomNumber()

        require(balls.size == 5) {
            "PowerBall은 5개의 메인 숫자가 필요합니다. 현재: ${balls.size}"
        }
    }

    override fun toString(): String {
        return "${balls.joinToString("\t")}\t$bonusBall"
    }
}

class Round(val index: Int) {
    lateinit var powerBall: PowerBall
    var totalWining: Long = 0
    var totalMoney: Long = 0
    lateinit var tables: List<Table>

    fun draw() {
        this.powerBall = PowerBall()
    }

    fun buy(count: Int) {
        tables = (1..count).map {
            val table = Table.buy()
            totalMoney += table.price
            table
        }
    }

    fun matchUp() {
        tables.forEach {
            it.checkRank(this.powerBall)
            it.checkPrize(this.totalMoney)
            totalWining += it.prize
        }
    }

}

class Table {
    val powerBall: PowerBall = PowerBall()
    val price: Long = 2
    var rank: Rank = FAIL
    var prize: Long by notNull()
    lateinit var winsBall: Set<Int>
    var bonusBallMatchYN : Boolean = false
    fun checkRank(target: PowerBall) {
       this.winsBall = this.powerBall.balls.intersect(target.balls)
       this.bonusBallMatchYN = this.powerBall.bonusBall == target.bonusBall

        rank = when {
            winsBall.size == 5 && bonusBallMatchYN  -> NO_1
            winsBall.size == 5 && !bonusBallMatchYN -> NO_2
            winsBall.size == 4 && bonusBallMatchYN -> NO_3
            winsBall.size == 4 && !bonusBallMatchYN -> NO_4
            winsBall.size == 3 && bonusBallMatchYN -> NO_5
            winsBall.size == 3 && !bonusBallMatchYN -> NO_6
            winsBall.size == 2 && bonusBallMatchYN -> NO_7
            winsBall.size == 1 && bonusBallMatchYN -> NO_8
            winsBall.isEmpty() && bonusBallMatchYN -> NO_0
            else -> FAIL
        }
    }

    fun checkPrize(totalMoney: Long) {
        this.prize = when (this.rank) {
            NO_1 -> totalMoney
            NO_2 -> NO_2.value
            NO_3 -> NO_3.value
            NO_4 -> NO_4.value
            NO_5 -> NO_5.value
            NO_6 -> NO_6.value
            NO_7 -> NO_7.value
            NO_8 -> NO_8.value
            NO_0 -> NO_0.value
            FAIL -> 0
        }

    }

    companion object {
        fun buy(): Table {
            return Table()
        }
    }


    enum class Rank(val value: Long, val displayName: String) {
        NO_1(0, "1등"),
        NO_2(1_000_000, "2등"),
        NO_3(50_000, "3등"),
        NO_4(100,"4등"),
        NO_5(100, "5등"),
        NO_6(7, "6등"),
        NO_7(7, "7등"),
        NO_8(4,"8등"),
        NO_0(4,"9등"),
        FAIL(0, "꽝")
    }
}



