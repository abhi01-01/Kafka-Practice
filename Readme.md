# Get the visuals here.

* [The Kafka Blueprint](https://drive.google.com/file/d/19smdkYKejUaE7FUCutCgteXYobXJryaw/view?usp=sharing)
* [Kafka System Internals](https://drive.google.com/file/d/1Pjur1ivoZ7SbQZhqS6KujDoTXt-X8d8h/view?usp=sharing)
* [Mastering Kafka KRaft](https://drive.google.com/file/d/12G2oI8ohy6kJATFIjkc2nU88K_Ld91WV/view?usp=sharing)
* [Kafka Producer](https://drive.google.com/file/d/1ezODjFg2xTW5XFwJQdio5CK7nvwgVe3a/view?usp=sharing)
* [Kafka Consumer Architecture](https://drive.google.com/file/d/16hfUSO43WuCv5hQVJ7HPi1X8rZyMA06Q/view?usp=sharing)
* [String Kafka JSON Serialization](https://drive.google.com/file/d/1BlyHZ3RZnXotkH3gQGxPoY2_irHSrMRE/view?usp=sharing)
* [Mastering the Kafka Integration](https://drive.google.com/file/d/1FkTsAMvn5GKgZ_m-WDGgDd8rZdHk1PbS/view?usp=sharing)


# Kafka Using Kraft (Some Usefull Commands)

Here’s a **proper local KRaft cluster (multi-broker, persistent, production-like)**

### 1. Directory structure

Create clean, persistent folders:

```bash
mkdir -p ~/kafka-cluster/{broker1,broker2,broker3}
```

Each broker will have its own log dir.


### 2. Generate ONE cluster UUID

```bash
bin/kafka-storage.sh random-uuid
```

Save it somewhere. You’ll reuse this for all brokers.


### 3. Create configs for 3 brokers

Start from:

```
config/server.properties
```

Create 3 copies:

```bash
cp config/server.properties config/broker1.properties
cp config/server.properties config/broker2.properties
cp config/server.properties config/broker3.properties
```

### 4. Modify configs (this is where most people mess up)

### Broker 1 (`broker1.properties`)

```properties
node.id=1
process.roles=broker,controller
log.dirs=/home/abhising/kafka-cluster/broker1

listeners=PLAINTEXT://:9092,CONTROLLER://:9093
advertised.listeners=PLAINTEXT://localhost:9092
controller.listener.names=CONTROLLER

controller.quorum.voters=1@localhost:9093,2@localhost:9095,3@localhost:9097
```

---

### Broker 2 (`broker2.properties`)

```properties
node.id=2
process.roles=broker,controller
log.dirs=/home/abhising/kafka-cluster/broker2

listeners=PLAINTEXT://:9094,CONTROLLER://:9095
advertised.listeners=PLAINTEXT://localhost:9094
controller.listener.names=CONTROLLER

controller.quorum.voters=1@localhost:9093,2@localhost:9095,3@localhost:9097
```

---

### Broker 3 (`broker3.properties`)

```properties
node.id=3
process.roles=broker,controller
log.dirs=/home/abhising/kafka-cluster/broker3

listeners=PLAINTEXT://:9096,CONTROLLER://:9097
advertised.listeners=PLAINTEXT://localhost:9096
controller.listener.names=CONTROLLER

controller.quorum.voters=1@localhost:9093,2@localhost:9095,3@localhost:9097
```


### 5. Format ALL brokers (same UUID)

```bash
bin/kafka-storage.sh format -t <UUID> -c config/broker1.properties
bin/kafka-storage.sh format -t <UUID> -c config/broker2.properties
bin/kafka-storage.sh format -t <UUID> -c config/broker3.properties
```

Do this **once only**.

### 6. Start cluster

Open 3 terminals:

```bash
bin/kafka-server-start.sh config/broker1.properties
```

```bash
bin/kafka-server-start.sh config/broker2.properties
```

```bash
bin/kafka-server-start.sh config/broker3.properties
```



### 7. Create a topic (replicated)

```bash
bin/kafka-topics.sh --create \
  --topic test-topic \
  --bootstrap-server localhost:9092 \
  --replication-factor 3 \
  --partitions 3
```


### 8. Test resilience (this is where learning happens)

### Kill one broker

```bash
kill -15 <pid-of-broker1>
```

Produce/consume again → should still work.


### 9. Restart safely

```bash
bin/kafka-server-start.sh config/kraft/broker1.properties
```

No format. No UUID. Just start.

---
### The above is 
* 3 brokers
* quorum-based controllers
* replicated partitions
* fault tolerance (1 broker can die)


---

### Non-negotiable rules going forward

* Never use `/tmp`
* Never re-run `format` unless resetting cluster
* Same UUID across brokers
* Unique `node.id` per broker
* `controller.quorum.voters` must match exactly

---

* Replace `localhost` with `127.0.0.1` in case of any unresolved error while starting the Kafka and with IP in production

* To delete the existing Cluster

```bash
rm -rf ~/kafka-cluster
```

* To check if the cluster exist

```bash
ls -ld ~/kafka-cluster
```

* Identifies network connections and listener status specifically for ports containing the string "909"

```bash
netstat -an | grep 909
```