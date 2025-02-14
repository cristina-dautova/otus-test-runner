def prepare_yaml_config() {

    def config = readYaml text: $CONFIG

    config.each { k, v ->  
        env[k] = v.toString()
    }
}

return this
