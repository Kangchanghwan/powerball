fun main() {
    var index = 0;

    do {
        val num = readLine()!!.toInt()
        val round = Round(index = ++index)

        round.buy(num)
        round.draw()
        round.matchUp()

        UI(
            round = round.index,
            target = round.powerBall,
            tables = round.tables,
            totalMoney = round.totalMoney,
            totalWining = round.totalWining,
        ).draw()

    } while (num != 0)
}


data class UI(
   val round: Int,
   val target: PowerBall,
   val tables: List<Table>,
   val totalMoney: Long,
   val totalWining: Long,
   val profit: Double = (totalWining/totalMoney.toDouble() * 100) - 100
) {

    fun draw() {
        println("$round\t회차\tPowerBall")
        println("당첨번호 : $target")
        println("-------------------당첨 내역----------------------")
        println("당첨순위\t\t\t번호\t\t\t\t당첨번호\t보너스볼당첨여부")
        tables.filter { it.isWin }.forEach {
            println("${it.rank}\t${it.powerBall}\t${it.winsBall.joinToString("\t")}\t${it.bonusBallMatchYN}")
        }
        println("-----------------------------------------------")

        println("총 구매금액 : ${totalMoney}달러\t총 당첨금액 : ${totalWining}달러\t수익율 : $profit%")

    }

}