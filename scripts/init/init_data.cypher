CALL apoc.cypher.runMany("MATCH(n) DETACH DELETE n;
CREATE (:User {provider: 'google', id: 'bruno.m.lourenco97@gmail.com', deprecated: false})-[:CONTAINS_DETAILS {version: 1}]->(:UserDetails {role: 'admin'});", {})