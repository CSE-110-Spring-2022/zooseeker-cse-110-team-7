package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

//TODO: Planner Database Tests
/**
 * Tests the Database
 * - Update [Incomplete] Note: this is another synonym for Add
 * - Delete [Incomplete]
 * - Find   [Incomplete]
 * */
@RunWith(AndroidJUnit4.class)
public class PlannerDatabaseTest {
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
        List<String> some_tags = new ArrayList<>(); some_tags.add("tag1"); some_tags.add("tag2");
        List<String> empty_tags = new ArrayList<>();
        NodeItem n1 = new NodeItem("n1", "regular_node", "exhibit", some_tags);
        NodeItem n2 = new NodeItem("n2", "empty_node", "exhibit", empty_tags);
        long id1 = nodeDao.insert(n1); long id2 = nodeDao.insert(n2);

        /* DELETE ITEM 1 */
        NodeItem retrievedItem = nodeDao.get(id1);
        assertEquals(n1, retrievedItem);    // check that items are the same
        long delNum = nodeDao.delete(retrievedItem);
        assertEquals(1, delNum);    //
        assertNull(nodeDao.get(id1));       // check that item cannot be retrieved

        /* DELETE ITEM 2 */
        retrievedItem = nodeDao.get(id2);
        assertEquals(n2, retrievedItem);    // check that items are the same
        delNum = nodeDao.delete(retrievedItem);
        assertEquals(1, delNum);    //
        assertNull(nodeDao.get(id2));       // check that item cannot be retrieved
    }
}
