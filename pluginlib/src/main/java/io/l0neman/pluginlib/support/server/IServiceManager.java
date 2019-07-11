/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package io.l0neman.pluginlib.support.server;
public interface IServiceManager extends android.os.IInterface
{
  /** Default implementation for IServiceManager. */
  public static class Default implements io.l0neman.pluginlib.support.server.IServiceManager
  {
    @Override public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException
    {
    }
    @Override public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements io.l0neman.pluginlib.support.server.IServiceManager
  {
    private static final java.lang.String DESCRIPTOR = "io.l0neman.pluginlib.support.server.IServiceManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an io.l0neman.pluginlib.support.server.IServiceManager interface,
     * generating a proxy if needed.
     */
    public static io.l0neman.pluginlib.support.server.IServiceManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof io.l0neman.pluginlib.support.server.IServiceManager))) {
        return ((io.l0neman.pluginlib.support.server.IServiceManager)iin);
      }
      return new io.l0neman.pluginlib.support.server.IServiceManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_addService:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.IBinder _arg1;
          _arg1 = data.readStrongBinder();
          this.addService(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getService:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.IBinder _result = this.getService(_arg0);
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements io.l0neman.pluginlib.support.server.IServiceManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeStrongBinder(binder);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addService(name, binder);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getService(name);
          }
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static io.l0neman.pluginlib.support.server.IServiceManager sDefaultImpl;
    }
    static final int TRANSACTION_addService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(io.l0neman.pluginlib.support.server.IServiceManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static io.l0neman.pluginlib.support.server.IServiceManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException;
  public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException;
}
