import Table.Rank.*
import kotlin.properties.Delegates.notNull
import kotlin.random.Random

data class  NumberList(
    private val start: Int,
    private val end: Int,
    private var allNumber: MutableList<Int> = (start..end).mapTo(mutableListOf()) { it }
) {
    fun getRandomNumber(): Int {
        val nextInt = Random.nextInt(0, allNumber.size - 1)
        val result = this.allNumber[nextInt]
        this.allNumber -= this.allNumber[nextInt]
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
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is PowerBall ->
                this.balls.containsAll(other.balls) && this.bonusBall == other.bonusBall

            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = balls.hashCode()
        result = 31 * result + bonusBall
        return result
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
            this.powerBall == target -> NO_1
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


    enum class Rank(val value: Long) {
        NO_1(0),
        NO_2(1_000_000),
        NO_3(50_000),
        NO_4(100),
        NO_5(100),
        NO_6(7),
        NO_7(7),
        NO_8(4),
        NO_0(4),
        FAIL(0)
    }
}



