package com.news.dbconfig;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraTemplate;

import javax.annotation.PreDestroy;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Robson Martins
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CassandraConfig {
    public static final String CASSANDRA_URL = "cassandra_url";

    private static final Pattern PATTERN = Pattern.compile("^([^:]+):(\\d+)/(\\w+)$");
    private static final Logger log = LoggerFactory.getLogger(CassandraConfig.class);

    @Autowired
    private Environment env;

    private String host;
    private int port;
    private String keyspace;

    private Session session;

    @Bean
    public CassandraTemplate cassandraTemplate() {
        if (!this.isCassandraUrlProvided()) {
            return null;
        }
        Cluster cluster = Cluster.builder().addContactPointsWithPorts(Collections.singletonList(new InetSocketAddress(host, port))).build();
        session = cluster.connect();

        this.recreateDatabase();

        session.execute("USE " + keyspace);

        CassandraTemplate cassandraTemplate = new CassandraTemplate(session);
        return cassandraTemplate;
    }

    protected void recreateDatabase() {
        boolean needToCreate = true;
        List<Row> rows = session.execute("select * from system.schema_keyspaces").all();
        for (Row row : rows) {
            if (keyspace.equals(row.getString(0))) {
                needToCreate = false;
                break;
            }
        }
        
        if (needToCreate) {
            session.execute("CREATE KEYSPACE " + keyspace + " with replication = {'class':'SimpleStrategy', 'replication_factor':1}");
            session.execute("USE " + keyspace);
            session.execute("CREATE TABLE " + keyspace + ".newsarticlenosql (\n" +
                    "    objectid text PRIMARY KEY,\n" +
                    "    id int ,\n" +
                    "    title text,\n" +
                    "    text_content text,\n" +
                    "    createdTimestamp timestamp\n" +
                    ")");
            session.execute("CREATE INDEX id_newsarticlenosql ON " + keyspace + ".newsarticlenosql (id)");
        }
    }

    @PreDestroy
    protected void destroy() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    protected boolean parseConnectionString(String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            host = matcher.group(1);
            port = Integer.parseInt(matcher.group(2));
            keyspace = matcher.group(3);

            return true;
        } else {
            log.error("Can't parse cassandra_url: " + text);
            return false;
        }
    }

    public boolean isCassandraUrlProvided() {
        String url = env.getProperty(CASSANDRA_URL);
        if (url == null || !this.parseConnectionString(url)) {
            return false;
        }

        return true;
    }
}
