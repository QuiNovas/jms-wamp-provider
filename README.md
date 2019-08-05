## jms-wamp-provider

Java server application uses WAMP to integrate PubSub and RPC 

* [WAMP Documentation reference guide](https://wamp-proto.org/) Go here to check about WAMP
* [JMS Documentation reference guide](https://en.wikipedia.org/wiki/Java_Message_Service) Go here to check about JMS

 
## Embedding in other projects

To embed jms-wamp-provider in another maven project is sufficient to include a repository and declare the dependency:

```
<repositories>
  <repository>
    <id>quinovas</id>
    <url>https://dependency.quinovas.com</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
</repositories>
```

Include dependency in your project:

```
<dependency>
      <groupId>com.echostreams</groupId>
      <artifactId>jms-wamp-provider</artifactId>
      <version>1.0-SNAPSHOT</version>
</dependency>
```

## Build from sources

After a git clone of this repository, cd into the cloned sources and make sure you have maven installed to build this:

`mvn install`.

In project_source_folder/target directory will be produced the selfcontained file for the broker with all dependencies and a running script.

## Download Runnable Build

Download the self distribution tar from [QuiNovas](https://quinovas.com/artifact/download/quinovas/generic/jms-wamp-provider-1.0-SNAPSHOT-bin.tar.gz) ,
then untar and start the broker listening on `` port and enjoy!

Ubuntu

```
tar xvf jms-wamp-provider-1.0-SNAPSHOT-bin.tar.gz
cd jms-wamp-provider-1.0-SNAPSHOT
chmod 777 startApp.sh
./startApp.sh
```

Or if you are on Windows shell, Unzip the build

```
 cd jms-wamp-provider-1.0-SNAPSHOT
 .\startApp.bat
```

---
