openelisglobal-plugins
======================

Repository for plugins for openelisglobal

[![Build Status](https://github.com/openelisglobal/openelisglobal-plugins/actions/workflows/ci.yml/badge.svg)](https://github.com/openelisglobal/openelisglobal-plugins/actions/workflows/ci.yml)

For Building The Plugins 

1. Got to the [Parent pom file](./pom.xml)
1. set the right absolute `SystemPath path` to the `openelisglobal` dependence located under the [lib](./lib/) folder
1. Run the Maven Build  

    ```mvn clean install```
1. Find the built plugin jars under the `plugins` directory 

