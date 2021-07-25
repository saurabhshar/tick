o How to run the project (preferably with maven Spring Boot plugin or alike, or as an xecutable jar)
As its a Spring Boot project, run below command from project directory  -
./mvnw spring-boot:run

I've configured the service at port:9080, it could be configured in env. specific property file. So, services will be accessible at -
http://localhost:9080/tick
http://localhost:9080/statistics
http://localhost:9080/statistics/{instrument_identifier}

o Which assumptions you made while developing - 
- Assumtion on approximate number of instruments -
- I've assumed, for simplicity, that each instrument is publishing a tick every second. Assuming that there are approximately 25 exchanges with ~2000 tickers on average which are being processed(could be much more as well) there would be around 50K ticks every seconds if all exchanges are open at the same time - this makes 50 requests per milliseconds which could have a collision for timestamp - so created extra space for collision of 1000 size(configurable), which is enough to accomodate ~50 or more collisions coming. If the number grows significantly and we do see collisions, then ticks coming first only are being considered causing event loss.
- Since /statistics is to be calculated at O(1), only way possible seemed to be either prepopulating the stats on every tick, or periodically scheduling it to be calculated. Given the large number of ticks being consumed impacting calculation of statistics for all incoming ticks, I've take approach to rather use scheduling - assuming that the calcuation and response are eventually consistent.
- Assumption for statistics/{instrument_identifier} API - I've assumed its ok to have this API return value at O(n) as there was no clear mention of O(1) requirement for this API. Since the ticks being published are less I've gone ahead with polling when this API is called. This way I've tried to maintain a balance using both the above approaches.
- Application/process calling /tick API are out of scope, only a dummy implementation is provided here.
- timestamp generated in all the ticks is in the same timezone as the TickApplication.
- Standard library for aggregation - My assumption for this point is that its for external jars to jdk.
- There is a chance that stats will remain same if there's no new tick being published - for simplicity sliding window is being cleaned up when adding a new quote only. So, stats will remain constant - i.e. whatever they were at end of the day till next day. [It could be improved with a cleaner thread though]. New assumption - added a stats publishing timestamp thats valid for 60secs window - else reset

o What would you improve if you had more time
- Stats publishing could be improved further and instead of only scheduling /polling/pre-populating a hybrid approach can be taken to populate stats for lets say 25 ticks together or for the interval of 10ms.
- More efforts required towards profiling and tuning if needed.
- As the number of ticks grow, we might think of scaling it with distributed system, considering an intermediate datastore/cache might help in that case.
- Deleting the ticks could be a loss, it could be send to another feed for further analytics or periodically snapped for future use.,

o And, whether you liked the challenge or not - Absolutely liked working on this. I'm eager to discuss this further in coming rounds.
