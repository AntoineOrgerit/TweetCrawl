# Projet Multi-Agent - Visualisation de tweets

#### FOUGERON Lisa<br>RAYEZ Laurent<br>ORGERIT Antoine<br>GRÉAU François
___
![Page 1 Sujet](uml/SujetP1.jpg)

![Page 1 Sujet](uml/SujetP2.jpg)

## Dépendences
Ce projet dépend de l'utilitaire open source de manipulation de graphes GraphViz, et notamment de la commande `dot`.
Il est donc impératif que ce dernier soit installé et que la commande soit connue après des variables d'environnement du système d'exploitation.

## Utilisation
L'ensemble du projet et ses librairies doivent être installés avec la commande `mvn install`. Il permet de télécharger les dépendences suivantes :
- Twitter4j, permettant de consommer l'API Twitter ;
- JADE, permettant de mettre en place et de lancer un système multi-agent.

Le lancement du projet s'effectue avec la commande `mvn exec:java`.

## Diagrammes :
- ### Use-case<br> ![Use Case Diagram](uml/usecase.png)
- ### Classes<br> ![Class Diagram](uml/classes.png)
- ### Collaboration<br> ![Collaboration Diagram](uml/collaboration.png)