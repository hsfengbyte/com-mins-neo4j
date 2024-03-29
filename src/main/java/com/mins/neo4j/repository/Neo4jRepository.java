package com.mins.neo4j.repository;

import com.mins.neo4j.domain.GraphObject;
import com.mins.neo4j.domain.NodeObject;
import com.mins.neo4j.domain.RelationshipObject;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

/**
 *
 *  @Author hsfeng
 *  @Create 2019/7/21 17:12
 */
@SuppressWarnings("unchecked")
@NoRepositoryBean
public interface Neo4jRepository extends org.springframework.data.neo4j.repository.Neo4jRepository{

    @Deprecated
    Iterable<NodeObject> findByLabel(String label);

    @Deprecated
    Iterable<RelationshipObject> findByType(String type);

    @Deprecated
    @Query("MATCH (n:NodeObject) WHERE n.label={label} RETURN n")
    Iterable<NodeObject> findNodeObjects(@Param("label") String label);

    @Deprecated
    @Query("MATCH p=()-[r:RelationshipObject]->() WHERE r.type={type} RETURN p")
    Iterable<RelationshipObject> findRelationshipObjects(@Param("type") String type);

    @Deprecated
    @Query("MATCH (n:NodeObject) WHERE n.label={label} DELETE n")
    void deleteByLabel(@Param("label") String label);

    @Deprecated
    @Query("MATCH p=()-[r:RelationshipObject]->() WHERE r.type={type} DELETE p")
    void deleteByType(@Param("type") String type);




    @Query("MATCH (n:NodeObject) WHERE n.groupId={groupId} RETURN n")
    Iterable<NodeObject> findNodeObjects(@Param("groupId") Long groupId);

    @Query("MATCH p=(n:NodeObject)-[*1..2]-(m:NodeObject) WHERE id(n)={id} AND n.groupId={groupId} RETURN m UNION ALL MATCH (n:NodeObject) WHERE id(n)={id} AND n.groupId={groupId} RETURN n as m")
    Iterable<NodeObject> findNodeObjects(@Param("groupId") Long groupId, @Param("id") Long id, @Param("depth") Long depth);

    default void saveNodeObjects(Iterable<NodeObject> iterable){
        saveAll(iterable);
    }

    default void deleteNodeObjects(Iterable<NodeObject> iterable){
        deleteAll(iterable);
    }

    @Query("MATCH p=(n:NodeObject)-[r:RelationshipObject]-() WHERE id(n)={id} AND n.groupId={groupId} DELETE r,n")
    void deleteNodeObjectById(@Param("id") Long id, @Param("groupId") Long groupId);

    @Query("MATCH (n:NodeObject) WHERE n.groupId={groupId} DELETE n")
    void deleteNodeObjects(@Param("groupId") Long groupId);



    @Query("MATCH p=()-[r:RelationshipObject]->() WHERE r.groupId={groupId} RETURN p")
    Iterable<RelationshipObject> findRelationshipObjects(@Param("groupId") Long groupId);

    @Query("MATCH p=(n:NodeObject)-[*1..2]-() WHERE id(n)={id} AND n.groupId={groupId} return p")
    Iterable<RelationshipObject> findRelationshipObjects(@Param("groupId") Long groupId, @Param("id") Long id, @Param("depth") Long depth);

    default void saveRelationshipObjects(Iterable<RelationshipObject> iterable){
        saveAll(iterable);
    }

    default void deleteRelationshipObjects(Iterable<RelationshipObject> iterable){
        deleteAll(iterable);
    }

    @Query("MATCH p=()-[r:RelationshipObject]-() WHERE id(r)={id} AND r.groupId={groupId} DELETE p")
    void deleteRelationshipObjectById(@Param("id") Long id, @Param("groupId") Long groupId);

    @Query("MATCH p=()-[r:RelationshipObject]->() WHERE r.groupId={groupId} DELETE p")
    void deleteRelationshipObjects(@Param("groupId") Long groupId);



    default GraphObject findGraphObject(Long groupId){
        return new GraphObject(groupId, findNodeObjects(groupId), findRelationshipObjects(groupId));
    }

    default GraphObject findGraphObject(Long groupId, Long id, Long depth){
        return new GraphObject(groupId, findNodeObjects(groupId, id, depth), findRelationshipObjects(groupId, id, depth));
    }
    default void saveGroupObject(GraphObject graphObject){
        saveNodeObjects(graphObject.getNodeObjects());
        saveRelationshipObjects(graphObject.getRelationshipObjects());
    }

    default void deleteGroupObject(GraphObject graphObject){
        deleteRelationshipObjects(graphObject.getGroupId());
        deleteNodeObjects(graphObject.getGroupId());
    }

    /*
    @Query("MATCH (n) WHERE n.`properties.name`={0} return n")
    Iterable<NodeObject> getNodeObject(String string);

    @Query("MATCH (n) WHERE n.label={0} AND n.`properties.name`={2} RETURN n")
    Result getNodeObject(String label, String key, String value);

    @Query("MATCH p=()-[r:RelationshipObject]->() RETURN p")
    Iterable<RelationshipObject> getRelationshipObject();
    */
}
