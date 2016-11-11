package java.com.spotter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by dimitri on 03/11/2016.
 */
public class App extends AbstractVerticle {

	public void start() {
		ClusterManager mgr = new HazelcastClusterManager();

		VertxOptions options = new VertxOptions().setClusterManager(mgr);

		Vertx.clusteredVertx(options, res -> {
			if (res.succeeded()) {
				super.vertx = res.result();
			} else {
				// failed!
			}
		});
	}


	

	public void stop() {
	}
}
