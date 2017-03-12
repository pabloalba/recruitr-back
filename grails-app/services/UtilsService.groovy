class UtilsService {
    def calculateAverage(votes) {
        println("Votes2: ${votes}")
        if (votes.size() == 0) {
            return 0
        }
        def sum = votes.sum { it.vote }
        def avg = sum / votes.size()
        return Math.round(avg * 2) / 2
    }
}
