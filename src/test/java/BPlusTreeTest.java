import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.bplustree.BPlusTree;
import com.db_engine.DBAppException;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BPlusTreeTest {

    private final int ENTRY_BOUND = 100;

    @Test
    public void insert() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(11);
        List<Integer> data = new ArrayList<>();
        for (int i = 9; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        t.insert(0, 10);
        data.sort(Integer::compareTo);
        Assert.assertEquals(t.toString(), data.toString());

        t = new BPlusTree<>(10);
        for (int i = 9; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        List<Integer> root = Arrays.asList(4);
        List<Integer> leftChild = Arrays.asList(0, 1, 2, 3);
        List<Integer> rightChild = Arrays.asList(4, 5, 6, 7, 8, 9);
        Assert.assertEquals(t.toString(), root + "  \n" + leftChild + "  " + rightChild + "  \n");
    }

    @Test
    public void equivalentQuery() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(5);
        Assert.assertEquals(t.query(0), Collections.emptyList());
        this.load(t);
        for (int i = 0; i < 1000; ++i) {
            int query = ThreadLocalRandom.current().nextInt(ENTRY_BOUND);
            Assert.assertEquals(t.query(query).toString(), VITEquivalentQuery(query).toString());
        }

    }

    @Test
    public void rangeQuery() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(5);
        Assert.assertEquals(t.rangeQuery(0, 10), Collections.emptyList());
        this.load(t);
        for (int i = 0; i < 1000; ++i) {
            int startInclude = ThreadLocalRandom.current().nextInt(ENTRY_BOUND - 1);
            int endExclude = ThreadLocalRandom.current().nextInt(startInclude + 1, ENTRY_BOUND);
            Assert.assertEquals(t.rangeQuery(startInclude, endExclude).toString(),
                    VITRangeQuery(startInclude, endExclude).toString());
        }
    }

    @Test
    public void testUpdate() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>();
        Assert.assertFalse(t.update(0, 1, 2));
        this.load(t);
        Assert.assertEquals(t.query(2), Arrays.asList(2));
        Assert.assertTrue(t.update(2, 2, 101));
        Assert.assertEquals(t.query(2), Arrays.asList(101));
        Assert.assertFalse(t.update(2, 102, 103));
        Assert.assertFalse(t.update(100, 2, 3));
    }

    @Test
    public void testRemove() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>();
        Assert.assertFalse(t.remove(0, 1));
        this.load(t);
        Assert.assertFalse(t.remove(100, 100));
        for (int i = 0; i < 100; ++i) {
            t.remove(i, i);
        }
        this.reverseLoad(t);
        for (int i = ENTRY_BOUND - 1; i >= 0; --i) {
            t.remove(i, i);
        }
        for (int i = 0; i < ENTRY_BOUND; i += 2) {
            t.insert(i, i);
        }
        t.insert(7, 7);
        Assert.assertTrue(t.remove(8, 8));
        for (int i = 30; i >= 0; i -= 2) {
            t.remove(i, i);
        }
    }

    @Test
    public void testRemoveInBatch() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>();
        Assert.assertFalse(t.remove(0));
        this.reverseLoad(t);
        Assert.assertFalse(t.remove(ENTRY_BOUND));
        for (int i = ENTRY_BOUND - 1; i >= 0; --i) {
            Assert.assertTrue(t.remove(i));
        }
    }

    @Test
    public void doesSerializeDeserializeWork() throws DBAppException {
        try {
            BPlusTree<Integer, Integer> t = new BPlusTree<>(5);

            t.serialize("tree.class");
            BPlusTree<Integer, Integer> t2 = BPlusTree.deserialize("tree.class");
            Assert.assertEquals(t.toString(), t2.toString());
        } finally {
            // delete the file

            File file = new File("tree.class");

            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }

    @Test
    public void chaoticTestWithDeserializationString() throws DBAppException {

        try {
            BPlusTree<Integer, Integer> t = new BPlusTree<>();

            t.serialize("tree.class");
            BPlusTree<String, Integer> bPlusTree = BPlusTree.deserialize("tree.class");
            TreeMap<String, Integer> treeMap = new TreeMap<>();

            int times = 100000000;
            int maxKey = 500;
            int maxValue = 100000;

            for (int i = 0; i < times; i++) {
                String key = (int) (Math.random() * maxKey) + "";
                int value = (int) (Math.random() * maxValue);
                if (Math.random() > 0.1) {
                    treeMap.put(key, value);
                    if (!bPlusTree.query(key).isEmpty()) {
                        bPlusTree.remove(key);
                    }
                    bPlusTree.insert(key, value);
                }
                if (Math.random() < 0.5) {
                    String deleteKey = (int) (Math.random() * maxKey) + "";
                    Integer deletedValue = treeMap.remove(deleteKey);
                    boolean deleted = bPlusTree.remove(deleteKey);
                    Assert.assertTrue((deletedValue == null && !deleted) || (deletedValue != null && deleted));
                }

                String getKey = (int) (Math.random() * maxKey) + "";
                Integer exceptedValue = treeMap.get(getKey);
                List<Integer> actualValues = bPlusTree.query(getKey);
                Assert.assertTrue((exceptedValue == null && actualValues.isEmpty())
                        || (exceptedValue != null && actualValues.contains(exceptedValue)));
            }
        } finally {
            // delete the file

            File file = new File("tree.class");

            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }

    @Test
    public void chaoticTestWithDeserialization() throws DBAppException {

        try {
            BPlusTree<Integer, Integer> t = new BPlusTree<>();

            t.serialize("tree.class");
            BPlusTree<Integer, Integer> bPlusTree = BPlusTree.deserialize("tree.class");
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();

            int times = 100000000;
            int maxKey = 500;
            int maxValue = 100000;

            for (int i = 0; i < times; i++) {
                int key = (int) (Math.random() * maxKey);
                int value = (int) (Math.random() * maxValue);
                if (Math.random() > 0.1) {
                    treeMap.put(key, value);
                    if (!bPlusTree.query(key).isEmpty()) {
                        bPlusTree.remove(key);
                    }
                    bPlusTree.insert(key, value);
                }
                if (Math.random() < 0.5) {
                    int deleteKey = (int) (Math.random() * maxKey);
                    Integer deletedValue = treeMap.remove(deleteKey);
                    boolean deleted = bPlusTree.remove(deleteKey);
                    Assert.assertTrue((deletedValue == null && !deleted) || (deletedValue != null && deleted));
                }

                int getKey = (int) (Math.random() * maxKey);
                Integer exceptedValue = treeMap.get(getKey);
                List<Integer> actualValues = bPlusTree.query(getKey);
                Assert.assertTrue((exceptedValue == null && actualValues.isEmpty())
                        || (exceptedValue != null && actualValues.contains(exceptedValue)));
            }
        } finally {
            // delete the file

            File file = new File("tree.class");

            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }

    @Test
    public void chaoticTest() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        BPlusTree<Integer, Integer> bPlusTree = new BPlusTree<>();
        int times = 100000000;
        int maxKey = 500;
        int maxValue = 100000;

        for (int i = 0; i < times; i++) {
            int key = (int) (Math.random() * maxKey);
            int value = (int) (Math.random() * maxValue);
            if (Math.random() > 0.1) {
                treeMap.put(key, value);
                if (!bPlusTree.query(key).isEmpty()) {
                    bPlusTree.remove(key);
                }
                bPlusTree.insert(key, value);
            }
            if (Math.random() < 0.5) {
                int deleteKey = (int) (Math.random() * maxKey);
                Integer deletedValue = treeMap.remove(deleteKey);
                boolean deleted = bPlusTree.remove(deleteKey);
                Assert.assertTrue((deletedValue == null && !deleted) || (deletedValue != null && deleted));
            }

            int getKey = (int) (Math.random() * maxKey);
            Integer exceptedValue = treeMap.get(getKey);
            List<Integer> actualValues = bPlusTree.query(getKey);
            Assert.assertTrue((exceptedValue == null && actualValues.isEmpty())
                    || (exceptedValue != null && actualValues.contains(exceptedValue)));
        }
    }

    private List<Integer> VITEquivalentQuery(int query) {
        Set<Integer> temp = new HashSet<>();
        for (int dataItem = 0; dataItem < ENTRY_BOUND; ++dataItem) {
            if (dataItem == query) {
                temp.add(dataItem);
            }
        }

        List<Integer> res = new ArrayList<>(temp);
        res.sort(Integer::compareTo);
        return res;
    }

    private List<Integer> VITRangeQuery(int startInclude, int endExclude) {
        Set<Integer> temp = new HashSet<>();
        for (int dataItem = 0; dataItem < ENTRY_BOUND; ++dataItem) {
            if (dataItem >= startInclude && dataItem < endExclude) {
                temp.add(dataItem);
            }
        }

        List<Integer> res = new ArrayList<>(temp);
        res.sort(Integer::compareTo);
        return res;
    }

    private void load(BPlusTree<Integer, Integer> bPlusTree) {
        for (int i = 0; i < ENTRY_BOUND; ++i) {
            bPlusTree.insert(i, i);
        }
    }

    private void reverseLoad(BPlusTree<Integer, Integer> bPlusTree) {
        for (int i = ENTRY_BOUND - 1; i >= 0; --i) {
            bPlusTree.insert(i, i);
        }
    }

}