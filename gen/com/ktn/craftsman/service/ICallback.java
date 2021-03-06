/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: H:\\Craftsman\\src\\com\\ktn\\craftsman\\service\\ICallback.aidl
 */
package com.ktn.craftsman.service;
public interface ICallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ktn.craftsman.service.ICallback
{
private static final java.lang.String DESCRIPTOR = "com.ktn.craftsman.service.ICallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ktn.craftsman.service.ICallback interface,
 * generating a proxy if needed.
 */
public static com.ktn.craftsman.service.ICallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ktn.craftsman.service.ICallback))) {
return ((com.ktn.craftsman.service.ICallback)iin);
}
return new com.ktn.craftsman.service.ICallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_showResult:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.showResult(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ktn.craftsman.service.ICallback
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
@Override public void showResult(java.lang.String result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(result);
mRemote.transact(Stub.TRANSACTION_showResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_showResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void showResult(java.lang.String result) throws android.os.RemoteException;
}
