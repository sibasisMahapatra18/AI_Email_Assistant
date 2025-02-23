package com.email.writter.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.writter.Model.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class EmGeneratorService {
      
    public EmGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    private final WebClient webClient;
   
   
      @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String genrateEmReply(EmailRequest emailRequest){
        
        //build the prompt
     String prompt=buildPrompt(emailRequest);

        //Craft a request
        Map<String, Object> requestBody = Map.of(
            "contents",new Object[]{
                Map.of("parts",new Object[]{
                    Map.of("text", prompt)
                })
            }
        );
        //Do request and get response
               String response=webClient.post().uri(geminiApiUrl+geminiApiKey).header("Content-Type", "application/json").
               bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
        
       
        // return response
       return extractResponseContent(response);
           }
           private String extractResponseContent(String response) {
              try {
                ObjectMapper mapper=new ObjectMapper();
                JsonNode rootNode=mapper.readTree(response);//JsonNode 
                return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
              } catch (Exception e) {
                return "Error processing request "+e.getMessage();
              }
           }
           private String buildPrompt(EmailRequest emailRequest){
       StringBuilder prompt=new StringBuilder();
         prompt.append("Generate a proffessional email reply for the following email content . Please Don't generate a subject line ");
           if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
               prompt.append("Use a ").append(emailRequest.getTone()).append("tone. ");
           }
           prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
         return prompt.toString();
    }
}
