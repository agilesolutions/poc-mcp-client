package com.agilesolutions.mcp.config;

import com.agilesolutions.mcp.rest.StockClient;
import com.agilesolutions.mcp.tools.StockTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class McpConfig {

    @Bean("stockClient")
    public StockClient stockClient(ApplicationProperties applicationProperties) {
        RestClient restClient = RestClient.builder().baseUrl(applicationProperties.getUrl()).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StockClient.class);
    }


    @Bean
    ChatClient chatClient(@Value("${com.agilesolutions.request.model}") String model,
                          @Value("${com.agilesolutions.request.model}") String baseUrl) {

        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(new OllamaApi(baseUrl))
                .defaultOptions(OllamaOptions.builder().model(model).build())
                .build();

        return ChatClient
                .builder(chatModel)
                .defaultTools(StockTools.class)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

    }

    @Bean
    public ChatClient contentEvaluator(@Value("${com.agilesolutions.evaluation.model}") String model,
            @Value("${com.agilesolutions.evaluation.model}") String baseUrl
    ) {
        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(new OllamaApi(baseUrl))
                .defaultOptions(OllamaOptions.builder().model(model).build())
                .build();

        return ChatClient.builder(chatModel)
                .build();
    }

    @Bean
    public RelevancyEvaluator relevancyEvaluator(
            @Qualifier("contentEvaluator") ChatClient chatClient) {
        return new RelevancyEvaluator(chatClient.mutate());
    }

    @Bean
    public FactCheckingEvaluator factCheckingEvaluator(
            @Qualifier("contentEvaluator") ChatClient chatClient) {
        return new FactCheckingEvaluator(chatClient.mutate());
    }

}
