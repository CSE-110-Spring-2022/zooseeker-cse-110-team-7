package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.planner.NodeDao;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


//TODO: Planner Database Tests
/**
 * Tests the Database
 * - Update [Complete] Note: this is another synonym for Add
 * - Delete [Complete]
 * - Find   [Complete]
 * */
@RunWith(AndroidJUnit4.class)
public class NodeDatabaseTest {
    private NodeDao nodeDao;
    private NodeDatabase db;

    @Before
    public void CreateDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NodeDatabase.class)
                .allowMainThreadQueries()
                .build();
        nodeDao = db.nodeDao();
    }

    @After
    public void CloseDatabase(){
        db.close();
    }

    @Test
    public void UpdateTest(){
        /* SETUP */
        List<String> some_tags = new ArrayList<>(); some_tags.add("tag1"); some_tags.add("tag2");
        List<String> empty_tags = new ArrayList<>();
        NodeItem n1 = new NodeItem("n1", "regular_node", "exhibit", some_tags);
        NodeItem n2 = new NodeItem("n2", "empty_node", "exhibit", empty_tags);
        nodeDao.insert(n1); nodeDao.insert(n2);

        /* CHECK INSERT */
        NodeItem retrievedItem = nodeDao.get(n1.id);
        assertTrue(retrievedItem.equals(n1));       // check that items are the same
        retrievedItem = nodeDao.get(n2.id);
        assertTrue(retrievedItem.equals(n2));       // check that items are the same

        /* UPDATE NAME AND TAGS */
        //swap names
        String nameTemp = n1.name;
        n1.name = n2.name;
        n2.name = nameTemp;

        //swap tags
        List<String> tagsTemp = n1.tags;
        n1.tags = n2.tags;
        n2.tags = tagsTemp;

        nodeDao.update(n1); nodeDao.update(n2);

        /* CHECK UPDATE */
        retrievedItem = nodeDao.get(n1.id);
        assertTrue(retrievedItem.equals(n1));       // check that items are the same
        retrievedItem = nodeDao.get(n2.id);
        assertTrue(retrievedItem.equals(n2));       // check that items are the same
    }

    @Test
    public void DeleteTest(){ //Note: Delete = Remove from UI not delete from database
        /*-=-=-=-=- Delete One -=-=-=-=-*/
        /* SETUP */
        List<String> some_tags = new ArrayList<>(); some_tags.add("tag1"); some_tags.add("tag2");
        List<String> empty_tags = new ArrayList<>();
        NodeItem n1 = new NodeItem("n1", "regular_node", "exhibit", some_tags);
        NodeItem n2 = new NodeItem("n2", "empty_node", "exhibit", empty_tags);
        nodeDao.insert(n1); nodeDao.insert(n2);

        /* DELETE ITEM 1 */
        NodeItem retrievedItem = nodeDao.get(n1.id);
        assertTrue(retrievedItem.equals(n1));       // check that items are the same
        long delNum = nodeDao.delete(retrievedItem);// number of items deleted
        assertEquals(1, delNum);            // check if only 1 item deleted
        assertNull(nodeDao.get(n1.id));             // check that item cannot be retrieved

        /* DELETE ITEM 2 */
        retrievedItem = nodeDao.get(n2.id);
        assertTrue(retrievedItem.equals(n2));       // check that items are the same
        delNum = nodeDao.delete(retrievedItem);     // number of items deleted
        assertEquals(1, delNum);            // check if only 1 item deleted
        assertNull(nodeDao.get(n2.id));             // check that item cannot be retrieved

        /*-=-=-=-=- Delete All -=-=-=-=-*/
        /* SETUP */
        nodeDao.insert(n1); nodeDao.insert(n2);

        /* DELETE ALL */
        retrievedItem = nodeDao.get(n1.id); assertTrue(retrievedItem.equals(n1));
        retrievedItem = nodeDao.get(n2.id); assertTrue(retrievedItem.equals(n2));

        delNum = nodeDao.deleteAll();
        assertEquals(2, delNum);            // check if 2 items deleted

        // check that items cannot be retrieved
        assertNull(nodeDao.get(n1.id)); assertNull(nodeDao.get(n2.id));

    }

    @Test
    public void FindTest(){
        /* SETUP */
        List<String> some_tags = new ArrayList<>(); some_tags.add("AA"); some_tags.add("AB");
        List<String> some_tags_2 = new ArrayList<>(); some_tags_2.add("BB"); some_tags_2.add("AC");
        List<String> empty_tags = new ArrayList<>();
        NodeItem n1 = new NodeItem("n1", "regular_node", "exhibit", some_tags);
        NodeItem n2 = new NodeItem("n2", "empty_node", "undefined", empty_tags);
        NodeItem n3 = new NodeItem("n3", "place", "intersection", some_tags_2);
        n1.onPlanner = true; n2.onPlanner = true;
        nodeDao.insert(n1); nodeDao.insert(n2); nodeDao.insert(n3);
        List<Boolean> onPlannerBools = new ArrayList<>();
        List<String> kinds = new ArrayList<>();
        String queryString = "";
        List<NodeItem> items = new ArrayList<>();

        /* QUERY ONLY EXHIBITS */
        {
            onPlannerBools.add(true);
            onPlannerBools.add(false);
            kinds.add("exhibit");
            items = nodeDao.getByKind(onPlannerBools, kinds);
            assertEquals(1, items.size());
            assertTrue(items.get(0).equals(n1));

            kinds.clear();
            kinds.add("undefined");
            items = nodeDao.getByKind(onPlannerBools, kinds);
            assertEquals(1, items.size());
            assertTrue(items.get(0).equals(n2));

            kinds.clear();
            kinds.add("intersection");
            items = nodeDao.getByKind(onPlannerBools, kinds);
            assertEquals(1, items.size());
            assertTrue(items.get(0).equals(n3));
        }

        /* QUERY ONLY PLANNER ITEMS */
        {
            items = nodeDao.getByOnPlanner(false);
            assertEquals(1, items.size());
            assertTrue(items.get(0).equals(n3));

            items = nodeDao.getByOnPlanner(true);
            assertEquals(2, items.size());
            assertTrue(items.get(0).equals(n1));
            assertTrue(items.get(1).equals(n2));
        }

        /* QUERY INTERSECTION OF ALL KINDS, ALL PLANNER ITEMS, TAG = `queryString` */
        {
            items.clear();
            kinds.clear();
            onPlannerBools.clear();
            onPlannerBools.add(true);
            onPlannerBools.add(false);
            kinds.add("exhibit");
            kinds.add("intersection");
            kinds.add("undefined");
            queryString = "%A%";
            items = nodeDao.getByFilter(onPlannerBools, kinds, queryString);
            assertEquals(2, items.size());
            assertTrue(items.get(0).equals(n1));
            assertTrue(items.get(1).equals(n3));
        }
    }
}
