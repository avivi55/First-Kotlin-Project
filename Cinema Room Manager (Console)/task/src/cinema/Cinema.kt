package cinema

fun <T> printMatrix(matrix: MutableList<MutableList<T>>) {
    println("Cinema:")
    println("  ${(1..matrix[0].size).toList().joinToString(" ")}")
    for ((i, row) in matrix.withIndex()) {
        println("${i + 1} ${row.joinToString(" ")}")
    }
}

data class Seat(var x: Int, var y: Int)

fun getPrice(seat: Seat, rows: Int, seatsPerRow: Int): Int {
    val halfRows = rows / 2

    val seats = rows * seatsPerRow

    val prices = object {
        val firstHalf = 10
        val secondHalf = if (seats > 60) 8 else 10
    }

    return if (seat.y < halfRows) prices.firstHalf else prices.secondHalf
}

fun buyTicket(matrix: MutableList<MutableList<Char>>, rows: Int, seatsPerRow: Int, chosenSeat: Seat): Seat {
    println("Enter a row number:")
    chosenSeat.y = readln().toInt() - 1
    while (chosenSeat.y >= rows){
        println("Wrong Input!")
        chosenSeat.y = readln().toInt() - 1
    }

    println("Enter a seat number in that row:")
    chosenSeat.x = readln().toInt() - 1
    while (chosenSeat.x >= rows){
        println("Wrong Input!")
        chosenSeat.x = readln().toInt() - 1
    }

    matrix[chosenSeat.y][chosenSeat.x] = 'B'

    println()
    println("Ticket price : \$${getPrice(chosenSeat, rows, seatsPerRow)}")

    return chosenSeat
}

fun printStats(soldTicketsCount: Int, percentage: Double, income: Int, totalIncome: Int) =
    println("Number of purchased tickets: $soldTicketsCount\n" +
            "Percentage: ${getFPercentage(percentage)}%\n" +
            "Current income: \$$income\n" +
            "Total income: \$$totalIncome")


fun getFPercentage(a: Double) = "%.2f".format(a)

fun main() {
    println("Enter the number of rows:")
    val rows = readln().toInt()

    println("Enter the number of seats in each row:")
    val seatsPerRow = readln().toInt()

    val seatsMatrix = MutableList(rows) { MutableList(seatsPerRow) { 'S' } }

    var chosenSeat = Seat(0, 0)

    var soldTicketsCount = 0

    var income = 0

    val halfRows = rows / 2
    val seats = rows * seatsPerRow
    val totalIncome = if (seats <= 60)
        10 * seats
    else
        (10 * (halfRows * seatsPerRow)) + (8 * ((rows - halfRows) * seatsPerRow))

    var percentage = 0.0

    val boughtSeats = mutableListOf<Seat>()

    while (true) {
        println("\n1. Show the seats\n" +
                "2. Buy a ticket\n" +
                "3. Statistics\n" +
                "0. Exit")

        when (readln()) {
            "1" -> printMatrix(seatsMatrix)
            "2" -> {
                chosenSeat = buyTicket(seatsMatrix, rows, seatsPerRow, chosenSeat)
                if (chosenSeat in boughtSeats) {
                    println("That ticket has already been purchased")
                    while (chosenSeat in boughtSeats) {
                        chosenSeat = buyTicket(seatsMatrix, rows, seatsPerRow, chosenSeat)
                    }
                }
                soldTicketsCount++
                income += getPrice(chosenSeat, rows, seatsPerRow)
                percentage = (soldTicketsCount.toDouble() / seats.toDouble()) * 100
                boughtSeats.add(chosenSeat.copy())
            }
            "3" -> printStats(soldTicketsCount, percentage, income, totalIncome)
            "0" -> break
            else -> println("Invalid Input")
        }
    }
}