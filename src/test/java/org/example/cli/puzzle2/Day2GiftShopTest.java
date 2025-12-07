package org.example.cli.puzzle2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Day2GiftShopTest {

    @Test
    void testExampleScenario() {
        String input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                       "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                       "824824821-824824827,2121212118-2121212124";
        
        long sum = Day2GiftShop.calculateInvalidIdSum(input);
        
        // "Adding up all the invalid IDs in this example produces 1227775554."
        assertEquals(1227775554L, sum);
    }

    @Test
    void testIsInvalidId() {
        // Valid per puzzle description (so isInvalidId should be true)
        assertTrue(Day2GiftShop.isInvalidId(11));
        assertTrue(Day2GiftShop.isInvalidId(22));
        assertTrue(Day2GiftShop.isInvalidId(99));
        assertTrue(Day2GiftShop.isInvalidId(1010));
        assertTrue(Day2GiftShop.isInvalidId(123123));
        assertTrue(Day2GiftShop.isInvalidId(6464));
        
        // Not invalid (so isInvalidId should be false)
        assertFalse(Day2GiftShop.isInvalidId(12));
        assertFalse(Day2GiftShop.isInvalidId(101)); // odd length
        assertFalse(Day2GiftShop.isInvalidId(123124));
        assertFalse(Day2GiftShop.isInvalidId(5)); // odd length
    }
}
