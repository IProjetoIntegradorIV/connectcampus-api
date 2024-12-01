package com.connectcampus.api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
// Indica que esta classe é uma classe de configuração do Spring.
// O Spring irá processá-la durante a inicialização e registrar quaisquer beans definidos aqui no contexto da aplicação.
public class MongoConfig {

    // Campo que armazena o contexto da aplicação fornecido pelo Spring.
    // O `ApplicationContext` é usado para obter outros beans configurados na aplicação.
    private final ApplicationContext applicationContext;

    // Construtor que recebe o contexto da aplicação e o inicializa na variável `applicationContext`.
    // O Spring injeta automaticamente este parâmetro devido à inversão de controle (IoC).
    public MongoConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Método configurado para executar automaticamente após a inicialização do bean.
     * Este método remove o campo `_class` que o MongoDB adiciona por padrão nos documentos armazenados.
     */
    @PostConstruct
    // Marca este método para ser executado após a conclusão da inicialização do bean.
    // Isso garante que as dependências estejam configuradas antes que este método seja chamado.
    public void init() {
        // Obtém o bean `MappingMongoConverter` gerenciado pelo Spring, que é responsável por conversões entre objetos Java e documentos MongoDB.
        MappingMongoConverter mappingMongoConverter =
                applicationContext.getBean(MappingMongoConverter.class);

        // Configura o `MappingMongoConverter` para não adicionar o campo `_class` nos documentos MongoDB.
        // O `DefaultMongoTypeMapper(null)` remove o mapeamento padrão do tipo.
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}