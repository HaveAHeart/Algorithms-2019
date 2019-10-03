package lesson1;

import com.jcraft.jsch.IO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import static lesson1.JavaTasks.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class SortsTest {

    static private final Random r = new Random(Calendar.getInstance().getTimeInMillis());

    private static void assertSorted(int[] arr, String prefix) {
        for (int i = 0; i< arr.length-1; i++) {
            assertTrue(arr[i] <= arr[i+1],
                       prefix + " ERROR: i = " + i + " a[i] = " + arr[i] + " a[i+1] = " + arr[i+1]);
        }
    }

    private static <T extends Comparable<T>> void assertSorted(T[] arr, String prefix) {
        for (int i = 0; i< arr.length-1; i++) {
            assertTrue(arr[i].compareTo(arr[i+1]) <= 0,
                       prefix + " ERROR: i = " + i + " a[i] = " + arr[i] + " a[i+1] = " + arr[i+1]);
        }
    }

    @Test
    @Tag("Example")
    public void insertionSort() {
        int[] arr = new int[] { 3, 7, 5, 9, 1, 6, 19, 13 };
        Sorts.insertionSort(arr);
        assertSorted(arr, "INSERTION SORT");
    }

    @Test
    @Tag("Example")
    public void insertionSortStrings() {
        String[] arr = new String[] { "beta", "omega", "alpha", "", "!!!", "teta", "O" };
        Sorts.insertionSort(arr);
        assertSorted(arr, "INSERTION SORT");
    }

    @Test
    @Tag("Example")
    public void mergeSort() {
        int[] arr = new int[] { 3, 7, 5, 9, 1, 6, 19, 13 };
        Sorts.mergeSort(arr);
        assertSorted(arr, "MERGE SORT");
    }

    @Test
    @Tag("Example")
    public void longInsertionSort() {
        int LENGTH = 65536;
        int[] arr = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            arr[i] = r.nextInt();
        }
        Sorts.insertionSort(arr);
        assertSorted(arr, "INSERTION SORT LONG");
    }

    @Test
    @Tag("Example")
    public void longMergeSort() {
        int LENGTH = 65536;
        int[] arr = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            arr[i] = r.nextInt();
        }
        Sorts.mergeSort(arr);
        assertSorted(arr, "MERGE SORT LONG");
    }

    @Test
    @Tag("Example")
    public void longHeapSort() {
        int LENGTH = 65536;
        int[] arr = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            arr[i] = r.nextInt();
        }
        Sorts.heapSort(arr);
        assertSorted(arr, "HEAP SORT LONG");
    }

    @Test
    @Tag("Example")
    public void quickSort() {
        int[] arr = new int[] { 3, 7, 5, 9, 1, 6, 19, 13 };
        Sorts.quickSort(arr);
        assertSorted(arr, "QUICK SORT");
    }

    @Test
    @Tag("Example")
    public void longQuickSort() {
        int LENGTH = 65536;
        int[] arr = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            arr[i] = r.nextInt();
        }
        Sorts.quickSort(arr);
        assertSorted(arr, "QUICK SORT LONG");
    }

    @Test
    @Tag("Example")
    public void longCountingSort() {
        int LENGTH = 65536;
        int LIMIT = 262144;
        int[] arr = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            arr[i] = r.nextInt(LIMIT);
        }
        int[] result = Sorts.countingSort(arr, LIMIT - 1);
        assertSorted(result, "COUNTING SORT LONG");
        Sorts.quickSort(arr);
        assertArrayEquals(arr, result);
    }

    //task 1
    @Test
    public void testTTI() {
        String[] testStrings = {"12:00:00 AM", "12:00:00 PM", "07:15:50 PM", "00:00:01 AM", "08:10:30 AM"};
        //0, 43200, 69350, 1, 29430
        int[] results = {0, 43200, 69350, 1, 29430};
        for (int j = 0; j < 5; j++) assertEquals(timeToInt(testStrings[j]), results[j]);
    }

    @Test
    public void testQuickSort() {

        ArrayList<String> testCase1 = new ArrayList<>(); //empty case
        ArrayList<String> testSol1 = new ArrayList<>();
        timeQuickSort(testCase1, 0, testCase1.size());
        assertEquals(testSol1, testCase1);

        String[] testCaseBuilder2 = {"12:00:00 PM", "11:59:59 PM", "12:00:00 AM", "11:59:59 AM", "08:11:17 AM"}; //12:00:00 AM/PM case
        ArrayList<String> testCase2 = new ArrayList<>(Arrays.asList(testCaseBuilder2));
        timeQuickSort(testCase2, 0, testCase2.size() -1);
        String[] testSolBuilder2 = {"12:00:00 AM", "08:11:17 AM", "11:59:59 AM", "12:00:00 PM", "11:59:59 PM"};
        ArrayList<String> testSol2 = new ArrayList<>(Arrays.asList(testSolBuilder2));
        assertEquals(testSol2, testCase2);

        String[] testCaseBuilder3 = {"11:11:11 AM", "11:11:11 AM", "08:11:22 AM"}; //duplicating elements case
        ArrayList<String> testCase3 = new ArrayList<>(Arrays.asList(testCaseBuilder3));
        timeQuickSort(testCase3, 0, testCase3.size() - 1);
        String[] testSolBuilder3 = {"08:11:22 AM", "11:11:11 AM", "11:11:11 AM"};
        ArrayList<String> testSol3 = new ArrayList<>(Arrays.asList(testSolBuilder3));
        assertEquals(testSol3, testCase3);

        String[] testCaseBuilder4 = {"11:20:30 AM", "00:20:30 PM"}; //incorrect input: 00:xx:xx case
        ArrayList<String> testCase4 = new ArrayList<>(Arrays.asList(testCaseBuilder4));
        assertThrows(IllegalArgumentException.class, () -> timeQuickSort(testCase4, 0, testCase4.size() - 1));

        String[] testCaseBuilder5 = {"12:22:21 PM", "2:30:50 AM"}; //incorrect input: time format error case
        ArrayList<String> testCase5 = new ArrayList<>(Arrays.asList(testCaseBuilder5));
        assertThrows(IllegalArgumentException.class, () -> timeQuickSort(testCase5, 0, testCase5.size() - 1));

        String[] testCaseBuilder6 = //some more tests
                {"12:22:21 PM", "12:30:50 AM", "08:19:50 PM", "12:11:49 PM", "01:30:50 AM", "12:22:21 PM",
                        "04:32:58 AM", "09:31:11 AM", "05:18:47 PM", "11:50:20 PM", "05:22:19 AM", "03:21:43 PM"};
        ArrayList<String> testCase6 = new ArrayList<>(Arrays.asList(testCaseBuilder6));
        timeQuickSort(testCase6, 0, testCase6.size() - 1);
        String[] testSolBuilder6 = {"12:30:50 AM", "01:30:50 AM", "04:32:58 AM", "05:22:19 AM", "09:31:11 AM",
                "12:11:49 PM", "12:22:21 PM", "12:22:21 PM", "03:21:43 PM", "05:18:47 PM", "08:19:50 PM", "11:50:20 PM"};
        ArrayList<String> testSol6 = new ArrayList<>(Arrays.asList(testSolBuilder6));
        assertEquals(testSol6, testCase6);
    }

    //all the sorting logic is already tested in testQuickSort(); testing reading/writing part

    private boolean fileEqualityTest(Path tmp, Path out) {
        File tmpOut = new File(tmp.toString());
        ArrayList<String> tmpArr = new ArrayList<>();
        ArrayList<String> outArr = new ArrayList<>();
        try(BufferedReader buffTmp = new BufferedReader(new FileReader(tmp.toString()));
            BufferedReader buffOut = new BufferedReader(new FileReader(out.toString()))) {
            //yep, writing both the ideal result and the output in arrays is surely not the best solution, but
            //I have no clue how can I compare them using streams and forEach()

            buffTmp.lines().forEach(tmpArr::add);
            buffOut.lines().forEach(outArr::add);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tmpOut.delete());
        return tmpArr.equals(outArr);
    }

    @Test
    public void testSortTimes() {
        //Too lazy, taking the input/output data from the last test :c
        //TODO: fix the stuff with the test file access
        Path tmp = Paths.get("test", "lesson1", "tmp.txt");

        Path in = Paths.get("test", "lesson1", "time_in_test.txt");
        Path out = Paths.get("test", "lesson1", "time_out_test.txt");
        sortTimes(in.toString(), tmp.toString());
        assertTrue(fileEqualityTest(tmp, out));
    }



    @Test
    public void testSortAddresses() {
        Path tmp = Paths.get("test", "lesson1", "tmp.txt");

        Path in = Paths.get("test", "lesson1", "addrIn.txt");
        Path out = Paths.get("test", "lesson1", "addrOut.txt");
        sortAddresses(in.toString(), tmp.toString()); //empty case testing
        assertTrue(fileEqualityTest(tmp, out));

        in = Paths.get("test", "lesson1", "addrIn1.txt");
        out = Paths.get("test", "lesson1", "addrOut1.txt");
        sortAddresses(in.toString(), tmp.toString()); //"ё" case testing
        assertTrue(fileEqualityTest(tmp, out));

        in = Paths.get("test", "lesson1", "addrIn2.txt");
        out = Paths.get("test", "lesson1", "addrOut2.txt");
        sortAddresses(in.toString(), tmp.toString()); //duplicating elements case testing
        assertTrue(fileEqualityTest(tmp, out));

        Path finalIn = Paths.get("test", "lesson1", "addrIn3.txt"); //incorrect format case testing
        assertThrows(IllegalArgumentException.class, () -> sortAddresses(finalIn.toString(), tmp.toString()));

    }

    @Test
    public void testSortTemperatures() {
        Path tmp = Paths.get("test", "lesson1", "tmp.txt");

        Path in = Paths.get("test", "lesson1", "tempIn.txt");
        Path out = Paths.get("test", "lesson1", "tempOut.txt");
        sortTemperatures(in.toString(), tmp.toString()); //empty case testing
        assertTrue(fileEqualityTest(tmp, out));

        in = Paths.get("test", "lesson1", "tempIn1.txt");
        out = Paths.get("test", "lesson1", "tempOut1.txt");
        sortTemperatures(in.toString(), tmp.toString()); //duplicating elements case testing
        assertTrue(fileEqualityTest(tmp, out));

        Path finalIn = Paths.get("test", "lesson1", "tempIn2.txt"); //incorrect format case testing
        assertThrows(IllegalArgumentException.class, () -> sortTemperatures(finalIn.toString(), tmp.toString()));
    }
}