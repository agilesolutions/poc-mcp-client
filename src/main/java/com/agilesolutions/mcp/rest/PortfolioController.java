package com.agilesolutions.mcp.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final ChatClient chatClient;

    @GetMapping("/highest-day/{days}")
    String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate pt = new PromptTemplate("""
                        On which day during last {days} days my wallet had the highest value in dollars based on the historical daily stock prices ?
                """);
        Prompt p = pt.create(Map.of("days", days));
        return this.chatClient.prompt(p)
                .call()
                .content();
    }

    @GetMapping("/latest-stock-price")
    String calculateLatestDailyStockPrice(@PathVariable String personId) {
        PromptTemplate pt = new PromptTemplate("""
                What’s the current value in dollars of my wallet based on the latest stock daily prices ?
                """);
        Prompt p = pt.create();
        return this.chatClient.prompt(p)
                .call()
                .content();
    }

}