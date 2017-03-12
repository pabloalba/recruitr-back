package recruitr

class Candidate {
    Process process
    String name
    float average
    String[] documents
    String description

    static hasMany = [votes: Vote]

    static constraints = {
    }

    static mapping = {
        description type: 'text'
    }
}