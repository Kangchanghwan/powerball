package v1

fun main() {
    var index = 0;

    val rounds: MutableList<Round> = mutableListOf()

    do {
        print("구매할 복권의 수를 입력해 주세요: ")
        val num = readln().toInt()
        val round = Round(index = ++index)

        round.buy(num)
        round.draw()
        round.matchUp()

        rounds.add(round)

        UI(
            round = round.index,
            target = round.powerBall,
            tables = round.tables,
            totalMoney = round.totalMoney,
            totalWining = round.totalWining,
            rounds = rounds
        ).draw()

    } while (num != 0)
}


fun getProfit(totalWining: Long,totalMoney: Long) = (totalWining / totalMoney.toDouble() * 100) - 100

data class UI(
    val round: Int,
    val target: PowerBall,
    val tables: List<Table>,
    val totalMoney: Long,
    val totalWining: Long,
    val profit: Double = getProfit(totalWining,totalMoney),
    val rounds: MutableList<Round>
) {

    fun draw() {
        print("\n\n")
        println("$round\t회차\tPowerBall")
        println("당첨번호 : $target")
        println("-------------------구매 내역----------------------")
        tables.forEach {
            println("${it.powerBall}\t")
        }
        println("-------------------당첨 내역----------------------")
        println("당첨순위\t\t\t번호\t\t\t\t당첨개수\t보너스볼당첨여부")
        tables.filter { it.rank != Table.Rank.FAIL }.forEach {
            println("${it.rank.displayName}\t${it.powerBall}\t${it.winsBall.size}\t\t${it.bonusBallMatchYN}")
        }

        println("-------------------결과 종합----------------------")
        tables.groupBy { it.rank }.forEach{ k, v -> println("${k.displayName} : ${v.size}")}
        println("${round}회차 구매금액 : ${totalMoney}달러\t 당첨금액 : ${totalWining}달러\t수익율 : ${profit.toInt()}%")
        val allTotalMoney = rounds.sumOf { it.totalMoney }
        val allTotalWining = rounds.sumOf { it.totalWining }
        println("누적 구매금액 : ${allTotalMoney}달러\t 당첨금액 : ${allTotalWining}달러\t수익율 : ${getProfit(allTotalWining,allTotalMoney).toInt()}%")
    }

}