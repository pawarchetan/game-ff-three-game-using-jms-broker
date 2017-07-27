package com.takeaway.gameofthree.util;

class MoveUtil {
    static int calculateNextMove(int number) {
        int reminder = number % 3;
        if(reminder == 0) {
            return number/3;
        } else if(reminder == 1 || reminder == 2) {
            return (number + 1)/3;
        } else {
            return number/3;
        }
    }
}
