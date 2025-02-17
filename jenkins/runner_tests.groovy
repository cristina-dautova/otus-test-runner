timeout(time: 10, unit: 'MINUTES') {

    node('gradle') {

        checkout scm

        dir('jenkins') {  
            utils = load './utils.groovy'
        }
        utils.prepare_yaml_config()

        def jobs = [:]

        for (def test_type: env.TESTS) {

            jobs[test_type] = node('gradle') {

                stage("Running $test_type tests") {

                    def parameters = [
                            "$REFSPEC",
                            "$CONFIG"
                    ]

                    build (job: "$test_type tests", parameters: parameters, propagate: false, wait: true)
                }
            }
        }

        parallel jobs

    }
}
