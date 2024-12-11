package com.doziem.treading.controller;

import com.doziem.treading.model.Coin;
import com.doziem.treading.service.ICoinService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {
    @Autowired
    private ICoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/all")
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coin> coins = coinService.getCoinList(page);

        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId,
                                                   @RequestParam("days") int days) throws Exception {
        String response = coinService.getMarketChart(coinId,days);

        JsonNode jsonNode = objectMapper.readTree(response);

        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String response = coinService.searchCoin(keyword);

        JsonNode jsonNode = objectMapper.readTree(response);

        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50ByMarketCapRank() throws Exception {
        String response = coinService.getTop50CoinsByMarketCapRank();

        JsonNode jsonNode = objectMapper.readTree(response);

        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/treading")
    public ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
        String response = coinService.getTreadingCoins();

        JsonNode jsonNode = objectMapper.readTree(response);

        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String response = coinService.getCoinDetails(coinId);

        JsonNode jsonNode = objectMapper.readTree(response);

        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }
}
