def prepare_yaml_config() {

    def config = readYaml text: env.CONFIG

    config.each { k, v ->  
        env[k] = v.toString()
    }
}

return this
