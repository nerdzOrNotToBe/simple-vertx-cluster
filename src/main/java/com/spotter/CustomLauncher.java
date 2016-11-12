package com.spotter;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.TcpIpConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by dimitri on 12/11/2016.
 */
public class CustomLauncher extends VertxCommandLauncher implements VertxLifecycleHooks {

	Logger logger = LoggerFactory.getLogger(CustomLauncher.class);
	/**
	 * Main entry point.
	 *
	 * @param args the user command line arguments.
	 */
	public static void main(String[] args) {
		new CustomLauncher().dispatch(args);
	}

	/**
	 * Utility method to execute a specific command.
	 *
	 * @param cmd  the command
	 * @param args the arguments
	 */
	public static void executeCommand(String cmd, String... args) {
		new CustomLauncher().execute(cmd, args);
	}

	/**
	 * Hook for sub-classes of {@link CustomLauncher} after the config has been parsed.
	 *
	 * @param config the read config, empty if none are provided.
	 */
	public void afterConfigParsed(JsonObject config) {
	}

	/**
	 * Hook for sub-classes of {@link CustomLauncher} before the vertx instance is started.
	 *
	 * @param options the configured Vert.x options. Modify them to customize the Vert.x instance.
	 */
	public void beforeStartingVertx(VertxOptions options) {
		logger.info("===================== Before Starting Vertx =======================");
		String groupName = System.getenv("vertx_cluster_name");
		String groupPwd = System.getenv("vertx_cluster_pass");
		if(groupName == null || groupName.isEmpty()){
			groupName = "spotter";
		}
		if(groupPwd == null || groupPwd.isEmpty()){
			groupPwd = "spotter";
		}
		logger.info("===================== Vertx CLuster Group =======================");
		logger.info("group: " + groupName);
		Config hazelcastConfig = new Config();
		MulticastConfig multicastConfig=new MulticastConfig();
		multicastConfig.setEnabled(false);
		TcpIpConfig tcpIpConfig=new TcpIpConfig().addMember("10.42.0-255.0-255").setEnabled(true);
		hazelcastConfig.getNetworkConfig().getJoin().setMulticastConfig(multicastConfig).setTcpIpConfig(tcpIpConfig);
		GroupConfig groupConfig = new GroupConfig(groupName,groupPwd);
		hazelcastConfig.setGroupConfig(groupConfig);
		ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
		options.setClusterManager(mgr);
	}

	/**
	 * Hook for sub-classes of {@link CustomLauncher} after the vertx instance is started.
	 *
	 * @param vertx the created Vert.x instance
	 */
	public void afterStartingVertx(Vertx vertx) {

	}

	/**
	 * Hook for sub-classes of {@link CustomLauncher} before the verticle is deployed.
	 *
	 * @param deploymentOptions the current deployment options. Modify them to customize the deployment.
	 */
	public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {

	}

	/**
	 * A deployment failure has been encountered. You can override this method to customize the behavior.
	 * By default it closes the `vertx` instance.
	 *
	 * @param vertx             the vert.x instance
	 * @param mainVerticle      the verticle
	 * @param deploymentOptions the verticle deployment options
	 * @param cause             the cause of the failure
	 */
	public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
		// Default behaviour is to close Vert.x if the deploy failed
		vertx.close();
	}
}
