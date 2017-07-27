package com.takeaway.gameofthree;

import com.takeaway.gameofthree.service.GameOfThreeServiceImplTest;
import com.takeaway.gameofthree.storage.GameOfThreeSessionImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameOfThreeServiceImplTest.class ,GameOfThreeSessionImplTest.class
})
public class GameOfThreeTestSuite {
}
