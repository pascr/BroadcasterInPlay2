# Experiment with Play 2.1 Enumerator and Concurrent.broadcast

This very small project shows what Concurrent.broadcast does (was "hub" in Play 2.0)

## How to use, what to see
Clone and run the Play 2.1 project (with scala 2.10 and Playframework 2.1)
Open to different browser and check http://localhost:9000/serverevents, you should see two different list of numbers (after a while because there is no client side handling of the Server Sent Events).
Then go to http://localhost:9000/servereventshub (with two browser windows). You should see that the two browser get the same stream of numbers.