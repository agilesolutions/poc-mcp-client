package com.agilesolutions.mcp.evaluation;

import com.agilesolutions.mcp.config.ApplicationProperties;
import com.agilesolutions.mcp.config.McpConfig;
import com.agilesolutions.mcp.tools.StockTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringJUnitConfig(classes = {McpConfig.class, StockTools.class, StockTools.class, ApplicationProperties.class}, initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class RelevancyEvaluatorTest {

    @Autowired
    ChatClient chatClient;

    @Autowired
    RelevancyEvaluator relevancyEvaluator;

    @Test
    public void whenRetrievingStocks_thenReturnLatestPrice() {

        String question = "What is the latest actual stock price for company AAPL?";
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                .call()
                .chatResponse();

        String answer = chatResponse.getResult().getOutput().toString();
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, answer);

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isTrue();

        String wrongAnswer = "bad";
        evaluationRequest = new EvaluationRequest(question, wrongAnswer);
        evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isFalse();


    }


}
