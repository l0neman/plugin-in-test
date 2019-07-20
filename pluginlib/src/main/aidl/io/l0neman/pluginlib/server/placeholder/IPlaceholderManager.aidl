package io.l0neman.pluginlib.server.placeholder;

/**
 * Created by l0neman on 2019/07/20.
 */
interface IPlaceholderManager {

    String queryKeyActivity(in String appliedActivityName);

    String queryKeyService(in String appliedServiceName);

    String applyActivity(in String keyActivityName);

    String applyService(in String keyServiceName);
}
