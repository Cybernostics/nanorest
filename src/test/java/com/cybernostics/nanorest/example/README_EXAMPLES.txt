README Example

These folders contain instances of the server, api and client components.
Normally they would (should) each live in their own maven project.

* The api should simply define the API interface and DTO objects used in common between
  client and server.

* The server contains a simple Spring Boot Application which exposes a NanaRest
  entity controller

* The client shows a simple client which uses the APIs.

The server and client both depend on the API but not on each other.

