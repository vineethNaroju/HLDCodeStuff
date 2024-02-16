Cluster -> a collection of nodes arranged in a ring architecture
Keyspace -> similar to schema, need to set Replication Factor & Replica Placement Strategy & Column Families.
Column Family -> similar to tables, gives collection of rows represented by Map<RowKey, SortedMap<ColumnKey, ColumnMap>>
Column -> Name, value and timestamp.

start the dockerfile
and then run
docker exec -it <container_name> cqlsh