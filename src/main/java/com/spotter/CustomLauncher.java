package com.spotter;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.GroupProperty;
import com.hazelcast.spi.discovery.integration.DiscoveryServiceSettings;
import com.ranger.hazelcast.servicediscovery.RancherDiscoveryStrategyFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.net.InetAddress;

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
		String groupName = System.getenv("cluster-name");
		String stackName = System.getenv("stack-name");
		String envName = System.getenv("environment-name");
		String rancherApi = System.getenv("rancher-api");
		String groupPwd = System.getenv("cluster-pass");
		if(groupName == null || groupName.isEmpty()){
			groupName = "spotter";
		}
		if(groupPwd == null || groupPwd.isEmpty()){
			groupPwd = "spotter";
		}
		if(stackName == null || groupName.isEmpty()){
			stackName = "vertx1";
		}
		if(envName == null || groupPwd.isEmpty()){
			envName = "Default";
		}
		if(rancherApi == null || groupName.isEmpty()){
			rancherApi = "http://10.34.0.252:8080/v1";
		}
		logger.info("cluster name: " + groupName);
		Config config = new Config();
		config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED, "true");
		config.setProperty(GroupProperty.DISCOVERY_SPI_PUBLIC_IP_ENABLED, "true");
		config.setProperty(GroupProperty.SOCKET_CLIENT_BIND_ANY, "false");
		config.setProperty(GroupProperty.SOCKET_BIND_ANY, "false");
		NetworkConfig networkConfig = config.getNetworkConfig();
		//networkConfig.setInterfaces(new InterfacesConfig().addInterface("10.42.*.*").setEnabled(true));
		//networkConfig.getInterfaces().addInterface(InetAddress.getLocalHost().getHostAddress()).setEnabled(true);
		JoinConfig joinConfig = networkConfig.getJoin();
		joinConfig.getTcpIpConfig().setEnabled(false);
		joinConfig.getMulticastConfig().setEnabled(false);
		joinConfig.getAwsConfig().setEnabled(false);
		DiscoveryConfig discoveryConfig = joinConfig.getDiscoveryConfig();
		DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new RancherDiscoveryStrategyFactory());
		discoveryStrategyConfig.addProperty("cluster-name", groupName);
		discoveryStrategyConfig.addProperty("stack-name", stackName);
		discoveryStrategyConfig.addProperty("environment-name", envName);
		discoveryStrategyConfig.addProperty("rancher-api", rancherApi);
		discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);
		GroupConfig groupConfig = new GroupConfig(groupName,groupPwd);
		config.setGroupConfig(groupConfig);
		ClusterManager mgr = new HazelcastClusterManager(config);
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
