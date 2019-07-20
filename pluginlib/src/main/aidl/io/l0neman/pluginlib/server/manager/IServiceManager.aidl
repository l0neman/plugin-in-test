package io.l0neman.pluginlib.server.manager;

interface IServiceManager {

    void addService(in String name, in IBinder binder);

    IBinder getService(in String name);
}