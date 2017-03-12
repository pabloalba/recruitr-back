package recruitr

import groovy.json.JsonSlurper


class FixturesController {
    static responseFormats = ['json', 'xml']
    Random rand = new Random()

    def index() {
        createFullProcess(
                'Design',
                'We are looking for a new Designer! Gimp and Inkskape should be your best friends!',
                'http://api-university.com/wp-content/uploads/2016/09/presentation.design.jpg',
                new Date().parse("dd.MM.yyy", '1.4.2017'),
                new Date().parse("dd.MM.yyy", '30.4.2017')
        )

        createFullProcess(
                'Machine Learning',
                'Machine Learning is the future, but we need someone NOW!',
                'http://www.cs.toronto.edu/~urtasun/courses/CSC411_Fall16/CSC411_Fall16_files/machine_learning.jpg',
                new Date().parse("dd.MM.yyy", '1.5.2017'),
                new Date().parse("dd.MM.yyy", '31.5.2017')
        )

        createFullProcess(
                'UX',
                "We need a UX person. We don't require seniority, but the person must be comfortable with FOSS tools",
                'http://www.mobinius.com/wp-content/uploads/2014/02/UX-banner-blog.jpg',
                new Date().parse("dd.MM.yyy", '15.6.2017'),
                new Date().parse("dd.MM.yyy", '15.7.2017')
        )


        render "ok"
    }


    def createFullProcess(title, description, img, startDate, endDate) {
        def process = new Process(
                title: title,
                description: description,
                img: img,
                startDate: startDate,
                endDate: endDate
        ).save()

        (10 + rand.nextInt(10)).times {
            createFullCandidate(process)
        }
    }

    def createFullCandidate(process) {

        def data = new JsonSlurper().parseText("https://randomuser.me/api/".toURL().text)
        def bacon = new JsonSlurper().parseText("https://baconipsum.com/api/?type=all-meat&paras=1".toURL().text)
        def candidate = new Candidate(
                process: process,
                name: data["results"][0]["name"]["first"].capitalize() + " " + data["results"][0]["name"]["last"].capitalize(),
                average: 0,
                documents: [
                        "http://www.pdf995.com/samples/pdfeditsample.pdf",
                        "http://www.pdf995.com/samples/widgets.txt"
                ],
                description: bacon[0]
        ).save()
        def num = rand.nextInt(10)
        if (num > 0) {
            def sum = 0f
            num.times {
                def vote = createVote(candidate)
                sum += vote.vote
            }
            def avg = sum / num
            avg = Math.round(avg * 2) / 2
            candidate.average = avg
            candidate.save(flush: true)
        }


    }

    def createVote(candidate) {
        def data = new JsonSlurper().parseText("https://randomuser.me/api/".toURL().text)
        def bacon = new JsonSlurper().parseText("https://baconipsum.com/api/?type=all-meat&paras=1".toURL().text)
        def vote = new Vote(
                candidate: candidate,
                voter: data["results"][0]["email"],
                vote: (rand.nextInt(10) + 1) / 2,
                comment: bacon[0]
        ).save()
        return vote
    }


}

