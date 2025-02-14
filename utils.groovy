def prepare_yaml_config() {

    def config = readYaml text: $CONFIG

    config.each(k,v -> {
        env.setProperty(k,v)
    })
}

return this
