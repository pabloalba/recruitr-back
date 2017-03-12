package recruitr

class Vote {
    Candidate candidate
    String voter
    float vote
    String comment

    static constraints = {
    }
    static mapping = {
        comment type: 'text'
    }
}
