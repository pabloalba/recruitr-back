package recruitr


import grails.rest.*
import grails.converters.*

class ProcessController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ProcessController() {
        super(Process)
    }
}
