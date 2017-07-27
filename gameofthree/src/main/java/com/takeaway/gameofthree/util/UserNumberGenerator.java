package com.takeaway.gameofthree.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
@Log4j2
public class UserNumberGenerator implements NumberGenerator {
    @Override
    public int getNextNumber(int number) {
        log.info("Last number is: " + number + ". \nInput the next number:");
        try {
            Scanner scanner = new Scanner(System.in);
            int nextNumber = scanner.nextInt();
            if (nextNumber != MoveUtil.calculateNextMove(number)) {
                log.error("Error: Number not correct.");
                return getNextNumber(number);
            }
            return nextNumber;
        } catch (InputMismatchException e) {
            log.error("Error: Incorrect input.");
            return getNextNumber(number);
        }
    }

    @Override
    public int getInitialNumber() {
        log.info("Input the First Input (Number):- ");
        try {
            Scanner scanner = new Scanner(System.in);
            int number = scanner.nextInt();
            if (number <= 1) {
                log.error("Error: Number should be > 1");
                return getInitialNumber();
            }
            return number;
        } catch (InputMismatchException e) {
            log.error("Error: Incorrect input.");
            return getInitialNumber();
        }
    }
}
