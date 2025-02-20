timeout(time: 10, unit: 'MINUTES') {

    node('gradle') {

        checkout scm

        // dir('jenkins') {  
        //     utils = load './utils.groovy'
        // }
        // utils.prepare_yaml_config()

        parameters {
            string(name: 'YAML_CONFIG', defaultValue: '', description: 'YAML configuration content')
        }

        def config = [:]
        
        stage('Read YAML') {
            script {      
                config = readYaml text: params.YAML_CONFIG
                echo "Jenkins URL: ${config}"
            }
        }

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
