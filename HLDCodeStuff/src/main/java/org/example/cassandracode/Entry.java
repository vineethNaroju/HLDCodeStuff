package org.example.cassandracode;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class Entry {

    Cluster cassCluster;
    Session cassSession;


    void print(Object o) {
        System.out.println(o);
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Hello World !");

        Entry e = new Entry();
        e.demo();




    }

    public void demo() {
        cassCluster = Cluster.builder().addContactPoint("localhost").withPort(9042).build();
        cassSession = cassCluster.newSession();




        print(cassSession.isClosed());

        cassSession.execute("CREATE KEYSPACE IF NOT EXISTS store WITH REPLICATION = {'class': 'SimpleStrategy', " +
                " 'replication_factor':'1' }; ");




    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        cassSession.close();
        cassCluster.close();



    }
}
