package recruitr

import grails.rest.RestfulController

class CandidateController extends RestfulController {
    static responseFormats = ['json', 'xml']

    CandidateController() {
        super(Candidate)
    }

    @Override
    def index() {
        def processId = params.processId
        respond Candidate.where {
            process.id == processId
        }.list()
    }
}
