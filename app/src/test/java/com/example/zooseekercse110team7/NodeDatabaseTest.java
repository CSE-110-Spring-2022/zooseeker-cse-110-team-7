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
 * - Update [Incomplete] Note: this is another synonym for Add
 * - Delete [Complete]
 * - Find   [Incomplete]
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
}
