package recruitr

import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

class VoteController extends RestfulController {
    static responseFormats = ['json', 'xml']
    def utilsService

    VoteController() {
        super(Vote)
    }

    @Override
    def index() {
        def candidateId = params.candidateId
        respond Vote.where {
            candidate.id == candidateId
        }.list()
    }

    @Override
    @Transactional
    def save() {
        if (handleReadOnly()) {
            return
        }
        def instance = createResource()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view: 'create' // STATUS CODE 422
            return
        }

        saveResource instance
        updateCandidateAverage(instance.candidate)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [classMessageArg, instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: this.controllerName, action: 'show', id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null))
                respond instance, [status: CREATED, view: 'show']
            }
        }
    }


    @Override
    @Transactional
    def update() {
        if (handleReadOnly()) {
            return
        }

        Vote instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        instance.properties = getObjectToBind()

        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view: 'edit' // STATUS CODE 422
            return
        }

        updateResource instance
        updateCandidateAverage(instance.candidate)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [classMessageArg, instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: this.controllerName, action: 'show', id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null))
                respond instance, [status: OK]
            }
        }
    }

    def updateCandidateAverage(candidate) {
        candidate.average = utilsService.calculateAverage(candidate.votes)
        candidate.save()
    }
}
