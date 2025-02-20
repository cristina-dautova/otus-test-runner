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
                echo "Using CONFIG: ${config}"
                //echo "TESTS: ${config.TESTS}"
            }
        }

        def jobs = [:]

        def test_types = config.TESTS ?: []

        for (def test_type in test_types) {

            jobs[test_type] = node('gradle') {

                stage("Running $test_type tests") {

                        def refspecValue = env.REFSPEC ?: ""
                        def configValue = env.CONFIG ?: "default-config"

                        echo "Running tests for: ${test_type}"
                        echo "Using CONFIG: ${configValue}"
                        echo "Using REFSPEC: ${refspecValue}"

                        def parameters = [
                            string(name: 'REFSPEC', value: refspecValue),
                            string(name: 'CONFIG', value: configValue)
                        ]
                    
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
