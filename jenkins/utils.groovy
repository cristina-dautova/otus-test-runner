def prepare_yaml_config() {

    if(!env.CONFIG){
        error "CONFIG environment variable is not set."
    }
    
    def config = readYaml text: env.CONFIG

    config.each { k, v ->  
        env[k] = v.toString()
    }
}

return this
