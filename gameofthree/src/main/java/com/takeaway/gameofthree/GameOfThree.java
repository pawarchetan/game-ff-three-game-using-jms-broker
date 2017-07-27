package com.takeaway.gameofthree;


import com.takeaway.gameofthree.services.GameOfThreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class GameOfThree {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GameOfThree.class, args);
        GameOfThreeService gameOfThreeService = context.getBean(GameOfThreeService.class);
        gameOfThreeService.startGame();
    }
}
