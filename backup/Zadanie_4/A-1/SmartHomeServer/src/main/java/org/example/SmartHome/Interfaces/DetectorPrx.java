//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `SmartHome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHome.Interfaces;

public interface DetectorPrx extends SmartHome.Interfaces.SmartDevicePrx
{
    default void setSensitivityLevel(int level)
        throws SmartHome.Errors.ActionNotPermitted,
               SmartHome.Errors.SensitivityLevelOutOfRange
    {
        setSensitivityLevel(level, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void setSensitivityLevel(int level, java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted,
               SmartHome.Errors.SensitivityLevelOutOfRange
    {
        try
        {
            _iceI_setSensitivityLevelAsync(level, context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(SmartHome.Errors.SensitivityLevelOutOfRange ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> setSensitivityLevelAsync(int level)
    {
        return _iceI_setSensitivityLevelAsync(level, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> setSensitivityLevelAsync(int level, java.util.Map<String, String> context)
    {
        return _iceI_setSensitivityLevelAsync(level, context, false);
    }

    /**
     * @hidden
     * @param iceP_level -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_setSensitivityLevelAsync(int iceP_level, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "setSensitivityLevel", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_setSensitivityLevel);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeInt(iceP_level);
                 }, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_setSensitivityLevel =
    {
        SmartHome.Errors.ActionNotPermitted.class,
        SmartHome.Errors.SensitivityLevelOutOfRange.class
    };

    default int getSensitivityLevel()
        throws SmartHome.Errors.ActionNotPermitted
    {
        return getSensitivityLevel(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default int getSensitivityLevel(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            return _iceI_getSensitivityLevelAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<java.lang.Integer> getSensitivityLevelAsync()
    {
        return _iceI_getSensitivityLevelAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Integer> getSensitivityLevelAsync(java.util.Map<String, String> context)
    {
        return _iceI_getSensitivityLevelAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Integer> _iceI_getSensitivityLevelAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Integer> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getSensitivityLevel", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getSensitivityLevel);
        f.invoke(true, context, null, null, istr -> {
                     int ret;
                     ret = istr.readInt();
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getSensitivityLevel =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default String getLocation()
        throws SmartHome.Errors.ActionNotPermitted
    {
        return getLocation(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String getLocation(java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            return _iceI_getLocationAsync(context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> getLocationAsync()
    {
        return _iceI_getLocationAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> getLocationAsync(java.util.Map<String, String> context)
    {
        return _iceI_getLocationAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_getLocationAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getLocation", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getLocation);
        f.invoke(true, context, null, null, istr -> {
                     String ret;
                     ret = istr.readString();
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getLocation =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    default void setLocation(String location)
        throws SmartHome.Errors.ActionNotPermitted
    {
        setLocation(location, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void setLocation(String location, java.util.Map<String, String> context)
        throws SmartHome.Errors.ActionNotPermitted
    {
        try
        {
            _iceI_setLocationAsync(location, context, true).waitForResponseOrUserEx();
        }
        catch(SmartHome.Errors.ActionNotPermitted ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> setLocationAsync(String location)
    {
        return _iceI_setLocationAsync(location, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> setLocationAsync(String location, java.util.Map<String, String> context)
    {
        return _iceI_setLocationAsync(location, context, false);
    }

    /**
     * @hidden
     * @param iceP_location -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_setLocationAsync(String iceP_location, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "setLocation", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_setLocation);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_location);
                 }, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_setLocation =
    {
        SmartHome.Errors.ActionNotPermitted.class
    };

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static DetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), DetectorPrx.class, SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static DetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), DetectorPrx.class,  SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static DetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), DetectorPrx.class, SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static DetectorPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), DetectorPrx.class, SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static DetectorPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, DetectorPrx.class, SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static DetectorPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, DetectorPrx.class,  SmartHome.Interfaces._DetectorPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default DetectorPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (DetectorPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default DetectorPrx ice_adapterId(String newAdapterId)
    {
        return (DetectorPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default DetectorPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (DetectorPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default DetectorPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (DetectorPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default DetectorPrx ice_invocationTimeout(int newTimeout)
    {
        return (DetectorPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default DetectorPrx ice_connectionCached(boolean newCache)
    {
        return (DetectorPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default DetectorPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (DetectorPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default DetectorPrx ice_secure(boolean b)
    {
        return (DetectorPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default DetectorPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (DetectorPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default DetectorPrx ice_preferSecure(boolean b)
    {
        return (DetectorPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default DetectorPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (DetectorPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default DetectorPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (DetectorPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default DetectorPrx ice_collocationOptimized(boolean b)
    {
        return (DetectorPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default DetectorPrx ice_twoway()
    {
        return (DetectorPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default DetectorPrx ice_oneway()
    {
        return (DetectorPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default DetectorPrx ice_batchOneway()
    {
        return (DetectorPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default DetectorPrx ice_datagram()
    {
        return (DetectorPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default DetectorPrx ice_batchDatagram()
    {
        return (DetectorPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default DetectorPrx ice_compress(boolean co)
    {
        return (DetectorPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default DetectorPrx ice_timeout(int t)
    {
        return (DetectorPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default DetectorPrx ice_connectionId(String connectionId)
    {
        return (DetectorPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default DetectorPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (DetectorPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::Interfaces::Detector";
    }
}
