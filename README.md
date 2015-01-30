# nanoRest making MicroServices simpler...
A convention over configuration approach to rest client and server development

Lots of people are spending lots of time cooking up microservices and many of these
talk to each other using some form of REST.

Spring has lots of annotations and templates to make things easier, but nanoRest is an attempt to:
i) Make it easier to create consistent looking REST controllers requiring a minimum of annotations.
ii) Create clients automatically based on a shared interface used to create the controller.
iii) Ensure that the interface controls what both the client and server look like, resulting in 
a lower burden on testing to ensure everything plays nicely together.

How does it do all of these things?

Good Question. Glad to see you're interested. Sitting comfortably? Then here we go...


