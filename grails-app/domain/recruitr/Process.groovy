package recruitr

class Process {
    String title
    String description
    String img
    Date startDate
    Date endDate

    static constraints = {
        title blank: false
        description blank: false
    }

    static mapping = {
        description type: 'text'
    }
}
